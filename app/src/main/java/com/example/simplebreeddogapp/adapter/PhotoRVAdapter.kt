package com.example.simplebreeddogapp.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.simplebreeddogapp.R
import com.example.simplebreeddogapp.databinding.LayoutItemImagesBinding

class PhotoRVAdapter(
    photoList: List<String>,
    private val context: Context
) : RecyclerView.Adapter<PhotoRVAdapter.PhotoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_item_images,
            parent, false
        )
        return PhotoViewHolder(itemView)
    }

    private var imageList = photoList

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        Log.d("URL", imageList[position])


        holder.binding.loader.visibility = View.VISIBLE
        holder.binding.ivError.visibility = View.GONE

        Glide.with(context)
            .load(imageList[position])
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.binding.ivError.visibility = View.VISIBLE
                    holder.binding.loader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.binding.ivError.visibility = View.GONE
                    holder.binding.loader.visibility = View.GONE
                    return false
                }

            })
            .into(holder.binding.idIVImage)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateList(photoList: List<String>) {
        imageList = photoList
        notifyDataSetChanged()
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LayoutItemImagesBinding.bind(itemView)
    }

}