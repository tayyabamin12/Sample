package com.safidence.safidence.data.model

data class ResponseLogin(
    val access_token: String,
    val success: Boolean,
    val user: User
)

data class Role(
    val id: Int,
    val title: String
)

data class Profile(
    val address: String,
    val company: String,
    val created_at: String,
    val emergency_email: String,
    val emergency_name: String,
    val emergency_phone: String,
    val id: Int,
    val national_country_id: Int,
    val occupation: String,
    val owner_id: Int,
    val permanent_city_id: Int,
    val permanent_country_id: Int,
    val residence_country_id: Int,
    val updated_at: String
)

data class User(
    val created_at: String,
    val email: String,
    val email_verified_at: String,
    val id: Int,
    val is_active: Int,
    val name: String,
    val nic: String,
    val owner_id: String,
    val phone: String,
    val profile: Profile,
    val profile_id: Int,
    val profile_type: String,
    val role: Role,
    val role_id: Int,
    val send_login_notification: Int,
    val updated_at: String
)