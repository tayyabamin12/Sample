package com.safidence.safidence.data.model

data class ResponseAlerts(
    val body: List<AlertsBody>,
    val status: String,
    val status_code: Int
)

data class AlertsBody(
    val created_at: String,
    val event_time: String,
    val instructions: String,
    val message: String
)