package com.belajar.contactapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belajar.contactapp.model.Contact
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        // Cek apakah ada pesan yang dikirim melalui Intent
        val message = intent.getStringExtra("message")
        if (!message.isNullOrEmpty()) {
            // Tampilkan pesan menggunakan Toast
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Data Dummy
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

        // Panggil komponen RecyclerView dari layout
        val recyclerView: RecyclerView = findViewById(R.id.rv_contacts)

        // Ambil data contact
        listViewModel.getContacts()

        // Jika data terdeteksi
        listViewModel.contacts.observe(this) {
            Log.d("ListActivity", it.toString())

            // Kirim ke ListAdapter untuk ditampilkan di RecyclerView
            recyclerView.adapter = ListAdapter(this, it)
        }

        // Tetapkan LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Untuk menuju halaman tambah data
        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}