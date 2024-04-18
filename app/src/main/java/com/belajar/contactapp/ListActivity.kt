package com.belajar.contactapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belajar.contactapp.model.Contact
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListActivity : AppCompatActivity() {
    private val listViewModel: ListViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val contacts = listOf(
            Contact (
                name = "Ani",
                address = "Jalan Raya 5",
                number = "0867-8909-1010"
            ),
            Contact(
                name = "Akalia",
                address = "Jalan Raya 10",
                number = "021-9090-1920"
            )
        )

        val recyclerView: RecyclerView = findViewById(R.id.rv_contacts)

        listViewModel.getContacts()
        listViewModel.contacts.observe(this) {
            Log.d("ListActivity", it.toString())

            recyclerView.adapter = ListAdapter(this, it)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }
}