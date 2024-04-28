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
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class UpdateActivity : AppCompatActivity() {

    private val updateViewModel: UpdateViewModel by viewModel()
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

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
        val ivImage = findViewById<ImageView>(R.id.iv_update_image)
        val updateBtn = findViewById<Button>(R.id.btn_update)

        // Set up Firebase Cloud Storage
        storageReference = FirebaseStorage.getInstance().reference

        // Set up image picker
        ivImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, AddActivity.IMAGE_PICK_CODE)
        }

        // Set data awal contact ke dalam EditText
        nameEd.setText(contact?.name)
        addressEd.setText(contact?.address)
        numberEd.setText(contact?.number)
        Picasso.get()
            .load(contact?.imageUrl)
            .into(ivImage)

        if(contact?.imageUrl == null) {
            ivImage.setImageResource(R.drawable.account_circle_24)
        }

        updateBtn.setOnClickListener {
            val currentContact = contact
            // if image changed
            if (currentContact != null) {
                if (::imageUri.isInitialized) {

                    if(currentContact.imageUrl != null) {
                        val oldImageRef = storageReference.child(getFileNameFromUrl(currentContact.imageUrl))

                        oldImageRef.delete().addOnSuccessListener {
                            Log.i("UpdateActivity", "Success deleting old image")
                        }.addOnFailureListener { e ->
                            // Handle any errors
                            Log.e("UpdateActivity", "Error deleting old image", e)
                        }
                    }

                    val imageRef = storageReference.child("images/${UUID.randomUUID()}")
                    imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
                        // Get the download URL of the uploaded image
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Save the contact data to Firestore
                            val contact = Contact(
                                id = contact?.id,
                                name = nameEd.text.ifEmpty { "" }.toString(),
                                address = addressEd.text.ifEmpty { "" }.toString(),
                                number = numberEd.text.ifEmpty { "" }.toString(),
                                imageUrl = imageUrl
                            )
                            updateViewModel.updateContact(contact, imageUrl)
                        }
                    }
                } else {
                    // Save the contact data to Firestore without an image
                    val contact = Contact(
                        id = contact?.id,
                        name = nameEd.text.ifEmpty { "" }.toString(),
                        address = addressEd.text.ifEmpty { "" }.toString(),
                        number = numberEd.text.ifEmpty { "" }.toString(),
                        imageUrl = contact?.imageUrl
                    )
                    updateViewModel.updateContact(contact, contact?.imageUrl)
                }
            }
        }
        updateViewModel.contactUpdated.observe(this) {
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
            val imageIv = findViewById<ImageView>(R.id.iv_update_image)
            imageIv.setImageURI(imageUri)
        }
    }

    private fun getFileNameFromUrl(url: String?): String {
        val uri = Uri.parse(url)
        val segments = uri.pathSegments
        return segments.lastOrNull()?.split("?")?.first() ?: ""
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}