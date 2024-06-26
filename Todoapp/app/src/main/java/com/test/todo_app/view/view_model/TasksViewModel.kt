package com.test.todo_app.view.view_model

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.todo_app.R
import com.test.todo_app.domain.interfaces.view.ShowListUpdate
import com.test.todo_app.domain.interfaces.view.TaskAddResponse
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.use_case.CRUDUseCases
import com.test.todo_app.domain.use_case.ListTaskManager
import com.test.todo_app.view.model.TaskView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    private val useCase: CRUDUseCases, val listM: ListTaskManager
) : ViewModel() {

    // list screen
    private var showListUpdate: ShowListUpdate? = null

    // detail screen
    var currentTask: MutableLiveData<TaskView> = MutableLiveData()
    var name: MutableLiveData<String> = MutableLiveData()
    var description: MutableLiveData<String> = MutableLiveData()

    private var jobLoadTasks:Job? = null

    init {
        loadTasks()
    }

    fun attachShowListUpdate(s: ShowListUpdate) {
        showListUpdate = s
    }

    fun clearTaskData() {
        currentTask = MutableLiveData()
        name.value = ""
        description.value = ""
    }

    fun makeAction(action: ListTaskAction) {
        when (action) {
            ListTaskAction.LoadPage -> loadTasks()
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
        val list = useCase.addTaskUseCase.invoke(name, description)
        if (list != null) {
            add.showSuccess(dialog)
            clearTaskData()
            showListUpdate?.updateRV(list)
        } else {
            add.showError(dialog, dialog.context?.getString(R.string.error_no_name) ?: "")
        }
    }

    private fun loadTasks() {
        jobLoadTasks?.cancel()
        jobLoadTasks = useCase.readTasksUseCase()
            .onEach { listTask ->
            listM.setNewList(listTask)
            showListUpdate?.updateRV(listTask) }
            .launchIn(viewModelScope)
    }

    private fun deleteTask(task: TaskView) {
        useCase.deleteTaskUseCase(task)
        val list = listM.setDeletedTask(task)
        showListUpdate?.updateRV(list)
    }

    private fun updateProgress(task: TaskView, nextProgress: StateTask) {
        val updTask = useCase.updateTaskUseCase.updateProgress(task, nextProgress)
        currentTask.value = updTask
        val list = listM.setUpdatedTask(updTask)
        if (list != null) {
            showListUpdate?.updateRV(list)
        }
    }

    private fun updateText(task: TaskView, newName: String, newDescription: String) {
        val updTask = useCase.updateTaskUseCase.updateText(task, newName, newDescription)
        currentTask.value = updTask
        val list = listM.setUpdatedTask(updTask)
        if (list != null) {
            showListUpdate?.updateRV(list)
        }
    }
}