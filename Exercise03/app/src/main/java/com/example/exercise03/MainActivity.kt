package com.example.exercise03

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException




class MainActivity : AppCompatActivity() {

    val okHttpClient = OkHttpClient()
    var arrayList_details: ArrayList<Model> = ArrayList();
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://www.mocky.io/v2/59a94ceb100000200c3e0a78"
        var message = editText.text.toString()

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                message = s.toString()
            }
        })





        button.setOnClickListener {
            Log.i("MCC",message)
            getRepos(message)

        }

    }


    fun getRepos(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        // 建立Call
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.i("MCC", "fail")
            }

            override fun onResponse(call: Call?, response: Response?) {
                var str_response = response?.body()!!.string()
                Log.i("MCC", str_response)
                //creating json array
                val jsonarray = JSONArray(str_response)

                var size: Int = jsonarray.length()
                for (i in 0..size - 1) {
                    var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                    var model: Model = Model();
                    model.photo = json_objectdetail.getString("photo")
                    model.author = json_objectdetail.getString("author")
                    arrayList_details.add(model)
                    //Log.i("MCC", arrayList_details[i].photo)
                }
            }
        })

        Thread(Runnable {
            // performing some dummy time taking operation
            var i=0;
            while(i<Int.MAX_VALUE){
                i++
            }

            // try to touch View of UI thread
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                Log.i("MCC","image list ok")
                val layoutManager = LinearLayoutManager(this)
                rv_images.layoutManager = layoutManager
                imageGalleryAdapter = ImageGalleryAdapter(this, arrayList_details)
                rv_images.adapter = imageGalleryAdapter
            })
        }).start()


    }



    private inner class ImageGalleryAdapter(
        val context: Context,
        val arrayListDetails: ArrayList<Model>
    ) :  RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val photoView = inflater.inflate(R.layout.item_image, parent, false)
            return MyViewHolder(photoView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val photoInfo = arrayListDetails[position]
            val imageView = holder.photoImageView
            Log.i("MCC","get image ok")
            Picasso.get()
                .load(photoInfo.photo)
                .fit()
                .tag(context)
                .into(imageView)

            val textView = holder.photoAuthorView
            textView.text = photoInfo.author
        }

        override fun getItemCount(): Int {
            return arrayListDetails.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            var photoImageView: ImageView = itemView.findViewById(R.id.rv_photo)
            var photoAuthorView: TextView = itemView.findViewById(R.id.rv_author)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View) {

                val position = adapterPosition
                Log.i("MCC",position.toString())
                if (position != RecyclerView.NO_POSITION) {
                    val photoInfo = arrayListDetails.get(position)
                    Log.i("MCC",photoInfo.photo)
                    val intent = Intent(context, ShowImage::class.java).apply {
                        putExtra(ShowImage.EXTRA_PHOTO,photoInfo.photo)
                        putExtra(ShowImage.EXTRA_AUTHOR,photoInfo.author)
                    }
                    startActivity(intent)
                }
            }
        }

    }



}


