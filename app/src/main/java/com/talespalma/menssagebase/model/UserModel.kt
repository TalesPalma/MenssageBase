package com.talespalma.menssagebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photos: String = ""
) : Parcelable
