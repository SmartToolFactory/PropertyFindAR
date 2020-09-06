package com.smarttoolfactory.data.model.local

data class BrokerEntity(
    val address: String,
    val agentId: Int,
    val agentName: String,
    val agentPhoto: String,
    val email: String,
    val id: Int,
    val leadEmailReceivers: List<String>,
    val license: String,
    val logo: String,
    val mobile: String?,
    val name: String,
    val phone: String,
    val phoneExtension: String
)
