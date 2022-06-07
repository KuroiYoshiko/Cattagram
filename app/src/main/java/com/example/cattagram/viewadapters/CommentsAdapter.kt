package com.example.cattagram.viewadapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattagram.R
import com.example.cattagram.retrofit.Api
import com.example.cattagram.retrofit.GetCommentsResponse
import com.example.cattagram.retrofit.ImagesGetUserDetailsResponse
import com.example.cattagram.retrofit.UserGetUserDetailsResponse
import com.squareup.picasso.Picasso
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit

class CommentsAdapter: RecyclerView.Adapter<CommentsAdapter.ViewHolder?>() {

    private var commentResponse = emptyList<GetCommentsResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_listitem, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(commentResponse[position].avatar).fit().into(holder.profilePicture)
        holder.comment.text = commentResponse[position].comment
        holder.username.text = commentResponse[position].username
    }

    override fun getItemCount(): Int {
        return commentResponse.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var profilePicture: ImageView
        var username: TextView
        var comment: TextView
        var con: Context

        init {
            profilePicture = itemView.findViewById(R.id.commentProfilePicIv)
            username = itemView.findViewById(R.id.commentUsernameTv)
            comment = itemView.findViewById(R.id.commentBody)
            con = itemView.context
        }
    }

    fun setData(newCommentList: List<GetCommentsResponse>) {
        commentResponse = newCommentList
        notifyDataSetChanged()
    }

    private fun userRequest(): Api? {

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("accept", "application / json")
                .addHeader("Authorization", "Bearer 1234567asdfgh")
                .build()
            chain.proceed(newRequest)
        }.build()

        val retrofitBuilder = Retrofit.Builder()
            .client(client)
            .baseUrl("http://158.101.165.232:7000/")
            .build()
            .create(Api::class.java)


        return retrofitBuilder
    }
}