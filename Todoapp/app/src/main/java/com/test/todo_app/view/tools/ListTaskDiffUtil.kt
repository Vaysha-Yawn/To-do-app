package com.test.todo_app.view.tools

import androidx.recyclerview.widget.DiffUtil
import com.test.todo_app.domain.model.Task

class ListTaskDiffUtil(
    private val oldList:List<Task>,
    private val newList:List<Task>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].state != newList[newItemPosition].state -> false
            oldList[oldItemPosition].dateCreated != newList[newItemPosition].dateCreated -> false
            oldList[oldItemPosition].shortDescription != newList[newItemPosition].shortDescription -> false
            oldList[oldItemPosition].fullDescription != newList[newItemPosition].fullDescription -> false
            else -> true
        }


    }
}