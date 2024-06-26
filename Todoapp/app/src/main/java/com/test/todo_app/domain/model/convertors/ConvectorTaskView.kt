package com.test.todo_app.domain.model.convertors

import com.test.todo_app.domain.model.Task
import com.test.todo_app.repository.model.TaskRoom
import com.test.todo_app.view.model.TaskView


fun TaskView.toTask(): Task {
    val task = this
    return Task(
        task.id,
        task.shortDescription,
        task.fullDescription,
        task.dateCreated,
        task.state
    )
}

fun Task.toTaskView(): TaskView {
    val task = this
    return TaskView(
        task.id,
        task.shortDescription,
        task.fullDescription,
        task.dateCreated,
        task.state
    )
}
