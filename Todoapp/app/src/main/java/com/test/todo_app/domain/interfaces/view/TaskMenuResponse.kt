package com.test.todo_app.domain.interfaces.view

import androidx.fragment.app.DialogFragment
import com.test.todo_app.domain.model.Task
import com.test.todo_app.view.screens.DialogMenuTask

interface TaskMenuResponse{
    fun toDone(dialog: DialogFragment, task:Task)
    fun toInProgress(dialog: DialogFragment, task:Task)
    fun toDelete(dialog: DialogFragment, task:Task)
}