package com.test.todo_app.domain.interfaces.view

import com.test.todo_app.domain.model.Task

interface ShowListUpdate{
    fun updateRV(newList: List<Task>)
}