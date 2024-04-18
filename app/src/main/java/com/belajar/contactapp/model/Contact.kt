package com.belajar.contactapp.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    @DocumentId val id: String? = null,
    val name: String,
    val address: String,
    val number: String
) : Parcelable {
    constructor(): this(null, "", "", "")
}