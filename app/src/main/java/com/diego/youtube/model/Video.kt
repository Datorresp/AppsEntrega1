package com.diego.youtube.model

class Video {

    var preview: String
    var description: String
    var canal: String
    var viewers: String
    var time: String

    constructor(preview:String, description:String, canal:String, viewers:String, time: String) {
        this.preview = preview
        this.description = description
        this.canal = canal
        this.viewers = viewers
        this.time = time
    }
}