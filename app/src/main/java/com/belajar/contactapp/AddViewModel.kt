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

class AddViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _contactAdded = MutableLiveData(false)
    val contactAdded: LiveData<Boolean>
        get() = _contactAdded

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            repository.insert(contact).collect {
                when (it) {
                    is Resource.Error -> {
                        _contactAdded.value = false
                        Log.e("AddViewModel: Insert", it.error)
                    }
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        _contactAdded.value = true
                    }
                }
            }
        }
    }
}