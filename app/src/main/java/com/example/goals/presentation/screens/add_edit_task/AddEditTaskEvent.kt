package com.example.goals.presentation.screens.add_edit_task

sealed class AddEditTaskEvent {

    object SaveTask: AddEditTaskEvent()

    data class SaveSubTask(val title: String, val index: Int?): AddEditTaskEvent()

    data class DeleteSubTask(val index: Int) : AddEditTaskEvent()

    data class SubTaskItemSelected(val index: Int?): AddEditTaskEvent()

    data class ChangeSubTaskCompleteness(val subTaskIndex: Int): AddEditTaskEvent()

    data class TitleTextChange(val text: String): AddEditTaskEvent()

    data class ContentTextChange(val text: String): AddEditTaskEvent()

    data class ColorChange(val colorInt: Int): AddEditTaskEvent()

    data class StartTimeChange(val time: Long): AddEditTaskEvent()

    data class EndTimeChange(val time: Long): AddEditTaskEvent()

    data class BottomSheetTextChange(val text: String): AddEditTaskEvent()

    data class DateChange(val date: String): AddEditTaskEvent()

}