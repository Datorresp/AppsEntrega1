package com.diego.youtube

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.diego.youtube.model.UserController
import com.diego.youtube.model.VideoController
import com.diego.youtube.databinding.ActivityProfileBinding
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileFragment : Fragment() , NameChangeFragment.OnListener {

    private var _binding:ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var videoController: VideoController
    private lateinit var userController: UserController

    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        _binding!!.textView.setText(userController.getActualUser().name)
        return view
    }

    private fun onCameraResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(file?.path)
            Log.e(">>>", file?.path+"")
            val thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/3, bitmap.height/3, true)
            val uri = activity?.let { getImageUriFromBitmap(it, thumbnail) }
            if (uri != null) {
                userController.getActualUser().profileImage = uri.toString()
            }
            binding.profileImage.setImageURI(uri)
        } else if (activityResult.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(activity, "No Se Tomó la Foto", Toast.LENGTH_SHORT).show()
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

        binding.textView.text = userController.getActualUser().name

        //Gallery
        binding.changeBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${activity?.getExternalFilesDir(null)}/photo.png")
            val uri = activity?.let { it1 -> FileProvider.getUriForFile(it1.applicationContext, it1.packageName, file!!) }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

            Log.e("----", uri?.path.toString())

            cameraLauncher.launch(intent)
        }

        binding.editNameBtn.setOnClickListener {
            val dialog = NameChangeFragment()
            dialog.listener = this
            activity?.supportFragmentManager?.let { it1 -> dialog.show(it1, "nameChange") }
        }

        binding.closeSesionBtn.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    fun setControllers(publicationController: VideoController, userController: UserController) {
        this.videoController = publicationController
        this.userController = userController
    }

    override fun onListener(name: String) {
        binding.textView.text = name
        userController.getActualUser().name = name
    }

    override fun onResume() {
        super.onResume()
        val uri = Uri.parse(userController.getActualUser().profileImage)
        if (uri != null) {
            binding.profileImage.setImageURI(uri)
        } else {
            Toast.makeText(context, "No Tienes Imagen de Perfil", Toast.LENGTH_SHORT)
        }
    }
}