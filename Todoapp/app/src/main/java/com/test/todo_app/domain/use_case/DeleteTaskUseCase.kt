package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.Task
import javax.inject.Inject

class DeleteTaskUseCase@Inject constructor(
    val repository: TaskRepository, val listM: ListTaskManager
) {
    fun deleteTask(task: Task) {
        repository.delete(task)
    }
}