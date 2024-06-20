package com.test.todo_app.domain.model

sealed class ListTaskAction{
    data object LoadPage: ListTaskAction()
    class AddNewTask(val name:String, val description:String): ListTaskAction()
    class MoveProgressTask(val task: Task, val nextProgress: StateTask): ListTaskAction()
    class DeleteTask(val task: Task): ListTaskAction()
    class UpdateText(val task: Task, val newName:String, val newDescription:String): ListTaskAction()
}