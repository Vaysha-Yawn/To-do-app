package com.test.todo_app.repository.abstracts


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.todo_app.repository.entity.TaskRoom


@Dao
interface DAOTask {
    @Query("SELECT * FROM taskroom")
    suspend fun getAll(): List<TaskRoom>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskRoom: TaskRoom)

    @Update
    suspend fun update(taskRoom: TaskRoom)

    @Delete
    suspend fun delete(taskRoom: TaskRoom)
}
