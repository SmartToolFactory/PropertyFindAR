package com.smarttoolfactory.data.model.local

import com.smarttoolfactory.data.model.IEntity

data class BrokerEntity(
    val id: Int,
    val name: String,
    val address: String,
    val phone: String,
    val phoneExtension: String,
    val email: String,

    val mobile: String?,
    val agentPhoto: String?,
    val agentName: String,

    val leadEmailReceivers: List<String>,
    val license: String?,
    val agentId: Int,
    val logo: String?
) : IEntity
