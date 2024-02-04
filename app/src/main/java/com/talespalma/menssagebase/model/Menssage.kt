package com.talespalma.menssagebase.model

import com.google.firebase.firestore.ServerTimestamp
import com.google.type.DateTime
import java.util.Date

data class Menssage(
    val menssage: String = "",
    @ServerTimestamp
    val date: Date?=null
)
