package com.test.todo_app.domain.interfaces.view

import com.test.todo_app.domain.model.Task


interface ShowTaskMenuDialog{
    fun showTaskMenuDialog(task: Task)
}