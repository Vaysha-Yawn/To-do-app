package com.test.todo_app.view.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.test.todo_app.R
import com.test.todo_app.databinding.DialogMenuTaskBinding
import com.test.todo_app.domain.interfaces.view.TaskMenuResponse
import com.test.todo_app.domain.model.ActionTask
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogMenuTask() : DialogFragment() {

    private lateinit var binding: DialogMenuTaskBinding
    private var taskMenuResponse: TaskMenuResponse? = null
    private var showInProgress: Boolean = false
    private var showDone: Boolean = false
    private var showDelete: Boolean = false
    private var task: Task? = null

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            taskMenuResponse = (context as TaskMenuResponse)
        } catch (e: Exception) {
            closeDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgsFromBundle()
        showActions()
        setListeners()
        if (taskMenuResponse == null) {
            closeDialog()
            return
        }
    }

    private fun setListeners() {
        binding.buttonInProgress.setOnClickListener {
            if (task!=null){
                taskMenuResponse?.toInProgress(this, task!!)
            }
        }
        binding.buttonDone.setOnClickListener {
            if (task!=null){
                taskMenuResponse?.toDone(this, task!!)
            }
        }
        binding.buttonDelete.setOnClickListener {
            if (task!=null){
                taskMenuResponse?.toDelete(this, task!!)
            }
        }
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun showActions() {
        show(binding.buttonInProgress, showInProgress)
        show(binding.buttonDone, showDone)
        show(binding.buttonDelete, showDelete)
    }

    private fun show(view: View, showBoolean: Boolean) {
        if (showBoolean) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    private fun getArgsFromBundle() {
        showInProgress = arguments?.getBoolean(argInProgress) ?: false
        showDone = arguments?.getBoolean(argDone) ?: false
        showDelete = arguments?.getBoolean(argDelete) ?: false
        task = arguments?.getSerializable(argTask) as Task?
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
        val argInProgress = "progress"
        val argDone = "done"
        val argDelete = "delete"
        val argTask = "task"
        fun getInstance(task: Task, list: List<ActionTask>): DialogMenuTask {
            val bundle = Bundle()
            bundle.putBoolean(argInProgress, list.contains(ActionTask.MoveToInProgress))
            bundle.putBoolean(argDone, list.contains(ActionTask.MakeDone))
            bundle.putBoolean(argDelete, list.contains(ActionTask.Delete))
            bundle.putSerializable(argTask, task)
            val dialog = DialogMenuTask()
            dialog.arguments = bundle
            return dialog
        }
    }
}

fun whatWeCanDoWithTask(currentStateTask: StateTask): List<ActionTask> {
    return when (currentStateTask) {
        StateTask.newTask -> listOf(ActionTask.Delete, ActionTask.MoveToInProgress)
        StateTask.inProgress -> listOf(ActionTask.MakeDone)
        StateTask.done -> listOf()
    }
}