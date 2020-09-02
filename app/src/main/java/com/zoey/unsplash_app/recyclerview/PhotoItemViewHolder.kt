package com.zoey.unsplash_app.recyclerview

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zoey.unsplash_app.App
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.Photo
import com.zoey.unsplash_app.utils.Constants.TAG
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotoItemViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    // 뷰들을 가져온다.
    private val photoImageView = itemView.photo_image
    private val photoCreatedAtText = itemView.created_at_text
    private val photoLikesCountText = itemView.likes_count_text

    //데이터와 뷰를 묶는다.
    fun bindWithView(photoItem: Photo) {
        Log.d(TAG, "PhotoItemViewHolder - bindWithView() called")

        photoCreatedAtText.text= photoItem.createdAt
        photoLikesCountText.text = photoItem.likesCount.toString()

        // 이미지를 설정한다.
        Glide.with(App.instance)
            .load(photoItem.thumbnail)
            .placeholder(R.drawable.ic_photo)   // 작동이 안될때 띄울 사진
            .into(photoImageView)
    }
}