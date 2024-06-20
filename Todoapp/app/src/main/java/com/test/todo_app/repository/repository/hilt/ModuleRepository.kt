package com.test.todo_app.repository.repository.hilt

import android.app.Application
import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.repository.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ModuleRepository {
    @Provides
    @Singleton
    fun provideRepository(app:Application): TaskRepository {
        return RoomRepository(app.applicationContext)
    }
}