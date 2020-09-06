package com.smarttoolfactory.data.model.remote

import com.google.gson.annotations.SerializedName

data class BrokerDTO(
    @SerializedName("address")
    val address: String,
    @SerializedName("agent_id")
    val agentId: Int,
    @SerializedName("agent_name")
    val agentName: String,
    @SerializedName("agent_photo")
    val agentPhoto: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lead_email_receivers")
    val leadEmailReceivers: List<String>,
    @SerializedName("license")
    val license: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone_extension")
    val phoneExtension: String
)
