package com.example.goals.presentation.navigation

sealed class Destination(val route: String) {

    object HomeScreen : Destination(HOME_SCREEN_ROUTE)

    object TasksScreen : Destination(TASKS_SCREEN_ROUTE)

    object GoalsScreen : Destination(GOALS_SCREEN_ROUTE)

    object NotesScreen : Destination(NOTES_SCREEN_ROUTE)

    object AddEditGoalScreen : Destination(ADD_EDIT_GOAL_SCREEN_ROUTE)

    object AddEditTaskScreen : Destination(ADD_EDIT_TASK_SCREEN_ROUTE)

    object AddEditNoteScreen : Destination(ADD_EDIT_NOTE_SCREEN_ROUTE)

    object GoalInfoScreen: Destination(GOAL_INFO_SCREEN_ROUTE)

    object NoteInfoScreen: Destination(NOTE_INFO_SCREEN_ROUTE)

    companion object {

        private const val HOME_SCREEN_ROUTE = "home"

        private const val TASKS_SCREEN_ROUTE = "tasks"

        private const val GOALS_SCREEN_ROUTE = "goals"

        private const val NOTES_SCREEN_ROUTE = "notes"

        private const val ADD_EDIT_GOAL_SCREEN_ROUTE = "add_edit_goal"

        private const val ADD_EDIT_TASK_SCREEN_ROUTE = "add_edit_task"

        private const val ADD_EDIT_NOTE_SCREEN_ROUTE = "add_edit_note"

        private const val GOAL_INFO_SCREEN_ROUTE = "goal_info"

        private const val NOTE_INFO_SCREEN_ROUTE = "note_info"

        const val GOAL_ID_ARG = "goalId"

        const val NOTE_ID_ARG = "noteId"

        const val TASK_ID_ARG = "taskId"

    }
}