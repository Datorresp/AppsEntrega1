package com.diego.youtube

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.diego.youtube.databinding.ActivityControllerBinding
import com.diego.youtube.model.SerializationClass
import com.diego.youtube.model.UserController
import com.diego.youtube.model.VideoController
import com.diego.youtube.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar


class ControllerActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var publishFragment: PublishFragment

    private lateinit var binding: ActivityControllerBinding

    private lateinit var userController: UserController
    private lateinit var videoController: VideoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControllerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

            desSerialized()

        val bundle = intent.extras
        val actualUser = bundle?.getInt("actualUser")
        if (actualUser != null) {
            userController.setUpActualUser(actualUser)
        }

        homeFragment = HomeFragment.newInstance()
        homeFragment.setControllers(videoController, userController)
        profileFragment = ProfileFragment.newInstance()
        profileFragment.setControllers(videoController, userController)
        publishFragment = PublishFragment.newInstance()
        publishFragment.setControllers(videoController, userController)

        showFragment(homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> showFragment(homeFragment)
                R.id.profile -> showFragment(profileFragment)
                R.id.publish -> showFragment(publishFragment)
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.fragmentControl, fragment)

        transaction.commit()
    }

    override fun onPause() {
        super.onPause()
        val serializationClass = SerializationClass(videoController, userController)

    //    val json = Gson().toJson(serializationClass)
      //  Log.e(">>>", json.toString())
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sharedPreferences.edit()
        //    .putString("serialization" , json)
            .apply()
    }

    private fun desSerialized() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val json = sharedPreferences.getString("serialization", "No_Data")
        Log.e("!!!", json.toString())
        if (json != "No_Data") {
       //     val ser = Gson().fromJson(json, SerializationClass::class.java)
       //     userController = ser.userController
        //    videoController = ser.publicationController
        } else {
            userController = UserController()
            videoController = VideoController()
        }
    }

}