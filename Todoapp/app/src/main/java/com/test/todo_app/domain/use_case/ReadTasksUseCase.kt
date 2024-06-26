package com.test.todo_app.domain.use_case

import com.test.todo_app.domain.interfaces.repository.TaskRepository
import com.test.todo_app.domain.interfaces.view.ShowListUpdate
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.model.convertors.toTask
import com.test.todo_app.domain.model.convertors.toTaskView
import com.test.todo_app.view.model.TaskView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReadTasksUseCase @Inject constructor(
    val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<TaskView>> {
        val flow = getTasksFromDB()
        return convertTasksFlowToUIModel(flow)
    }

    private fun getTasksFromDB(): Flow<List<Task>> {
        return repository.read().map { list -> list.map { task -> task.toTask() } }
    }

    private fun convertTasksFlowToUIModel(taskList: Flow<List<Task>>): Flow<List<TaskView>> {
        return taskList.map { list -> list.map { task -> task.toTaskView() } }
    }

}