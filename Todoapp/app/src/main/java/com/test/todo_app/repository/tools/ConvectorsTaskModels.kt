package com.test.todo_app.repository.tools

import com.test.todo_app.domain.model.Task
import com.test.todo_app.repository.entity.TaskRoom


fun TaskRoom.toTask(): Task {
    val task = this
    return Task(
        task.id,
        task.shortDescription,
        task.fullDescription,
        task.dateCreated,
        task.state
    )
}

fun Task.toTaskRoom(): TaskRoom {
    val task = this
    return TaskRoom(
        task.id,
        task.shortDescription,
        task.fullDescription,
        task.dateCreated,
        task.state
    )
}
