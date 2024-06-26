package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.model.Task
import com.test.todo_app.view.model.TaskView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListTaskManager @Inject constructor() {
    var listTask = listOf<TaskView>()

    fun setUpdatedTask(updTask: TaskView): List<TaskView>? {
        val listTaskCopy = listTask.toMutableList()
        val oldIndex = findInd(updTask.id) ?: return null
        listTaskCopy[oldIndex] = updTask
        val sortedList = listTaskCopy.sortedBy { it.state }
        listTask = sortedList
        return sortedList
    }

    fun setAddedTask(task: TaskView): List<TaskView> {
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.add(task)
        val sortedListTask = listTaskCopy.sortedBy { it.state }
        listTask = sortedListTask
        return sortedListTask
    }

    fun setDeletedTask(task: TaskView): List<TaskView> {
        val listTaskCopy = listTask.toMutableList()
        listTaskCopy.remove(task)
        listTask = listTaskCopy
        return listTaskCopy
    }

    fun setNewList(list:List<TaskView>){
        listTask = list
    }

    private fun findInd(id: Int): Int? {
        for((index, task) in listTask.withIndex()){
            if (task.id==id){
                return index
            }
        }
        return null
    }
}