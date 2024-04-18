package com.belajar.contactapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val address: String,
    val number: String
) : Parcelable
