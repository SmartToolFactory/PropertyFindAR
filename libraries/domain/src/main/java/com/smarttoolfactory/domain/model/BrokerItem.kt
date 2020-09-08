package com.smarttoolfactory.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BrokerItem(
    val id: Int,
    val name: String,
    val address: String,
    val phone: String,
    val phoneExtension: String,
    val email: String,

    val mobile: String?,
    val agentPhoto: String,
    val agentName: String,

    val leadEmailReceivers: List<String>,
    val license: String,
    val agentId: Int,
    val logo: String
) : Item, Parcelable
