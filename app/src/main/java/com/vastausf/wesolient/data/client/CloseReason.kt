package com.vastausf.wesolient.data.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CloseReason(
    val code: Int,
    val message: String
) : Parcelable
