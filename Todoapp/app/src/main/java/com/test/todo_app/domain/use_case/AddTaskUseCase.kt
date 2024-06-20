package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    val repository: TaskRepository
) {
    fun addNewTask(name: String, description: String): Task {
        val task = createTask(name, description)
        repository.create(task)
        return task
    }

    private fun createTask(name: String, description: String): Task {
        val time = getDateAndTime()
        val task = Task(
            id = 0,
            shortDescription = name,
            fullDescription = description,
            dateCreated = time,
            state = StateTask.newTask
        )
        return task
    }

    private fun getDateAndTime(): String {
        val date = Calendar.getInstance().time
        val targetFormat = SimpleDateFormat("dd.MM.yyyy HH:mm",
            Locale.getDefault())
        val formattedDate: String = targetFormat.format(date)
        return formattedDate
    }
}