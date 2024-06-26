package com.test.todo_app.domain.interfaces.view

import androidx.fragment.app.DialogFragment
import com.test.todo_app.domain.model.Task
import com.test.todo_app.view.model.TaskView
import com.test.todo_app.view.screens.DialogMenuTask

interface TaskMenuResponse{
    fun toDone(dialog: DialogFragment, task:TaskView)
    fun toInProgress(dialog: DialogFragment, task:TaskView)
    fun toDelete(dialog: DialogFragment, task:TaskView)
}