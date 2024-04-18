package com.belajar.contactapp

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val contact: Contact?  = intent.getParcelableExtra("CONTACT")

        val name = contact?.name
        val address = contact?.address
        val number = contact?.number

        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvAddress = findViewById<TextView>(R.id.tv_detail_address)
        val tvNumber = findViewById<TextView>(R.id.tv_detail_number)

        tvName.text = name
        tvAddress.text = address
        tvNumber.text = number
    }
}