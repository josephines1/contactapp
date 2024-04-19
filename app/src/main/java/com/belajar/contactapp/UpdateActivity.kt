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

class UpdateActivity : AppCompatActivity() {

    private val updateViewModel: UpdateViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val contact: Contact? = intent.getParcelableExtra("CONTACT")

        val nameEd = findViewById<EditText>(R.id.ed_update_name)
        val addressEd = findViewById<EditText>(R.id.ed_update_address)
        val numberEd = findViewById<EditText>(R.id.ed_update_number)
        val updateBtn = findViewById<Button>(R.id.btn_update)

        // Set data awal contact ke dalam EditText
        nameEd.setText(contact?.name)
        addressEd.setText(contact?.address)
        numberEd.setText(contact?.number)

        updateBtn.setOnClickListener {
            val contactInput = Contact(
                id = contact?.id,
                name = nameEd.text.ifEmpty { "" }.toString(),
                address = addressEd.text.ifEmpty { "" }.toString(),
                number = numberEd.text.ifEmpty { "" }.toString(),
            )
            updateViewModel.updateContact(contactInput)
        }
        updateViewModel.contactUpdated.observe(this) {
            if (it) {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}