package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    val repository: TaskRepository
) {

    fun updateProgress(task: Task, nextProgress: StateTask):Task {
        val updatedTask = updateTask(task, null, null, nextProgress)
        repository.update(updatedTask)
        return updatedTask
    }

    fun updateText(task: Task, newName: String, newDescription: String):Task {
        val updatedTask = updateTask(task, newName, newDescription, null)
        repository.update(updatedTask)
        return updatedTask
    }

    private fun updateTask(
        oldTask: Task,
        newName: String?,
        newDescription: String?,
        newStateTask: StateTask?
    ): Task {
        val newTask = Task(
            id = oldTask.id,
            shortDescription = newName ?: oldTask.shortDescription,
            fullDescription = newDescription ?: oldTask.fullDescription,
            dateCreated = oldTask.dateCreated,
            state = newStateTask ?: oldTask.state
        )
        return newTask
    }
}