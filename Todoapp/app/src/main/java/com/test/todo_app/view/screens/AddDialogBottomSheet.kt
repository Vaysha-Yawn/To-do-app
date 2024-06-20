package com.test.todo_app.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.todo_app.R
import com.test.todo_app.databinding.DialogAddTaskBottomSheetBinding
import com.test.todo_app.domain.interfaces.view.ShowError
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.view.tools.validate
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDialogBottomSheet(private val viewModel: TasksViewModel) : BottomSheetDialogFragment(),
    ShowError {

    private lateinit var binding: DialogAddTaskBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddTaskBottomSheetBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBottomSheet()
        setStartedValue()
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            addTask.setOnClickListener {
                addTask()
            }
            editName.doOnTextChanged { text, start, before, count ->
                viewModel.name.postValue(text.toString())
            }
            editDescription.doOnTextChanged { text, start, before, count ->
                viewModel.description.postValue(text.toString())
            }
        }
    }

    private fun settingBottomSheet() {
        val bottomSheet = binding.bottomSheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val density = bottomSheet.context.resources.displayMetrics.density
        behavior.peekHeight = (300 * density).toInt()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.editName.requestFocus()
        val imm = getSystemService(binding.root.context, InputMethodManager::class.java)
        imm!!.showSoftInput(binding.editName, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setStartedValue() {
        if (viewModel.name.value != null && viewModel.name.value != "") {
            binding.editName.setText(
                viewModel.name.value!!.toCharArray(),
                0,
                viewModel.name.value!!.length
            )
        }
        if (viewModel.description.value != null && viewModel.description.value != "") {
            binding.editDescription.setText(
                viewModel.description.value!!.toCharArray(),
                0,
                viewModel.description.value!!.length
            )
        }
    }

    private fun addTask() {
        if (validate(viewModel.name.value ?: "")) {
            viewModel.makeAction(
                ListTaskAction.AddNewTask(
                    viewModel.name.value ?: "",
                    viewModel.description.value ?: ""
                )
            )
            dialog?.dismiss()
            viewModel.clearTaskData()
        } else {
            showError(getString(R.string.error_no_name))
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

