package com.diego.youtube.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diego.youtube.R

interface OnDeletePublication {
    fun onDelete(video: Video?)
}

class videoFragment (itemView: View) : RecyclerView.ViewHolder(itemView) {

    var listener: OnDeletePublication? = null
    var video: Video? = null

    val preView: ImageView = itemView.findViewById(R.id.publiImg)
    val descView: TextView = itemView.findViewById(R.id.descrTxt)
    val canalView: TextView = itemView.findViewById(R.id.canalTxt)
    val viewersView: TextView = itemView.findViewById(R.id.viewersTxt)
    val timeView: TextView = itemView.findViewById(R.id.timeTxt)

}