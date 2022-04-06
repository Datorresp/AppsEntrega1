package com.diego.youtube.model

class VideoController {

    private var videos = ArrayList<Video>()

    fun addVideo(newPublication: Video) {
        videos.add(newPublication)
    }

    fun getVideo(): ArrayList<Video> {
        return videos
    }

}