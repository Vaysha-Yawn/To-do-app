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
import com.test.todo_app.view.tools.validate
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
        viewModel.makeAction(ListTaskAction.LoadPage)
    }

    override fun toDone(dialog: DialogFragment, task:Task) {
        viewModel.makeAction(
            ListTaskAction.MoveProgressTask(
                task,
                StateTask.done
            )
        )
        dialog.dismiss()
    }

    override fun toInProgress(dialog: DialogFragment, task:Task) {
        viewModel.makeAction(
            ListTaskAction.MoveProgressTask(
                task,
                StateTask.inProgress
            )
        )
        dialog.dismiss()
    }

    override fun toDelete(dialog: DialogFragment, task:Task) {
        viewModel.makeAction(ListTaskAction.DeleteTask(task))
        dialog.dismiss()
        navController?.navigateUp()
    }

    override fun createTask(dialog: DialogFragment, name: String, description: String) {
        if (validate(name)) {
            viewModel.makeAction(
                ListTaskAction.AddNewTask(
                    name,
                    description
                )
            )
            dialog.dismiss()
            viewModel.clearTaskData()
        } else {
            showError(getString(R.string.error_no_name))
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}