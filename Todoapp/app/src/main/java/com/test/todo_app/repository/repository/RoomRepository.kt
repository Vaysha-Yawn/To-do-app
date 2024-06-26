package com.test.todo_app.repository.repository

import android.content.Context
import androidx.room.Room
import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.model.convertors.toTask
import com.test.todo_app.domain.model.convertors.toTaskRoom
import com.test.todo_app.repository.abstracts.AppDatabaseParam
import com.test.todo_app.repository.model.TaskRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun read(): Flow<List<TaskRoom>> = dao.getAll()

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




