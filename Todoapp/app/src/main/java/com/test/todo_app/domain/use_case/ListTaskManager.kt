package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.model.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListTaskManager @Inject constructor() {
    var listTask = listOf<Task>()

    fun setUpdatedTask(updTask: Task): List<Task>? {
        val listTaskCopy = listTask.toMutableList()
        val oldIndex = findInd(listTask, updTask.id) ?: return null
        listTaskCopy[oldIndex] = updTask
        val sortedList = listTaskCopy.sortedBy { it.state }
        listTask = sortedList
        return sortedList
    }

    fun setAddedTask(task: Task): List<Task> {
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.add(task)
        val sortedListTask = listTaskCopy.sortedBy { it.state }
        listTask = sortedListTask
        return sortedListTask
    }

    fun setDeletedTask(task: Task): List<Task> {
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.remove(task)
        listTask = listTaskCopy
        return listTaskCopy
    }

    private fun findInd(list:List<Task>, id: Int): Int? {
        for((index, task) in listTask.withIndex()){
            if (task.id==id){
                return index
            }
        }
        return null
    }

    fun showListTask(list: List<Task>) {
        listTask = list.sortedBy { it.state }
    }

    fun getListSize(): Int {
        return listTask.size
    }

    fun getTask(position: Int): Task {
        return listTask[position]
    }
}