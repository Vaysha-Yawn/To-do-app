package com.test.todo_app.domain.interfaces.view

interface ShowListUpdate{
    fun updateRV(position: IntRange)
    fun addRV(position:Int)
    fun deleteRV(position:Int)
    fun showRV(size:Int)
}