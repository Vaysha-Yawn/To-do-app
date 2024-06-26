package com.test.todo_app.domain.model

import androidx.annotation.Keep

@Keep
data class Task (
    val id:Int,
    var shortDescription:String,
    var fullDescription: String,
    val dateCreated:String,
    val state: StateTask,
)

