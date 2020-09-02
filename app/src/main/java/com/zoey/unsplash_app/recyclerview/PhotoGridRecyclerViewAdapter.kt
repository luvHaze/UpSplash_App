package com.zoey.unsplash_app.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.Photo

class PhotoGridRecyclerViewAdapter : RecyclerView.Adapter<PhotoItemViewHolder>() {

    private var photoList = ArrayList<Photo>()

    // 뷰홀더와 레이아웃 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        return PhotoItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item, parent, false))
    }

    // 보여줄 목록의 개수
    override fun getItemCount(): Int {
        return photoList.size
    }

    // 뷰가 묶였을때 데이터를 뷰홀더에 넘겨준다.
    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        holder.bindWithView(photoList[position])
    }

    // 외부에서 어댑터에 데이터 배열을 넣어준다.
    fun submitjList(photoList: ArrayList<Photo>) {
        this.photoList = photoList
    }

}