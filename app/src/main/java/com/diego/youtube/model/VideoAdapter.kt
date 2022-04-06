package com.diego.youtube.model

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.diego.youtube.R

class VideoAdapter: RecyclerView.Adapter<videoFragment>(), OnDeletePublication {

    private var videos = ArrayList<Video>()
    private lateinit var userController: UserController
    private lateinit var videoController : VideoController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): videoFragment {
        videos = videoController.getVideo()
        val inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.fragment_video, parent, false)
        val videoView = videoFragment(row)
        return videoView
    }

    override fun onBindViewHolder(holder: videoFragment, position: Int) {
        val video = videos[position]
        holder.listener = this
        holder.video = video

        val uri = Uri.parse(video.preview)
        holder.preView.setImageURI(uri)
        holder.descView.setText(video.description)
        holder.canalView.setText(userController.getActualUser().name)
        holder.viewersView.setText(video.viewers)
        holder.timeView.setText(video.time)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onDelete(Video: Video?) {
    }

    fun setPublications(videoController: VideoController, userController: UserController) {
        this.videoController = videoController
        this.userController = userController
    }

    fun refresh() {
        videos = videoController.getVideo();
    }
}