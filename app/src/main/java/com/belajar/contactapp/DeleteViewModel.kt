package com.belajar.contactapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belajar.contactapp.data.ContactRepository
import com.belajar.contactapp.model.Contact
import com.belajar.contactapp.utils.Resource
import kotlinx.coroutines.launch

class DeleteViewModel(private val repository: ContactRepository): ViewModel() {
    private var _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>>
        get() = _contacts

    fun delete(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact).collect {
                when (it) {
                    is Resource.Error -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.d("DeleteViewModel: delete", "Success")
                    }
                }
            }
        }
    }
}