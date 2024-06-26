package com.test.todo_app.domain.use_case

import androidx.fragment.app.DialogFragment
import com.test.todo_app.R
import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.interfaces.view.TaskAddResponse
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.model.convertors.toTask
import com.test.todo_app.domain.model.convertors.toTaskView
import com.test.todo_app.view.model.TaskView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    val repository: TaskRepository, val listM: ListTaskManager
) {

    operator fun invoke(name: String, description: String,):List<TaskView>?{
        return if (validateTask(name)){
            addNewTask(name, description)
        }else null
    }


    private fun addNewTask(name: String, description: String, ):List<TaskView>{
        val task = addNewTaskInRepository(name, description)
        val newList = listM.setAddedTask(task)
        return newList
    }

    private fun validateTask(name: String):Boolean {
        return (name.trim() != "")
    }

    private fun addNewTaskInRepository(name: String, description: String): TaskView {
        val task = createNewTask(name, description)
        repository.create(task.toTask())
        return task
    }

    private fun createNewTask(name: String, description: String): TaskView {
        val time = getDateAndTime()
        val task = Task(
            id = generateId(),
            shortDescription = name,
            fullDescription = description,
            dateCreated = time,
            state = StateTask.newTask
        )
        return task.toTaskView()
    }

    private fun generateId():Int{
        return Random().nextInt()
    }

    private fun getDateAndTime(): String {
        val date = Calendar.getInstance().time
        val targetFormat = SimpleDateFormat("dd.MM.yyyy HH:mm",
            Locale.getDefault())
        val formattedDate: String = targetFormat.format(date)
        return formattedDate
    }
}