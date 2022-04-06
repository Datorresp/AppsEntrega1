package com.diego.youtube

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.diego.youtube.model.UserController
import com.diego.youtube.model.VideoAdapter
import com.diego.youtube.model.VideoController
import com.diego.youtube.databinding.ActivityHomeBinding

class   HomeFragment : Fragment()  {

    private lateinit var videoController: VideoController
    private lateinit var userController: UserController

    private lateinit var videoAdapter: VideoAdapter


    private var _binding:ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            videoAdapter = VideoAdapter()
            adapter = videoAdapter
            videoAdapter.setPublications(videoController, userController)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onResume() {
        super.onResume()
        Log.e(">>>", "Resume")
        videoAdapter.refresh()
    }

    fun setControllers(publicationController: VideoController, userController: UserController) {
        this.videoController = publicationController
        this.userController = userController
    }
}