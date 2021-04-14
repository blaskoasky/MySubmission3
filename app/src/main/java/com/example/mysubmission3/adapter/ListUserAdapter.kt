package com.example.mysubmission3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.data.UserData
import com.example.mysubmission3.Activity.UserDetailActivity
import com.example.mysubmission3.databinding.UserItemBinding



class ListUserAdapter (private val listUser: ArrayList<UserData>):
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }
    override fun getItemCount(): Int = listUser.size


    inner class ListViewHolder (private val binding: UserItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(100,100))
                    .into(civAvatar)
                tvUsername.text = user.username
                tvName.text = user.name
                tvCompany.text = user.company
                tvLocation.text = user.location

                itemView.setOnClickListener {
                    val uData = UserData()
                    uData.username = user.username
                    uData.name = user.username
                    uData.avatar = user.avatar
                    uData.repository = user.repository
                    uData.company = user.company
                    uData.location = user.location
                    uData.followers = user.followers
                    uData.following = user.following
                    uData.isFav = user.isFav

                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_DATA, uData)
                    intent.putExtra(UserDetailActivity.EXTRA_FAVOURITE, uData)
                    context.startActivity(intent)
                }

            }
        }
    }
}