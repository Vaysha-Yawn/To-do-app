package com.test.todo_app.domain.model

import com.test.todo_app.R

enum class StateTask{
    newTask, inProgress, done
}

fun StateTask.getResProgressString(): Int {
    val res =  when(this){
        StateTask.newTask -> R.string.status_not_started
        StateTask.inProgress -> R.string.status_in_progress
        StateTask.done -> R.string.status_done
    }
    return res
}

fun StateTask.getResProgressColor(): Int {
    val res = when(this){
        StateTask.newTask -> R.color.not_started
        StateTask.inProgress -> R.color.in_progress
        StateTask.done -> R.color.done
    }
    return res
}

fun StateTask.getImgResByStatus(): Int {
    return when (this) {
        StateTask.newTask -> R.drawable.new_task
        StateTask.inProgress -> R.drawable.in_progress
        StateTask.done -> R.drawable.done
    }
}
