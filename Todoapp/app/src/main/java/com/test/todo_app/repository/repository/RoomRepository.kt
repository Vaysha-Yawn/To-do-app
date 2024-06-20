package com.test.todo_app.repository.repository

import android.content.Context
import androidx.room.Room
import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import com.test.todo_app.repository.abstracts.AppDatabaseParam
import com.test.todo_app.repository.tools.toTask
import com.test.todo_app.repository.tools.toTaskRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class RoomRepository(context: Context) : TaskRepository {

    private val db = Room.databaseBuilder(
        context,
        AppDatabaseParam::class.java, "database-task"
    ).build()
    private val dao = db.dao()

    override fun create(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(task.toTaskRoom())
        }
    }

    override suspend fun read(): List<Task> {
        val all = dao.getAll().map { it.toTask() }
        return all
    }

    override fun update(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.update(task.toTaskRoom())
        }
    }

    override fun delete(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(task.toTaskRoom())
        }
    }


}




