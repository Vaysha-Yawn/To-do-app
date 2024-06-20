package com.test.todo_app.view.tools

import com.test.todo_app.R
import com.test.todo_app.domain.model.StateTask

fun getResProgressString(progress: StateTask): Int {
    val res =  when(progress){
        StateTask.newTask -> R.string.status_not_started
        StateTask.inProgress -> R.string.status_in_progress
        StateTask.done -> R.string.status_done
    }
    return res
}

fun getResProgressColor(progress: StateTask): Int {
    val res = when(progress){
        StateTask.newTask -> R.color.not_started
        StateTask.inProgress -> R.color.in_progress
        StateTask.done -> R.color.done
    }
    return res
}

fun getImgResByStatus(state: StateTask): Int {
    return when (state) {
        StateTask.newTask -> R.drawable.new_task
        StateTask.inProgress -> R.drawable.in_progress
        StateTask.done -> R.drawable.done
    }
}
