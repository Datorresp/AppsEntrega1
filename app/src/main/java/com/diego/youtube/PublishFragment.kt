package com.diego.youtube

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.diego.youtube.databinding.FragmentPublishBinding
import com.diego.youtube.model.UserController
import com.diego.youtube.model.Video
import com.diego.youtube.model.VideoController
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class PublishFragment : Fragment() {

    private var _binding:FragmentPublishBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null
    private var image: String = ""

    private lateinit var videoController: VideoController
    private lateinit var userController: UserController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    private fun onCameraResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(file?.path)
            Log.e(">>>", file?.path+"")
            val thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/3, bitmap.height/3, true)
            val uri = activity?.let { getImageUriFromBitmap(it, thumbnail) }
            if (uri != null) {
                image = uri.toString()
            }
            binding.imageView2.setImageURI(uri)
        } else if (activityResult.resultCode == RESULT_CANCELED) {
            Toast.makeText(activity, "No Se Tomó la Foto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onGalleryResult(activityResult: ActivityResult?) {
        if (activityResult?.resultCode == RESULT_OK) {
            val uriImage = activityResult?.data?.data

            Log.e(">>>", uriImage?.path+"")

            if (uriImage != null) {
                //image = uriImage.toString()
            }
            uriImage?.let {
                binding.imageView2.setImageURI(uriImage)
            }
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onCameraResult)
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        //Camera
        binding.camaraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${activity?.getExternalFilesDir(null)}/photo.png")
            val uri = activity?.let { it1 -> FileProvider.getUriForFile(it1.applicationContext, it1.packageName, file!!) }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

            Log.e("----", uri?.path.toString())

            cameraLauncher.launch(intent)
        }

        binding.galleryBtn.setOnClickListener {
            val intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            }
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }


        //Add Publication
        binding.addPubBtn.setOnClickListener {
            Log.e(">>>", "")
            if (binding.descriptionTxt.text.toString().compareTo("") != 0) {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH) + 1
                val day = c.get(Calendar.DAY_OF_MONTH)

                val currentTime = "$year/$month/$day"
                videoController.addVideo(Video(image, binding.descriptionTxt.text.toString(), userController.getActualUserId().toString(), "1", currentTime))
                binding.descriptionTxt.text.clear()
                binding.imageView2.setImageURI(Uri.EMPTY)
                Toast.makeText(requireContext(), "Se ha creado" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PublishFragment()
    }

    fun setControllers(publicationController: VideoController, userController: UserController) {
        this.videoController = publicationController
        this.userController = userController
    }
}