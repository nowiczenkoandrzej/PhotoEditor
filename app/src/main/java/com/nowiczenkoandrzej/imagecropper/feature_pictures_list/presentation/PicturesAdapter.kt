package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nowiczenkoandrzej.imagecropper.R
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import java.io.ByteArrayOutputStream

class PicturesAdapter(
    val context: Context
): RecyclerView.Adapter<PicturesAdapter.ViewHolder>() {

    private val pictures = ArrayList<PictureModel>()
    private lateinit var listener: ItemClickListener

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<PictureModel>){
        pictures.clear()
        pictures.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(id: Int): PictureModel {
        return pictures[id]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.picture_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.progressBar.isVisible = true
        Glide
            .with(context)
            .load(pictures[position].editedBitmap)
            .into(holder.picture)
        holder.progressBar.isVisible = false

    }

    override fun getItemCount() = pictures.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var picture: ImageView
        var title: TextView
        var date: TextView
        var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
            picture = itemView.findViewById(R.id.im_picture)
            title = itemView.findViewById(R.id.tv_title)
            date = itemView.findViewById(R.id.tv_last_edit)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }
    }

    fun setClickListener(listener: ItemClickListener){
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

}