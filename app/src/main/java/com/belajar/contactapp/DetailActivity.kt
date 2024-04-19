package com.belajar.contactapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.belajar.contactapp.model.Contact

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil hasil passing data contact
        val contact: Contact?  = intent.getParcelableExtra("CONTACT")

        val name = contact?.name
        val address = contact?.address
        val number = contact?.number

        // Panggil komponen-komponen di layout
        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvAddress = findViewById<TextView>(R.id.tv_detail_address)
        val tvNumber = findViewById<TextView>(R.id.tv_detail_number)

        // Definisikan text pada setiap komponen
        tvName.text = name
        tvAddress.text = address
        tvNumber.text = number

        // Panggil komponen button Update di layout
        val btnUpdate = findViewById<TextView>(R.id.btn_detail_update)

        // Arahkan menuju halaman update data
        btnUpdate.setOnClickListener{
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("CONTACT", contact)
            startActivity(intent)
        }
    }
}