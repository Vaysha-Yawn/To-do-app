package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.model.Task
import javax.inject.Inject


class ListTaskManager @Inject constructor() {
    private var listTask = listOf<Task>()

    fun setUpdatedTask(updTask: Task): IntRange {
        val listTaskCopy = listTask.toMutableList()
        val oldIndex = findInd(listTask, updTask.id) ?: return 0..0
        listTaskCopy[oldIndex] = updTask
        val sortedList = listTaskCopy.sortedBy { it.state }
        val newIndex = findInd(sortedList, updTask.id) ?: return 0..0
        listTask = sortedList
        return oldIndex..newIndex
    }

    fun setAddedTask(task: Task): IntRange {
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.add(task)
        val sortedListTask = listTaskCopy.sortedBy { it.state }
        val end = listTaskCopy.indexOf(task)
        val start = sortedListTask.indexOf(task)
        listTask = sortedListTask
        return start..end
    }

    fun setDeletedTask(task: Task): Int {
        val index = findInd(listTask, task.id)?:-1
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.remove(task)
        listTask = listTaskCopy
        return index
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