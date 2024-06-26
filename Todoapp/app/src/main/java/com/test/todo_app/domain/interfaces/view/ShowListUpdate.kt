package com.test.todo_app.domain.interfaces.view

import com.test.todo_app.domain.model.Task
import com.test.todo_app.view.model.TaskView

interface ShowListUpdate{
    fun updateRV(newList: List<TaskView>)
}