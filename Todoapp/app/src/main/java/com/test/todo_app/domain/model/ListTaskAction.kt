package com.test.todo_app.domain.model

import androidx.fragment.app.DialogFragment
import com.test.todo_app.domain.interfaces.view.TaskAddResponse

sealed class ListTaskAction{
    data object LoadPage: ListTaskAction()
    class AddNewTask(val name:String, val description:String, val dialog: DialogFragment, val add: TaskAddResponse): ListTaskAction()
    class MoveProgressTask(val task: Task, val nextProgress: StateTask): ListTaskAction()
    class DeleteTask(val task: Task): ListTaskAction()
    class UpdateText(val task: Task, val newName:String, val newDescription:String): ListTaskAction()
}