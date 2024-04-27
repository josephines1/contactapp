package com.belajar.contactapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.belajar.contactapp.model.Contact
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.InputStream
import java.util.UUID

class AddActivity : AppCompatActivity() {

    private val addViewModel: AddViewModel by viewModel()
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

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

        val imageIv = findViewById<ImageView>(R.id.iv_add_image)

        // Set up Firebase Cloud Storage
        storageReference = FirebaseStorage.getInstance().reference

        // Set up image picker
        imageIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        addBtn.setOnClickListener {
            val name = nameEd.text.toString()
            val address = addressEd.text.toString()
            val number = numberEd.text.toString()

            // Upload image to Firebase Cloud Storage
            if (::imageUri.isInitialized) {
                val imageRef = storageReference.child("images/${UUID.randomUUID()}")
                imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
                    // Get the download URL of the uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Save the contact data to Firestore
                        val contact = Contact(
                            name = name,
                            address = address,
                            number = number,
                            imageUrl = imageUrl
                        )
                        addViewModel.addContact(contact)
                    }
                }
            } else {
                // Save the contact data to Firestore without an image
                val contact = Contact(
                    name = name,
                    address = address,
                    number = number
                )
                addViewModel.addContact(contact)
            }
        }

        addViewModel.contactAdded.observe(this) {
            if (it) {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            val imageIv = findViewById<ImageView>(R.id.iv_add_image)
            imageIv.setImageURI(imageUri)
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}