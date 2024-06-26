package com.test.todo_app.domain.model

import androidx.fragment.app.DialogFragment
import com.test.todo_app.domain.interfaces.view.TaskAddResponse
import com.test.todo_app.view.model.TaskView

sealed class ListTaskAction{
    data object LoadPage: ListTaskAction()
    class AddNewTask(val name:String, val description:String, val dialog: DialogFragment, val add: TaskAddResponse): ListTaskAction()
    class MoveProgressTask(val task: TaskView, val nextProgress: StateTask): ListTaskAction()
    class DeleteTask(val task: TaskView): ListTaskAction()
    class UpdateText(val task: TaskView, val newName:String, val newDescription:String): ListTaskAction()
}