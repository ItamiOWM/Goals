package com.example.goals.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.goals.domain.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks_table")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE scheduledDate=:date")
    fun getTasksByDate(date: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE scheduledDate=:date AND isCompleted=:isCompleted")
    fun getTasksByDateAndCompleteness(date: String, isCompleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE id=:id LIMIT 1")
    fun getTaskById(id: Int): Flow<Task?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM tasks_table WHERE id=:id")
    suspend fun deleteTask(id: Int)
}