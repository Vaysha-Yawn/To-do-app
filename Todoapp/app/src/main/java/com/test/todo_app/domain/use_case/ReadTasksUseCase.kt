package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.Task
import javax.inject.Inject

class ReadTasksUseCase@Inject constructor(
    val repository: TaskRepository
) {
    suspend fun read(): List<Task> {
        return repository.read()
    }
}