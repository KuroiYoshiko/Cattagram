package com.example.cattagram.viewadapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cattagram.R
import com.example.cattagram.picture.ShowPictureActivity
import com.example.cattagram.retrofit.getOneImgResponse
import com.squareup.picasso.Picasso


class MainPagePicAdapter: RecyclerView.Adapter<MainPagePicAdapter.ViewHolder?>() {

    private var response = emptyList<getOneImgResponse>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainPagePicAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.frame_news, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainPagePicAdapter.ViewHolder, position: Int) {
        Picasso.get().load(response[position].link).fit().into(holder.catPicture)
        holder.commentsButton.setOnClickListener {
            val intent = Intent(holder.con, ShowPictureActivity::class.java)
            startActivity(holder.con, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return response.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var profilePicture: ImageView
        var username: TextView
        var catPicture: ImageView
        var likeButton: Button
        var commentsButton: Button
        var con: Context

        init {
            profilePicture = itemView.findViewById(R.id.profilePicIv)
            username = itemView.findViewById(R.id.usernameTv)
            catPicture = itemView.findViewById(R.id.pictureIv)
            likeButton = itemView.findViewById(R.id.btLikePic)
            commentsButton = itemView.findViewById(R.id.btComments)
            con = itemView.context
        }
    }

    fun setData(newList: List<getOneImgResponse>) {
        response = newList
        notifyDataSetChanged()
    }
}