package com.yossefjm.dam2_3.db

import java.io.Serializable

data class UserClass(
    val id: Long = -1,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val birthdate: String,
    val dni: String
) : Serializable
