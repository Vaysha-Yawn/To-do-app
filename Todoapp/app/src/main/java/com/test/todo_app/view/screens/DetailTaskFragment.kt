package com.test.todo_app.view.screens

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.test.todo_app.databinding.FragmentDetailTasksBinding
import com.test.todo_app.domain.interfaces.view.ShowTaskMenuDialog
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.getResProgressColor
import com.test.todo_app.domain.model.getResProgressString
import com.test.todo_app.view.model.TaskView
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTaskFragment : Fragment(), ShowTaskMenuDialog {

    private lateinit var binding: FragmentDetailTasksBinding
    private val args: DetailTaskFragmentArgs by navArgs()
    private lateinit var viewModel: TasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTasksBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TasksViewModel::class.java]
        setCurrentTask()
        val currentTask = viewModel.currentTask

        currentTask.observe(viewLifecycleOwner) {
            with(binding) {
                setProgress(it.state)
                date.text = it.dateCreated
                if (it.state == StateTask.done) {
                    menu.visibility = View.GONE
                }
            }
        }
        setStringToEdit(binding.name, viewModel.name.value)
        setStringToEdit(binding.description, viewModel.description.value)
        with(binding) {
            setListenerToEdit(name, viewModel.name)
            setListenerToEdit(description, viewModel.description)
            menu.setOnClickListener {
                showTaskMenuDialog(currentTask.value!!)
            }
            back.setOnClickListener {
                navigateBack()
            }
        }
    }

    private fun setStringToEdit(view: TextInputEditText, text: String?) {
        if (text != null && text != "") {
            view.setText(text.toCharArray(), 0, text.length)
        }
    }

    private fun setListenerToEdit(view: TextInputEditText, param: MutableLiveData<String>) {
        view.doOnTextChanged { text, _, _, _ ->
            param.postValue(text.toString())
        }
    }

    private fun getProgressString(progress: StateTask): String {
        val res = progress.getResProgressString()
        return ContextCompat.getString(requireContext(), res)
    }

    private fun getProgressColorStateList(progress: StateTask): ColorStateList {
        val res = progress.getResProgressColor()
        return ColorStateList.valueOf(ResourcesCompat.getColor(resources, res, null))
    }

    private fun setProgress(state: StateTask) {
        val textProgress = getProgressString(state)
        val colorStateListProgress = getProgressColorStateList(state)
        binding.progress.text = textProgress
        binding.progress.backgroundTintList = colorStateListProgress
    }

    private fun setCurrentTask() {
        if (viewModel.currentTask.value == null) {
            viewModel.currentTask.value = args.task
            viewModel.name.value = args.task.shortDescription
            viewModel.description.value = args.task.fullDescription
        }
    }

    private fun navigateBack() {
        update()
        viewModel.clearTaskData()
        findNavController().navigateUp()
    }

    private fun update() {
        if (viewModel.currentTask.value != null || viewModel.name.value != null || viewModel.description.value != null) {
            viewModel.makeAction(
                ListTaskAction.UpdateText(
                    viewModel.currentTask.value!!,
                    viewModel.name.value!!,
                    viewModel.description.value!!
                )
            )
        }
    }

    override fun showTaskMenuDialog(task: TaskView) {
        update()
        val types = whatWeCanDoWithTask(task.state)
        val dialog = DialogMenuTask.getInstance(task, types)
        dialog.show(childFragmentManager, DialogMenuTask.TAG)
    }

}

