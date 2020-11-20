package com.vastausf.wesolient.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CloseReason(
    val code: Int,
    val message: String
) : Parcelable {
    companion object {
        val key = "CLOSE_REASON"
    }
}
