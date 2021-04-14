package com.example.mysubmission3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.Activity.UserDetailActivity
import com.example.mysubmission3.data.Favourite
import com.example.mysubmission3.databinding.UserItemBinding
import com.example.mysubmission3.helper.CustomOnItemClickListener

class FavouriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavouriteAdapter.ListViewHolder>() {

    var listFavourite = ArrayList<Favourite>()
        set(listFavourite) {
            if (listFavourite.size > 0) {
                this.listFavourite.clear()
            }
            this.listFavourite.addAll(listFavourite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavourite[position])
    }

    override fun getItemCount(): Int = listFavourite.size


    inner class ListViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favourite: Favourite){
            with(binding){
                Glide.with(itemView.context)
                    .load(favourite.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(civAvatar)
                tvUsername.text = favourite.username
                tvName.text = favourite.name
                tvCompany.text = favourite.company
                tvLocation.text = favourite.location

                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback{
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, UserDetailActivity::class.java)
                                intent.putExtra(UserDetailActivity.EXTRA_POSITION, position)
                                intent.putExtra(UserDetailActivity.EXTRA_NOTE, favourite)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )

            }
        }

    }
}