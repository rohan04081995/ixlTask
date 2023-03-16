package com.example.ixltask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ixltask.models.PersonalInfo

class UserDataAdapter(
    val usersDataList: List<PersonalInfo>,
    var userDataAdapterClickListener: UserDataAdapterClickListener
) : RecyclerView.Adapter<UserDataAdapter.UserDataHolder>() {

    interface UserDataAdapterClickListener {
        fun userDataAdapterClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_rv_layout, parent, false)

        return UserDataHolder(view)
    }

    override fun onBindViewHolder(holder: UserDataHolder, position: Int) {
        holder.userNameTvRec.text =
            "${position + 1}. ${usersDataList[position].firstName} ${usersDataList[position].lastName}"
    }

    override fun getItemCount(): Int {
        return usersDataList.size
    }

    inner class UserDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val userNameTvRec: TextView = itemView.findViewById(R.id.userNameTvRec)
        val userRecRootCl: ConstraintLayout = itemView.findViewById(R.id.userRecRootCl)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                userDataAdapterClickListener.userDataAdapterClick(adapterPosition)
            }
        }
    }

}