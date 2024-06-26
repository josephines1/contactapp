package com.belajar.contactapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.belajar.contactapp.model.Contact
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private val deleteViewModel: DeleteViewModel by viewModel()
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

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
        val imageUrl = contact?.imageUrl

        // Panggil komponen-komponen di layout
        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvAddress = findViewById<TextView>(R.id.tv_detail_address)
        val tvNumber = findViewById<TextView>(R.id.tv_detail_number)
        val ivImage = findViewById<ImageView>(R.id.iv_detail_image)

        // Definisikan text pada setiap komponen
        tvName.text = name
        tvAddress.text = address
        tvNumber.text = number
        Picasso.get()
            .load(imageUrl)
            .into(ivImage)

        if(imageUrl == null) {
            ivImage.setImageResource(R.drawable.account_circle_24)
        }

        val btnBack = findViewById<TextView>(R.id.btn_detail_back)

        btnBack.setOnClickListener{
            finish()
        }

        // Panggil komponen button Update di layout
        val btnUpdate = findViewById<TextView>(R.id.btn_detail_update)

        // Arahkan menuju halaman update data
        btnUpdate.setOnClickListener{
            btnUpdate.isSelected != btnUpdate.isSelected
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("CONTACT", contact)
            startActivity(intent)
        }

        // Set up Firebase Cloud Storage
        storageReference = FirebaseStorage.getInstance().reference

        // Konfirmasi sebelum menghapus
        fun showDeleteConfirmationDialog() {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setMessage("Are you sure to delete this contact?")
                setPositiveButton("Yes") { dialog, which ->
                    val contact: Contact? = intent.getParcelableExtra("CONTACT")
                    contact?.let { // Memeriksa apakah contact tidak null

                        if(contact.imageUrl != null) {
                            val imageRef = storageReference.child(getFileNameFromUrl(contact.imageUrl))

                            imageRef.delete().addOnSuccessListener {
                                Log.i("UpdateActivity", "Success deleting image")
                            }.addOnFailureListener { e ->
                                Log.e("UpdateActivity", "Error deleting image", e)
                            }
                        }

                        // hapus contact
                        deleteViewModel.delete(it)

                        val intent = Intent(context, ListActivity::class.java)
                        intent.putExtra("message", "Item berhasil dihapus")
                        startActivity(intent)
                        finish()
                    }
                }
                setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        // Panggil komponen button Update di layout
        val btnDelete = findViewById<TextView>(R.id.btn_detail_delete)

        // Munculkan konfirmasi
        btnDelete.setOnClickListener {
            btnDelete.isSelected != btnDelete.isSelected
            showDeleteConfirmationDialog()
        }
    }

    private fun getFileNameFromUrl(url: String?): String {
        val uri = Uri.parse(url)
        val segments = uri.pathSegments
        return segments.lastOrNull()?.split("?")?.first() ?: ""
    }
}