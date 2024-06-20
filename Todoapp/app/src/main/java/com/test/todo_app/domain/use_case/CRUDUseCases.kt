package com.test.todo_app.domain.use_case

import javax.inject.Inject

class CRUDUseCases @Inject constructor(
    val updateTaskUseCase: UpdateTaskUseCase,
    val addTaskUseCase: AddTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val readTasksUseCase: ReadTasksUseCase
)