package com.test.todo_app.view.list_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.todo_app.R
import com.test.todo_app.domain.interfaces.view.NavigateToFullTask
import com.test.todo_app.domain.interfaces.view.ShowTaskMenuDialog
import com.test.todo_app.domain.model.StateTask
import com.test.todo_app.domain.model.getImgResByStatus
import com.test.todo_app.view.model.TaskView

class ListTaskAdapter(
    private var list: List<TaskView >,
    private val showTaskMenuDialog: ShowTaskMenuDialog,
    private val navigateToFullTask: NavigateToFullTask,
) :
    RecyclerView.Adapter<ListTaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val state: ImageView
        val moreButton: ImageView
        val date: TextView
        val shortDescription: TextView

        init {
            state = view.findViewById(R.id.status)
            moreButton = view.findViewById(R.id.menu)
            date = view.findViewById(R.id.date)
            shortDescription = view.findViewById(R.id.short_description)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_holder_task_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task = list[position]
        viewHolder.state.setImageResource(task.state.getImgResByStatus())
        viewHolder.date.text = task.dateCreated
        viewHolder.shortDescription.text = task.shortDescription
        viewHolder.moreButton.setOnClickListener {
            showTaskMenuDialog.showTaskMenuDialog(task)
        }
        if (task.state == StateTask.done) {
            viewHolder.moreButton.visibility = View.GONE
        } else {
            viewHolder.moreButton.visibility = View.VISIBLE
        }
        viewHolder.itemView.setOnClickListener {
            navigateToFullTask.navigateToFullTask(task)
        }
    }

    fun setData(newList: List<TaskView>){
        val diffUtil = ListTaskDiffUtil(list, newList)
        val diffRes = DiffUtil.calculateDiff(diffUtil)
        list = newList
        diffRes.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = list.size
}
