package com.ianarbuckle.gymplanner.authentication.domain

data class Register(
  val username: String,
  val password: String,
  val email: String,
  val firstName: String,
  val lastName: String,
)

data class RegisterResponse(val message: String)
