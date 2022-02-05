package com.blackapp.wajeezandroiddevelopertask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackapp.wajeezandroiddevelopertask.databinding.UserRowBinding
import com.blackapp.wajeezandroiddevelopertask.domain.model.User
import java.util.*
import javax.inject.Inject


class UserListAdapter
@Inject
constructor() : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var userList: ArrayList<User> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListAdapter.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UserRowBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        var item: User = userList[position]
        holder.bind(item)
    }

    override fun getItemCount() = userList.size

    fun submitData(userList: List<User>) {
        this.userList.clear()
        this.userList.addAll(userList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: UserRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User) {
            binding.user = data
            binding.executePendingBindings()
        }

    }
}