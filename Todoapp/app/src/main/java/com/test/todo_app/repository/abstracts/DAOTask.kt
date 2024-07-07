package com.test.todo_app.repository.abstracts


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.todo_app.repository.model.TaskRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Dao
interface DAOTask {
    @Query("SELECT * FROM taskroom ORDER BY state DESC")
    fun getAll(): Flow<List<TaskRoom>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskRoom: TaskRoom)

    @Update
    suspend fun update(taskRoom: TaskRoom)

    @Delete
    suspend fun delete(taskRoom: TaskRoom)
}
