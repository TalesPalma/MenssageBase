package com.talespalma.menssagebase.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast

fun Activity.toastMenssage(msg:String){
    Toast.makeText(
        this,
        msg,
        Toast.LENGTH_LONG
    ).show()
}

