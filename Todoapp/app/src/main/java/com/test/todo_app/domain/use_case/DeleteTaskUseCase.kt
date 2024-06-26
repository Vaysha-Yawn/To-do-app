package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.model.convertors.toTask
import com.test.todo_app.view.model.TaskView
import javax.inject.Inject

class DeleteTaskUseCase@Inject constructor(
    val repository: TaskRepository, val listM: ListTaskManager
) {
    operator fun invoke(task: TaskView) {
        repository.delete(task.toTask())
    }
}