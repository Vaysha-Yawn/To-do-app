package com.test.todo_app.view.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.todo_app.domain.interfaces.view.ShowListUpdate
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.use_case.CRUDUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    val useCase: CRUDUseCases
) : ViewModel() {

    // list
    var listTask = listOf<MutableLiveData<Task>>()
    private var showListUpdate: ShowListUpdate? = null

    // detail, add, menu
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
            showListTask(l)
        }
    }
    private fun addNewTask(name: String, description: String) {
        val task = useCase.addTaskUseCase.addNewTask(name, description)
        setAddedTask(task)
    }
    private fun deleteTask(task: Task) {
        useCase.deleteTaskUseCase.deleteTask(task)
        setDeletedTask(task)
    }

    private fun updateProgress(task: Task, nextProgress: StateTask) {
        val updTask = useCase.updateTaskUseCase.updateProgress(task, nextProgress)
        setUpdatedTask(updTask)
    }

    private fun updateText(task: Task, newName: String, newDescription: String) {
        useCase.updateTaskUseCase.updateText(task, newName, newDescription)
    }

    private fun setUpdatedTask(updTask: Task) {
        currentTask.value = updTask
        var listTaskCopy = listTask.toList()
        val targetTask = findEl(updTask.id)
        targetTask?.value = updTask
        listTaskCopy = listTaskCopy.sortedBy { it.value?.state }
        val oldIndex = listTask.indexOf(targetTask)
        val newIndex = listTaskCopy.indexOf(targetTask)
        listTask = listTaskCopy
        showListUpdate?.updateRV(oldIndex..newIndex)
    }

    private fun setAddedTask(task: Task) {
        val listTaskCopy = listTask.toMutableList()
        val taskM = MutableLiveData(task)
        listTaskCopy.add(taskM)
        val sortedListTask = listTaskCopy.sortedBy { it.value?.state }
        val end = listTaskCopy.indexOf(taskM)
        val start = sortedListTask.indexOf(taskM)
        listTask = sortedListTask
        showListUpdate?.updateRV(start..end)
        showListUpdate?.addRV(start)
    }

    private fun setDeletedTask(task: Task) {
        val targetTask =  findEl(task.id)
        targetTask.let {
            val index = listTask.indexOf(targetTask)
            showListUpdate?.deleteRV(index)
        }
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.remove(targetTask)
        listTask = listTaskCopy
    }

    private fun findEl(id:Int):MutableLiveData<Task>?{
        return listTask.find { it -> id == it.value!!.id }
    }

    private fun showListTask(list: List<Task>) {
        listTask = list.sortedBy { it.state }
            .map { MutableLiveData(it) }
        CoroutineScope(Dispatchers.Main).launch {
            showListUpdate?.showRV()
        }
    }

    fun updateCurrentTask() {
        val updatedTask = useCase.updateTaskUseCase.updateTask(currentTask.value!!,  name.value, description.value, null)
        currentTask.value = updatedTask
    }

}



