package com.example.storage_basics

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK)
        {
            val data: Intent? = it.data
            val imageUri = data?.data
            imageView.setImageURI(imageUri)

            val storageReference = FirebaseStorage.getInstance().reference
            val imageRef = storageReference.child("images/" + UUID.randomUUID().toString())
            val uploadTask = imageRef.putFile(imageUri!!)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                val downloadUrl = taskSnapshot.storage.downloadUrl
                Log.d("MAIN", downloadUrl.toString())
              }.addOnFailureListener {
                Log.d("MAIN", it.toString())
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        val imageFromURL: ImageView = findViewById(R.id.imageFromURL)
        val button: Button = findViewById(R.id.button)

        Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(imageFromURL)

        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }
    }
}