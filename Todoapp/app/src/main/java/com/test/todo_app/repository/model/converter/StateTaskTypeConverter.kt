package com.test.todo_app.repository.model.converter

import androidx.room.TypeConverter
import com.test.todo_app.domain.model.StateTask

class StateTaskTypeConverter {
    @TypeConverter
    fun toState(value: String) = enumValueOf<StateTask>(value)
    @TypeConverter
    fun fromState(value: StateTask) = value.name
}