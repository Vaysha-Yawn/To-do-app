package com.test.todo_app.repository.abstracts


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.todo_app.repository.tools.StateTaskTypeConverter
import com.test.todo_app.repository.entity.TaskRoom

@Database(entities = [TaskRoom::class], version = 1, exportSchema = false)
@TypeConverters(StateTaskTypeConverter::class)
abstract class AppDatabaseParam : RoomDatabase() {
    abstract fun dao(): DAOTask
}
