package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.convertors.toTask
import com.test.todo_app.view.model.TaskView
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    val repository: TaskRepository
) {

    fun updateProgress(task: TaskView, nextProgress: StateTask):TaskView {
        val updatedTask = updateTask(task, null, null, nextProgress)
        repository.update(updatedTask.toTask())
        return updatedTask
    }

    fun updateText(task: TaskView, newName: String, newDescription: String): TaskView {
        val updatedTask = updateTask(task, newName, newDescription, null)
        repository.update(updatedTask.toTask())
        return updatedTask
    }

    private fun updateTask(
        oldTask: TaskView,
        newName: String?,
        newDescription: String?,
        newStateTask: StateTask?
    ): TaskView {
        val newTask = TaskView(
            id = oldTask.id,
            shortDescription = newName ?: oldTask.shortDescription,
            fullDescription = newDescription ?: oldTask.fullDescription,
            dateCreated = oldTask.dateCreated,
            state = newStateTask ?: oldTask.state
        )
        return newTask
    }
}