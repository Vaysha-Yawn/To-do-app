package com.test.todo_app.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.repository.model.converter.StateTaskTypeConverter


@Entity
data class TaskRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var shortDescription:String,
    var fullDescription: String,
    val dateCreated:String,
    @TypeConverters(StateTaskTypeConverter::class)
    val state: StateTask,
)

