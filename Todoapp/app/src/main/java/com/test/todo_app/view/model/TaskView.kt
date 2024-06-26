package com.test.todo_app.view.model

import android.os.Parcel
import android.os.Parcelable
import com.test.todo_app.domain.model.StateTask


data class TaskView (
    val id: Int = 0,
    var shortDescription:String,
    var fullDescription: String,
    val dateCreated:String,
    val state: StateTask,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?.stringToState()?:StateTask.newTask
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(shortDescription)
        parcel.writeString(fullDescription)
        parcel.writeString(dateCreated)
        parcel.writeString(state.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskView> {
        override fun createFromParcel(parcel: Parcel): TaskView {
            return TaskView(parcel)
        }

        override fun newArray(size: Int): Array<TaskView?> {
            return arrayOfNulls(size)
        }

        fun String.stringToState():StateTask?{
            return when(this){
                StateTask.newTask.name -> StateTask.newTask
                StateTask.inProgress.name -> StateTask.inProgress
                StateTask.done.name -> StateTask.done
                else -> {null}
            }
        }
    }
}