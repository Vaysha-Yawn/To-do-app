package com.test.todo_app.view.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.todo_app.databinding.DialogAddTaskBottomSheetBinding
import com.test.todo_app.domain.interfaces.view.TaskAddResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDialogBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAddTaskBottomSheetBinding
    private var taskAddResponse: TaskAddResponse? = null
    private var name: String = ""
    private var description: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddTaskBottomSheetBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            taskAddResponse = (context as TaskAddResponse)
        } catch (e: Exception) {
            dialog?.dismiss()
        }
    }


    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(argName, name)
        bundle.putString(argDescription, description)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        name = savedInstanceState?.getString(argName) ?: ""
        description = savedInstanceState?.getString(argDescription) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingBottomSheet()
        if (taskAddResponse == null){
            dialog?.dismiss()
            return
        }
        setStartedValue()
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            addTask.setOnClickListener {
                taskAddResponse?.createTask(this@AddDialogBottomSheet, name, description)
            }
            editName.doOnTextChanged { text, start, before, count ->
                name = text.toString()
            }
            editDescription.doOnTextChanged { text, start, before, count ->
                description = text.toString()
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
        binding.editName.setText(
            name.toCharArray(),
            0,
            name.length
        )
        binding.editDescription.setText(
            description.toCharArray(),
            0,
            description.length
        )
    }

    companion object {
        val argName = "name"
        val argDescription = "description"
        fun getInstance(): AddDialogBottomSheet {
            val dialog = AddDialogBottomSheet()
            return dialog
        }

        const val TAG = "ModalBottomSheet"
    }
}

