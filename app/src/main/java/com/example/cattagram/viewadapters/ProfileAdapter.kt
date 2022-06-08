package com.example.cattagram.viewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattagram.R
import com.example.cattagram.retrofit.GetCommentsResponse
import com.example.cattagram.retrofit.ProfileImgResponse
import com.squareup.picasso.Picasso


class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ViewHolder?>() {

    private var response = emptyList<ProfileImgResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.profile_listitem, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(response[position].link).rotate(90f).fit().into(holder.picture)
    }

    override fun getItemCount(): Int {
        return response.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var picture: ImageView
        var con: Context

        init {
            picture = itemView.findViewById(R.id.catPic)
            con = itemView.context
        }
    }

    fun setData(newCommentList: List<ProfileImgResponse>) {
        response = newCommentList
        notifyDataSetChanged()
    }
}