package com.safidence.safidence.data.model

data class ResponseTenantData(
    val body: List<Body>,
    val status: String,
    val status_code: Int
)

data class Profile1(
    val emergency_email: String,
    val emergency_name: String
)

data class Nationality(
    val name: String
)

data class Body(
    val email: String,
    val name: String,
    val nationality: Nationality,
    val nic: String,
    val phone: String,
    val profile: Profile1
)