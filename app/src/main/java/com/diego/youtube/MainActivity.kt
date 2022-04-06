package com.diego.youtube

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.diego.youtube.databinding.ActivityLoginBinding
import com.diego.youtube.model.UserController

class   MainActivity : AppCompatActivity() {

    private lateinit var userController: UserController

    private lateinit var binding: ActivityLoginBinding

    private lateinit var controllerActivity: ControllerActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        userController = UserController()
        controllerActivity = ControllerActivity()

        binding.logInBtn.setOnClickListener {
            var mail = binding.username.text.toString()
            var pass = binding.password.text.toString()

            val intUser = userController.verifyUser(mail, pass)
            if (intUser != -1) {
                val intent = Intent(this, ControllerActivity::class.java)
                intent.putExtra("actualUser", intUser)
                startActivity(intent)

            } else {
                val toast = Toast.makeText(applicationContext, "Contraseña y/o Usuario no Coinciden", Toast.LENGTH_LONG)
                toast.show()
            }
        }

    }

}