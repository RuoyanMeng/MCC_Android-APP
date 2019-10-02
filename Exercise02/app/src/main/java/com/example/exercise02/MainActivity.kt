package com.example.exercise02


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap.createBitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.FileProvider





class MainActivity : AppCompatActivity() {

    private var angle = 90.0f
    private val FROM_GALLERY = 1
    private val FROM_CAMERA = 2
    private var imageUri : Uri ? = null
    private var PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView.setRotation(0.0f)
        upload.setOnClickListener {
            showDialog()
        }

        rotate.setOnClickListener {

            rotate()
        }
    }



    private fun rotate(){

        when(angle){
            90.0f->{

                imageView.setRotation(90.0f)
                angle = angle + 90.0f
                Log.i("MCC", angle.toString())
            }
            180.0f->{

                imageView.setRotation(angle)
                angle = angle + 90.0f
                Log.i("MCC", angle.toString())
            }
            270.0f->{

                imageView.setRotation(angle)
                angle = 0.0f
                Log.i("MCC", angle.toString())
            }
            0.0f->{

                imageView.setRotation(angle)
                angle = angle + 90.0f
                Log.i("MCC", angle.toString())
            }

        }

    }

    fun showDialog() {

        val items = arrayOf("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Select Options")
            setItems(items) { dialog, which ->
                actions(items[which])
            }

//            setPositiveButton("OK", positiveButtonClick)
            show()
        }
    }



    fun actions(item : String){
        when(item){
            "Camera"->{
                //Toast.makeText(applicationContext, item + " is clicked", Toast.LENGTH_SHORT).show()
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    takePic()
                }
                else {
                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE)

                }
            }
            "Gallery"->{
                //Toast.makeText(applicationContext, item + " is clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, FROM_GALLERY)
                }
            }
            else->{


            }
        }
    }

    fun takePic(){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFile = File(externalCacheDir, timeStamp+".jpg")
        imageFile.createNewFile()
        imageUri = FileProvider.getUriForFile(this, "com.example.exercise02.fileProvider",imageFile)
        val imageIntent = Intent("android.media.action.IMAGE_CAPTURE")
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(imageIntent, FROM_CAMERA)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            FROM_GALLERY->{
                if (resultCode == Activity.RESULT_OK) {
                    imageView.setRotation(0.0f)
                    angle = 90.0f
                    imageView.setImageURI(data?.data)
                }
            }
            FROM_CAMERA->{
                //Log.i("MCC","a")
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri!!))
                    val matrix = Matrix()
                    matrix.postRotate(90.0f)
                    val scaledBitmap = createScaledBitmap(
                        bitmap,
                        bitmap.width,
                        bitmap.height,
                        true
                    )
                    val bitmap1 = createBitmap(scaledBitmap,
                        0,
                        0,
                        scaledBitmap.width,
                        scaledBitmap.height,
                        matrix,
                        true)

                    imageView.setRotation(0.0f)
                    angle = 90.0f
                    imageView!!.setImageBitmap(bitmap1)
                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        when(requestCode){
            PERMISSION_CODE->{
                if(grantResults.isNotEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    takePic()
                }else{
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}
