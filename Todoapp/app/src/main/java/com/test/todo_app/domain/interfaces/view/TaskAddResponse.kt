package com.test.todo_app.domain.interfaces.view

import androidx.fragment.app.DialogFragment

interface TaskAddResponse{
    fun createTask(dialog: DialogFragment, name:String, description:String)
    fun showSuccess(dialog: DialogFragment,)
    fun showError(dialog: DialogFragment,message: String)
}