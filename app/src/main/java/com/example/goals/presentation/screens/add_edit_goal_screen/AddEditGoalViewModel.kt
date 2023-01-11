package com.example.goals.presentation.screens.add_edit_goal_screen

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goals.R
import com.example.goals.domain.models.Goal
import com.example.goals.domain.models.InvalidGoalTitleException
import com.example.goals.domain.models.SubGoal
import com.example.goals.domain.repository.GoalsRepository
import com.example.goals.presentation.components.TextFieldState
import com.example.goals.presentation.navigation.Destination.Companion.GOAL_ID_ARG
import com.example.goals.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class AddEditGoalViewModel @Inject constructor(
    private val repository: GoalsRepository,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    var goalTitle by mutableStateOf(
        TextFieldState(hint = application.getString(R.string.enter_title)))
        private set

    var goalContent by mutableStateOf(
        TextFieldState(hint = application.getString(R.string.enter_content)))
        private set

    var goalColor by mutableStateOf(listOfColors.first().toArgb())
        private set

    var subGoals by mutableStateOf(emptyList<SubGoal>())
        private set

    var deadline by mutableStateOf(getDateDaysInAdvance(7)) //Default deadline is 7 days in advance from current date
        private set

    var bottomSheetText by mutableStateOf(EMPTY_STRING)
        private set

    var chosenSubGoalIndex by mutableStateOf<Int?>(null)
        private set

    private val _eventFlow = MutableSharedFlow<AddEditGoalUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentGoalId: Int = UNKNOWN_ID

    private var currentGoal: Goal? = null

    init {
        savedStateHandle.get<Int>(GOAL_ID_ARG)?.let { goalId ->
            if (goalId != UNKNOWN_ID) {
                currentGoalId = goalId
                getGoalById(goalId)
            }
        }
    }

    fun onEvent(event: AddEditGoalEvent) {
        when (event) {
            is AddEditGoalEvent.SubGoalItemSelected -> {
                chosenSubGoalIndex = event.index
            }
            is AddEditGoalEvent.BottomSheetTextChanged -> {
                bottomSheetText = event.text
            }
            is AddEditGoalEvent.DeadlineChange -> {
                deadline = event.deadline
            }
            is AddEditGoalEvent.ColorChange -> {
                goalColor = event.colorInt
            }
            is AddEditGoalEvent.SaveGoal -> {
                saveGoal(
                    currentGoalId,
                    goalTitle.text,
                    goalContent.text,
                    subGoals,
                    currentGoal?.isReached ?: false,
                    startDate = getCurrentDateString(),
                    endDate = deadline,
                    goalColor
                )
            }
            is AddEditGoalEvent.TitleTextChange -> {
                goalTitle = goalTitle.copy(text = event.text, textError = null)
            }
            is AddEditGoalEvent.ContentTextChange -> {
                goalContent = goalContent.copy(text = event.text, textError = null)
            }
            is AddEditGoalEvent.SaveSubGoal -> {
                saveSubGoal(bottomSheetText, chosenSubGoalIndex)
                resetCurrentSubGoalIndex()
            }
            is AddEditGoalEvent.DeleteSubGoal -> {
                deleteSubGoal(chosenSubGoalIndex)
                resetCurrentSubGoalIndex()
            }
            is AddEditGoalEvent.ChangeSubGoalCompleteness -> {
                changeSubGoalCompleteness(event.indexSubGoal)
            }
        }
    }

    private fun saveGoal(
        id: Int,
        title: String,
        content: String,
        subGoals: List<SubGoal>,
        isReached: Boolean,
        startDate: String,
        endDate: String,
        color: Int,
    ) {
        viewModelScope.launch {
            try {
                val goal = Goal(
                    id = id,
                    title = title,
                    content = content,
                    subGoals = subGoals,
                    isReached = isReached,
                    startDate = startDate,
                    endDate = endDate,
                    color = color
                )
                if (id == UNKNOWN_ID) {
                    repository.addGoal(goal) //If id == UNKNOWN_ID then goal is new and should be added
                    _eventFlow.emit(AddEditGoalUiEvent.ShowToast(application.getString(R.string.goal_added)))
                } else {
                    repository.editGoal(goal) //If id != UNKNOWN_ID then goal exists and should be edited
                    _eventFlow.emit(AddEditGoalUiEvent.ShowToast(application.getString(R.string.goal_edited)))
                }
                _eventFlow.emit(AddEditGoalUiEvent.GoalSaved)
            } catch (e: InvalidGoalTitleException) {
                _eventFlow.emit(AddEditGoalUiEvent.ShowToast(e.message.toString()))
                goalTitle = goalTitle.copy(textError = e.message) //Handle error
            }
        }
    }

    private fun getGoalById(id: Int) {
        viewModelScope.launch {
            repository.getGoalById(id).collectLatest { goal ->
                if (goal != null) {
                    goalTitle = goalTitle.copy(text = goal.title)
                    goalContent = goalContent.copy(text = goal.content)
                    goalColor = goal.color
                    deadline = goal.endDate
                    subGoals = goal.subGoals
                    currentGoal = goal
                }
            }
        }
    }

    private fun saveSubGoal(title: String, index: Int?) {
        viewModelScope.launch {
            val subGoal = SubGoal(
                title.trim(),
                false
            )
            val newSubGoals = subGoals.toMutableList()
            subGoals = if (index == null) {
                //If Index is null then it's new SubGoal and we should just add it
                newSubGoals.add(subGoal)
                newSubGoals
            } else {
                //Else then it's edited SubGoal and we should replace it with by index
                newSubGoals.removeAt(index)
                newSubGoals.add(index, subGoal)
                newSubGoals
            }
        }
    }

    private fun deleteSubGoal(index: Int?) {
        if (index != null) {
            viewModelScope.launch {
                val newSubGoalList = subGoals.toMutableList()
                newSubGoalList.removeAt(index)
                subGoals = newSubGoalList
            }
        }
    }

    private fun changeSubGoalCompleteness(index: Int) {
        viewModelScope.launch {
            val newSubGoalList = subGoals.toMutableList()
            val subGoal = newSubGoalList[index]
            newSubGoalList.removeAt(index)
            newSubGoalList.add(index, subGoal.copy(isCompleted = !subGoal.isCompleted))
            subGoals = newSubGoalList
        }
    }

    private fun resetCurrentSubGoalIndex() {
        chosenSubGoalIndex = null
    }

}