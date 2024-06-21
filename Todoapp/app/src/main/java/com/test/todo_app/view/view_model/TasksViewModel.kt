package com.test.todo_app.view.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.todo_app.domain.interfaces.view.ShowListUpdate
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.use_case.CRUDUseCases
import com.test.todo_app.domain.use_case.ListTaskManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    val useCase: CRUDUseCases, val listM: ListTaskManager
) : ViewModel() {

    // list screen
    private var showListUpdate: ShowListUpdate? = null

    // detail screen
    var currentTask: MutableLiveData<Task> = MutableLiveData()
    var name: MutableLiveData<String> = MutableLiveData()
    var description: MutableLiveData<String> = MutableLiveData()

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
            is ListTaskAction.AddNewTask -> addNewTask(action.name, action.description)
            is ListTaskAction.DeleteTask -> deleteTask(action.task)
            is ListTaskAction.MoveProgressTask -> updateProgress(action.task, action.nextProgress)
            is ListTaskAction.UpdateText -> updateText(
                action.task,
                action.newName,
                action.newDescription
            )
        }
    }

    private fun loadTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val l =  useCase.readTasksUseCase.read()
            listM.showListTask(l)
            CoroutineScope(Dispatchers.Main).launch {
                showListUpdate?.showRV(l.size)
            }
        }
    }

    private fun addNewTask(name: String, description: String) {
        val task = useCase.addTaskUseCase.addNewTask(name, description)
        val range = listM.setAddedTask(task)
        showListUpdate?.updateRV(range)
        showListUpdate?.addRV(range.first)
    }

    private fun deleteTask(task: Task) {
        useCase.deleteTaskUseCase.deleteTask(task)
        val index = listM.setDeletedTask(task)
        if (index>=0){
            showListUpdate?.deleteRV(index)
        }
    }

    private fun updateProgress(task: Task, nextProgress: StateTask) {
        val updTask = useCase.updateTaskUseCase.updateProgress(task, nextProgress)
        currentTask.value = updTask
        val range = listM.setUpdatedTask(updTask)
        showListUpdate?.updateRV(range)
    }

    private fun updateText(task: Task, newName: String, newDescription: String) {
        val updTask = useCase.updateTaskUseCase.updateText(task, newName, newDescription)
        currentTask.value = updTask
        val range = listM.setUpdatedTask(updTask)
        showListUpdate?.updateRV(range)
    }

    fun updateCurrentTask() {
        val updatedTask = useCase.updateTaskUseCase.updateTask(currentTask.value!!,  name.value, description.value, null)
        currentTask.value = updatedTask
    }

    fun getListSize(): Int {
        return listM.getListSize()
    }

    fun getTask(position: Int): Task {
        return listM.getTask(position)
    }

}



