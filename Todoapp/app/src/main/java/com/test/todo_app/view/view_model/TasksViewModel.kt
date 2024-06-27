package com.test.todo_app.view.view_model

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.test.todo_app.R
import com.test.todo_app.domain.interfaces.view.TaskAddResponse
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.use_case.CRUDUseCases
import com.test.todo_app.view.model.TaskView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    private val useCase: CRUDUseCases
) : ViewModel() {

    //list screen
    val listTasks = useCase.readTasksUseCase().asLiveData()

    // detail screen
    var currentTask: MutableLiveData<TaskView> = MutableLiveData()
    var name: MutableLiveData<String> = MutableLiveData()
    var description: MutableLiveData<String> = MutableLiveData()

    fun clearTaskData() {
        currentTask = MutableLiveData()
        name.value = ""
        description.value = ""
    }

    fun makeAction(action: ListTaskAction) {
        when (action) {
            is ListTaskAction.AddNewTask -> createTask(
                action.dialog,
                action.name,
                action.description,
                action.add
            )

            is ListTaskAction.DeleteTask -> deleteTask(action.task)
            is ListTaskAction.MoveProgressTask -> updateProgress(action.task, action.nextProgress)
            is ListTaskAction.UpdateText -> updateText(
                action.task,
                action.newName,
                action.newDescription
            )

        }
    }

    private fun createTask(
        dialog: DialogFragment,
        name: String,
        description: String,
        add: TaskAddResponse
    ) {
        val resultAdd = useCase.addTaskUseCase(name, description)
        if (resultAdd) {
            add.showSuccess(dialog)
            clearTaskData()
        } else {
            add.showError(dialog, dialog.context?.getString(R.string.error_no_name) ?: "")
        }
    }

    private fun deleteTask(task: TaskView) {
        useCase.deleteTaskUseCase(task)
    }

    private fun updateProgress(task: TaskView, nextProgress: StateTask) {
        val updTask = useCase.updateTaskUseCase.updateProgress(task, nextProgress)
        currentTask.value = updTask
    }

    private fun updateText(task: TaskView, newName: String, newDescription: String) {
        val updTask = useCase.updateTaskUseCase.updateText(task, newName, newDescription)
        currentTask.value = updTask
    }
}