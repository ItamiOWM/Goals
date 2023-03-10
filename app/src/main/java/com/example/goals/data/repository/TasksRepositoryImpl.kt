package com.example.goals.data.repository

import com.example.goals.data.local.dao.TasksDao
import com.example.goals.domain.models.SubTask
import com.example.goals.domain.models.Task
import com.example.goals.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val dao: TasksDao,
) : TasksRepository {

    override fun getTasks(): Flow<List<Task>> {
        return dao.getTasks()
    }

    override fun getTasksByDate(date: String): Flow<List<Task>> {
        return dao.getTasksByDate(date)
    }

    override fun getTasksByDateAndCompleteness(
        date: String,
        isCompleted: Boolean,
    ): Flow<List<Task>> {
        return dao.getTasksByDateAndCompleteness(date, isCompleted)
    }

    override fun getTaskById(id: Int): Flow<Task?> {
        return dao.getTaskById(id)
    }

    override suspend fun addTask(task: Task) {
        dao.addTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.id)
    }

    override suspend fun completeTask(task: Task) {
        val taskToUpdate = task.copy(
            isCompleted = !task.isCompleted,
            subTasks = task.subTasks.map { it.copy(isCompleted = !task.isCompleted) }
        )
        dao.updateTask(taskToUpdate)
    }

    override suspend fun completeSubTask(subTask: SubTask, task: Task) {
        val subtaskIndex = task.subTasks.indexOf(subTask)
        val newList = task.subTasks.toMutableList()
        //Replace objects
        newList.removeAt(subtaskIndex)
        newList.add(subtaskIndex, subTask.copy(isCompleted = !subTask.isCompleted))
        //Updated task
        val updatedTask = task.copy(subTasks = newList)
        dao.updateTask(updatedTask)
    }

    override suspend fun editTask(task: Task) {
        dao.updateTask(task)
    }

}