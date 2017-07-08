package com.oila.oneaccount.data.model

import java.util.Date

data class Profile(
        val name: Name,
        val email: String,
        val hexColor: String,
        val dateOfBirth: Date,
        val bio: String?,
        val avatar: String?)
