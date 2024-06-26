package com.test.todo_app.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.test.todo_app.R
import com.test.todo_app.domain.interfaces.view.TaskAddResponse
import com.test.todo_app.domain.interfaces.view.TaskMenuResponse
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import com.test.todo_app.view.model.TaskView
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskMenuResponse, TaskAddResponse {

    lateinit var viewModel:TasksViewModel
     private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
        navController = navHostFragment?.navController
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
    }

    override fun toDone(dialog: DialogFragment, task:TaskView) {
        viewModel.makeAction(
            ListTaskAction.MoveProgressTask(
                task,
                StateTask.done
            )
        )
        dialog.dismiss()
    }

    override fun toInProgress(dialog: DialogFragment, task:TaskView) {
        viewModel.makeAction(
            ListTaskAction.MoveProgressTask(
                task,
                StateTask.inProgress
            )
        )
        dialog.dismiss()
    }

    override fun toDelete(dialog: DialogFragment, task:TaskView) {
        viewModel.makeAction(ListTaskAction.DeleteTask(task))
        dialog.dismiss()
        navController?.navigateUp()
    }

    override fun createTask(dialog: DialogFragment, name: String, description: String) {
        viewModel.makeAction(ListTaskAction.AddNewTask(name, description, dialog, this))
    }

    override fun showSuccess(dialog: DialogFragment) {
        dialog.dismiss()
    }

    override fun showError(dialog: DialogFragment, message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}