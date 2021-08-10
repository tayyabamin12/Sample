package com.safidence.safidence.data.model

data class ResponseTenantContractExpiry(
    val body: BodyTenantContractExpiry,
    val status: String,
    val status_code: Int
)

data class BodyTenantContractExpiry(
    val expiry_date: String
)