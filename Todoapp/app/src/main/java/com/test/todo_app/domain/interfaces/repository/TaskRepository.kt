package com.test.todo_app.domain.interfaces.repository

import com.test.todo_app.domain.model.Task
import com.test.todo_app.repository.model.TaskRoom
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun create(task: Task)
    fun delete(task: Task)
    fun update(task: Task)
    fun read():  Flow<List<TaskRoom>>
}