package com.belajar.contactapp.data

import android.util.Log
import com.belajar.contactapp.model.Contact
import com.belajar.contactapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ContactRepository(
    private val firestoreDb: FirebaseFirestore
) {
    suspend fun insert(contact: Contact) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val ref = firestoreDb.collection("contacts")
            ref.add(contact)
            emit(Resource.Success(Unit))
        } catch (e: Error) {
            Log.e("ContactRepository: Insert", e.toString())
            emit(Resource.Error(e.toString()))
        }
    }

    fun getContact(): Flow<List<Contact>> = flow {
        val ref = firestoreDb.collection("contacts")
        val querySnapshot = ref.get().await()
        if (!querySnapshot.isEmpty) {
            emit(querySnapshot.toObjects(Contact::class.java))
        }
    }

    suspend fun update(contact: Contact): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val ref = firestoreDb.collection("contacts")
            ref.document(contact.id.orEmpty()).update(
                mapOf(
                    "name" to contact.name,
                    "address" to contact.address,
                    "number" to contact.number
                )
            )
            emit(Resource.Success(Unit))
        } catch (e: Error) {
            Log.e("ContactRepository: update", e.toString())
            emit(Resource.Error(e.toString()))
        }
    }

    suspend fun delete(contact: Contact): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val ref = firestoreDb.collection("contacts")
            ref.document(contact.id.orEmpty()).delete()
            emit(Resource.Success(Unit))
        } catch (e: Error) {
            Log.e("ContactRepository: delete", e.toString())
            emit(Resource.Error(e.toString()))
        }
    }
}