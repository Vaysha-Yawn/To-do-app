package com.test.todo_app.view.screens

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.ParcelableSpan
import android.text.Spannable
import android.text.style.BulletSpan
import android.text.style.EasyEditSpan
import android.text.style.ImageSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toIcon
import androidx.core.text.toSpannable
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.test.todo_app.R
import com.test.todo_app.databinding.FragmentDetailTasksBinding
import com.test.todo_app.domain.interfaces.view.ShowTaskMenuDialog
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.getResProgressColor
import com.test.todo_app.domain.model.getResProgressString
import com.test.todo_app.view.model.TaskView
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.TypeVariable

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
            italic.setOnClickListener {
                setSpannableEffect(StyleSpan(Typeface.ITALIC), StyleSpan::class.java)
            }
            bold.setOnClickListener {
                setSpannableEffect(StyleSpan(Typeface.BOLD), StyleSpan::class.java)
            }
            underline.setOnClickListener {
                setSpannableEffect(UnderlineSpan(), UnderlineSpan::class.java)
            }
            bullet.setOnClickListener {
                setSpannableEffectBullet()
            }
            strikethrough.setOnClickListener {
                setSpannableEffect(StrikethroughSpan(), StrikethroughSpan::class.java)
            }
            checkbox.setOnClickListener {
                setSpannableEffectCheckBox()
            }
        }
    }

    private fun setSpannableEffectCheckBox() {
        val spannable = binding.description.text
        val end = binding.description.selectionEnd
        val indexStartString = spannable?.substring(0..<end)?.lastIndexOf('\n')?.plus(1)
        val arr = spannable?.getSpans(indexStartString ?: 0, end, ImageSpan::class.java)
        val arr2 = spannable?.getSpans(indexStartString ?: 0, end, BulletSpan::class.java)
        if (arr2?.isEmpty() == false){
            setSpannableEffectBullet()
            return
        }
        when (arr?.isEmpty()) {
            true -> {
                val d = ContextCompat.getDrawable(requireContext(), R.drawable.unchecked)!!
                d.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                val b = d.toBitmap(binding.description.textSize.toInt()/2, binding.description.textSize.toInt()/2)
                spannable.setSpan(
                    ImageSpan(requireContext(), b),
                    indexStartString ?: 0,
                    indexStartString?.plus(1) ?: end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.insert(indexStartString?.plus(1) ?: 1," ",)
                binding.description.text = spannable
                binding.description.setSelection(end)
            }
            false -> {
                binding.description.text?.removeSpan(arr[0])
            }
            null -> return
        }
    }

    private fun setSpannableEffectBullet() {
        val spannable = binding.description.text
        val end = binding.description.selectionEnd
        val indexStartString = spannable?.substring(0..<end)?.lastIndexOf('\n')?.plus(1)
        val arr = spannable?.getSpans(indexStartString ?: 0, end, BulletSpan::class.java)
        val arr2 = spannable?.getSpans(indexStartString ?: 0, end, ImageSpan::class.java)
        if (arr2?.isEmpty() == false){
            setSpannableEffectCheckBox()
            return
        }
        when (arr?.isEmpty()) {
            true -> {
                spannable.setSpan(
                    BulletSpan(30, Color.BLACK, 10),
                    indexStartString ?: 0,
                    indexStartString?.plus(1) ?: end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.description.text = spannable
                binding.description.setSelection(end)
            }
            false -> {
                binding.description.text?.removeSpan(arr[0])
            }
            null -> return
        }
    }

    private fun <T>setSpannableEffect(style: ParcelableSpan,clazz:Class<T>) {
        val spannable = binding.description.text?.toSpannable()
        val end = binding.description.selectionEnd
        val arr = spannable?.getSpans(binding.description.selectionStart, binding.description.selectionEnd, clazz)
        when (arr?.isEmpty()) {
            true -> {
                spannable.setSpan(
                    style,
                    binding.description.selectionStart,
                    binding.description.selectionEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.description.setText(spannable)
                binding.description.setSelection(end)
            }
            false -> {
                binding.description.text?.removeSpan(arr[0])
            }
            null -> return
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
        if (viewModel.currentTask.value != null && viewModel.name.value != null && viewModel.description.value != null) {
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

