package com.example.usersinformation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecycleViewAdapter(
    private var userList: MutableList<ApiUser>,
    private val onItemClick: (ApiUser) -> Unit = {}
) : RecyclerView.Adapter<RecycleViewAdapter.UserViewHolder>(){

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameSurnameTextView: TextView = itemView.findViewById(R.id.NameAndSurname)
        val emailTextView: TextView = itemView.findViewById(R.id.EmailAddress)
        val telephoneTextView: TextView = itemView.findViewById(R.id.Telephone)
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.nameSurnameTextView.text = currentUser.name.getFullName()
        holder.emailTextView.text = currentUser.email
        holder.telephoneTextView.text = currentUser.phone

        Glide.with(holder.itemView.context)
            .load(currentUser.picture.medium)
            .placeholder(R.drawable.testavatar)
            .error(R.drawable.testavatar1)
            .circleCrop()
            .into(holder.avatarImageView)

        holder.itemView.setOnClickListener {
            onItemClick(currentUser)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUsers(newUsers: List<ApiUser>) {
        userList.clear()
        userList.addAll(newUsers)
        notifyDataSetChanged()
    }
}