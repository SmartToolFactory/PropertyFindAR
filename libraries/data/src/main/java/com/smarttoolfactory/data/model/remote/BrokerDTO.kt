package com.smarttoolfactory.data.model.remote

import com.google.gson.annotations.SerializedName
import com.smarttoolfactory.data.model.DataTransferObject

data class BrokerDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone_extension")
    val phoneExtension: String,
    @SerializedName("email")
    val email: String,

    @SerializedName("mobile")
    val mobile: String?,

    @SerializedName("agent_photo")
    val agentPhoto: String?,
    @SerializedName("agent_name")
    val agentName: String,

    @SerializedName("lead_email_receivers")
    val leadEmailReceivers: List<String>,
    @SerializedName("license")
    val license: String?,
    @SerializedName("agent_id")
    val agentId: Int,
    @SerializedName("logo")
    val logo: String?
) : DataTransferObject
