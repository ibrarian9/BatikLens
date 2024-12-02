package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class UserModel(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)
