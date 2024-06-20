package com.test.todo_app.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.todo_app.databinding.FragmentListTasksBinding
import com.test.todo_app.domain.interfaces.view.ShowListUpdate
import com.test.todo_app.domain.model.ListTaskAction
import com.test.todo_app.domain.interfaces.view.NavigateToFullTask
import com.test.todo_app.domain.model.Task
import com.test.todo_app.domain.interfaces.view.ShowTaskMenuDialog
import com.test.todo_app.view.list_adapter.ListTaskAdapter
import com.test.todo_app.view.view_model.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListTasksFragment : Fragment(), ShowTaskMenuDialog, NavigateToFullTask, ShowListUpdate {

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: FragmentListTasksBinding

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
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        viewModel.makeAction(ListTaskAction.LoadPage)
        viewModel.attachShowListUpdate(this)

        val listTaskAdapter = ListTaskAdapter(viewModel, this, this, viewLifecycleOwner)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = listTaskAdapter
        binding.addTask.setOnClickListener {
            showAddTask()
        }
    }

    override fun showTaskMenuDialog(task: Task) {
        viewModel.currentTask.value = task
        val types = whatWeCanDoWithTask(task.state)
        val dialog = DialogMenuTask( types, viewModel)
        dialog.show(childFragmentManager, DialogMenuTask.TAG)
    }

    private fun showAddTask() {
        val modalAddDialogBottomSheet = AddDialogBottomSheet(viewModel)
        modalAddDialogBottomSheet.show(childFragmentManager, AddDialogBottomSheet.TAG)
    }

    override fun navigateToFullTask(task: Task) {
       val action = ListTasksFragmentDirections.actionListTasksFragmentToDetailTaskFragment(task)
        findNavController().navigate(action)
    }

    override fun updateRV(position: IntRange){
        binding.recyclerView.adapter?.notifyItemRangeChanged(position.first, position.count())
    }

    override fun addRV(position:Int){
        binding.recyclerView.adapter?.notifyItemInserted(position)
    }

    override fun deleteRV(position:Int){
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }

    override fun showRV() {
        binding.recyclerView.adapter?.notifyItemInserted(viewModel.listTask.size)
    }

}

