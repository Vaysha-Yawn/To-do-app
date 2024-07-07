package com.test.todo_app.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.todo_app.databinding.FragmentListTasksBinding
import com.test.todo_app.domain.interfaces.view.NavigateToFullTask
import com.test.todo_app.domain.interfaces.view.ShowTaskMenuDialog
import com.test.todo_app.view.list_adapter.ListTaskAdapter
import com.test.todo_app.view.model.TaskView
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ListTasksFragment : Fragment(), ShowTaskMenuDialog, NavigateToFullTask {

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: FragmentListTasksBinding
    private lateinit var listTaskAdapter: ListTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListTasksBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TasksViewModel::class.java]
        listTaskAdapter = ListTaskAdapter(viewModel.listTasks.value?: listOf(), this, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = listTaskAdapter
        viewModel.listTasks.observe(viewLifecycleOwner) { newList ->
            listTaskAdapter.setData(newList)
        }
        binding.addTask.setOnClickListener {
            showAddTask()
        }
    }

    override fun showTaskMenuDialog(task: TaskView) {
        viewModel.currentTask.value = task
        val types = whatWeCanDoWithTask(task.state)
        val dialog = DialogMenuTask.getInstance(task, types)
        dialog.show(childFragmentManager, DialogMenuTask.TAG)
    }

    private fun showAddTask() {
        val modalAddDialogBottomSheet = AddDialogBottomSheet.getInstance()
        modalAddDialogBottomSheet.show(childFragmentManager, AddDialogBottomSheet.TAG)
    }

    override fun navigateToFullTask(task: TaskView) {
        val action = ListTasksFragmentDirections.actionListTasksFragmentToDetailTaskFragment(task)
        findNavController().navigate(action)
    }
}

