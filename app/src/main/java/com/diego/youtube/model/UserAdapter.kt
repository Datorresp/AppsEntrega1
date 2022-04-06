package com.diego.youtube.model

class UserAdapter {

    private val userController = UserController()
    private var users = ArrayList<User>()

    init {
        users = userController.getUsers()
    }
}