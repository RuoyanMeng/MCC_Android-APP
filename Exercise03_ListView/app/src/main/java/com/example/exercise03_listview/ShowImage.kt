package com.example.exercise03_listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ShowImage : AppCompatActivity() {
    companion object {
        const val EXTRA_PHOTO = "ShowImage.EXTRA_PHOTO"
        const val EXTRA_AUTHOR = "ShowImage.EXTRA_AUTHOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        val photo = intent.getStringExtra(EXTRA_PHOTO)
        val author = intent.getStringExtra(EXTRA_AUTHOR)

        getPhotoinfoView(photo,author)



    }

    private fun getPhotoinfoView(photo:String,author:String){
        Log.i("MCC","try get image details")

        Thread(Runnable {


            this@ShowImage.runOnUiThread(java.lang.Runnable {

                Log.i("MCC","detail shows ok")
                val imageView = findViewById<ImageView>(R.id.showImage)

                Picasso.get()
                    .load(photo)
                    .fit()
                    .into(imageView)

                val textView = findViewById<TextView>(R.id.showAuthor).apply {
                    text = author
                }
            })
        }).start()
    }

}