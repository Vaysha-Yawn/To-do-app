package com.test.todo_app.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.test.todo_app.R
import com.test.todo_app.databinding.DialogMenuTaskBinding
import com.test.todo_app.domain.model.ActionTask
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogMenuTask(
    private val listActionTask: List<ActionTask>,
    private val viewModel: TasksViewModel
) : DialogFragment() {

    private lateinit var binding: DialogMenuTaskBinding

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogMenuTaskBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.currentTask.value == null) {
            closeDialog()
        }
        showActions()
        setListeners()
    }

    private fun setListeners() {
        if (viewModel.currentTask.value == null) {
            return
        }
        binding.buttonInProgress.setOnClickListener {
            viewModel.makeAction(
                ListTaskAction.MoveProgressTask(
                    viewModel.currentTask.value!!,
                    StateTask.inProgress
                )
            )
            closeDialog()
        }
        binding.buttonDone.setOnClickListener {
            viewModel.makeAction(
                ListTaskAction.MoveProgressTask(
                    viewModel.currentTask.value!!,
                    StateTask.done
                )
            )
            closeDialog()
        }
        binding.buttonDelete.setOnClickListener {
            viewModel.makeAction(ListTaskAction.DeleteTask(viewModel.currentTask.value!!))
            closeDialog()
            findNavController().navigateUp()
        }
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun showActions() {
        show(binding.buttonInProgress, listActionTask.contains(ActionTask.MoveToInProgress))
        show(binding.buttonDone, listActionTask.contains(ActionTask.MakeDone))
        show(binding.buttonDelete, listActionTask.contains(ActionTask.Delete))
    }

    private fun show(view: View, showBoolean: Boolean) {
        if (showBoolean) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}

fun whatWeCanDoWithTask(currentStateTask: StateTask): List<ActionTask> {
    return when (currentStateTask) {
        StateTask.newTask -> listOf(ActionTask.Delete, ActionTask.MoveToInProgress)
        StateTask.inProgress -> listOf(ActionTask.MakeDone)
        StateTask.done -> listOf()
    }
}