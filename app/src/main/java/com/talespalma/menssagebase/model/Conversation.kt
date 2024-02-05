package com.talespalma.menssagebase.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Conversation(
    val idUserCurernt:String = "",
    val idDetinationsUser:String = "",
    val photo:String = "",
    val name:String = "",
    val menssage:String = "",
    @ServerTimestamp
    val date:Date? = null
): Parcelable
