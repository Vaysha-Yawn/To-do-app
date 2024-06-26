package com.test.todo_app.app

import android.app.Application
import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.repository.repository.RoomRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}