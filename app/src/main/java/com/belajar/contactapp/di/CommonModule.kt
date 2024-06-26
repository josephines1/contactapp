package com.belajar.contactapp.di

import com.belajar.contactapp.AddViewModel
import com.belajar.contactapp.ListViewModel
import com.belajar.contactapp.UpdateViewModel
import com.belajar.contactapp.DeleteViewModel
import com.belajar.contactapp.data.ContactRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ListViewModel(get()) }
    viewModel { AddViewModel(get()) }
    viewModel { UpdateViewModel(get()) }
    viewModel { DeleteViewModel(get()) }
}

val repositoryModule = module {
    single<ContactRepository> { ContactRepository(get()) }
}