package com.belajar.contactapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.belajar.contactapp.model.Contact
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddActivity : AppCompatActivity() {

    private val addViewModel: AddViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameEd = findViewById<EditText>(R.id.ed_add_name)
        val addressEd = findViewById<EditText>(R.id.ed_add_address)
        val numberEd = findViewById<EditText>(R.id.ed_add_number)
        val addBtn = findViewById<Button>(R.id.btn_add)

        addBtn.setOnClickListener {
            val contact = Contact(
                name = nameEd.text.ifEmpty { "" }.toString(),
                address = addressEd.text.ifEmpty { "" }.toString(),
                number = numberEd.text.ifEmpty { "" }.toString(),
            )
            addViewModel.addContact(contact)
        }
        addViewModel.contactAdded.observe(this) {
            if (it) {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}