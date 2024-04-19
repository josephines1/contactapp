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

class UpdateViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _contactUpdated = MutableLiveData(false)
    val contactUpdated: LiveData<Boolean>
        get() = _contactUpdated

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact).collect {
                when (it) {
                    is Resource.Error -> {
                        _contactUpdated.value = false
                        Log.e("UpdateViewModel: Update", it.error)
                    }
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        _contactUpdated.value = true
                    }
                }
            }
        }
    }
}