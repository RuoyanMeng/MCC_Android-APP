package com.example.exercise04

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage


class MainActivity : AppCompatActivity() {

    var message:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        upload.setOnClickListener {
            openGallery()
        }

        detect.setOnClickListener {
            textRecognition()

        }
    }


    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageView.setRotation(0.0f)

                    imageView.setImageURI(data?.data)

                }
            }
        }




    }

    private fun textRecognition(){
        val drawable = imageView.drawable as BitmapDrawable
        val bitmapT = drawable.bitmap
        val image = FirebaseVisionImage.fromBitmap(bitmapT)
        val detector = FirebaseVision.getInstance()
            .onDeviceTextRecognizer
        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->

                message = firebaseVisionText.text
                showText.setText(message)
            }
            .addOnFailureListener {
                // Task failed with an exception
            }



    }

}
