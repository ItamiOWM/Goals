package com.example.goals.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.goals.domain.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks_table")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE id=:id LIMIT 1")
    suspend fun getTaskById(id: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task)

    @Query("DELETE FROM tasks_table WHERE id=:id")
    suspend fun deleteTask(id: Int)
}