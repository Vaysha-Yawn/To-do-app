package com.test.todo_app.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Task (
    val id:Int,
    var shortDescription:String,
    var fullDescription: String,
    val dateCreated:String,
    val state: StateTask,
):Serializable

