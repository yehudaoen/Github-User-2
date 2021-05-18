package com.example.githubuser2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser2.databinding.GithubUserListBinding

class ListGithubUserAdapter(private val listUser: List<DataUserGithub>) : RecyclerView.Adapter<ListGithubUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: DataUserGithub)

    }

    inner class ListViewHolder(private val binding: GithubUserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataUserGithub){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(ciList)
                tvNameList.text = user.name
                tvUsernameList.text = user.username

                itemView.setOnClickListener { Toast.makeText(itemView.context, "Kamu memilih ${user.name}", Toast.LENGTH_SHORT).show() }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = GithubUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}