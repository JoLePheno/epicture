package epicture.epitech.eu

import kotlinx.android.synthetic.main.home_epicture.*
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.content.Intent
import okhttp3.*
import java.io.IOException
import android.R.attr.name
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import epicture.epitech.eu.R.id.imageView
import kotlinx.android.synthetic.main.image_list.*
import okhttp3.ResponseBody


data class Image(
    val link: String
)

class Home : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        const val URIUSER = "fesse";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getStringExtra(Epicture.URIUSER);
        Log.d("tag", user)
        val uriString = user.toString();
        val paramsString = uriString.substring(uriString.indexOf("#")+1);
        for (pair in paramsString) {
            Log.d("tag", pair.toString())
        }
        val intent = intent
        val username = intent.getStringExtra("username")
        val access_token = intent.getStringExtra("accessToken")
        val refresh_token = intent.getStringExtra("refreshToken")
        val id = intent.getStringExtra("id")
        setContentView(R.layout.home_epicture)
        viewManager = LinearLayoutManager(this)
        val client = OkHttpClient()
        val url = "https://api.imgur.com/3/account/$username/images/0"
        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", "Bearer $access_token")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("debug", "Request failed")
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.body().use { responseBody ->
                    if (response.isSuccessful) {
                        Log.d("debug", response.message())
                        //viewAdapter = HomeAdapter(Gson().fromJson(response.message(), Image::class.java))
                        recyclerView = HomeView.apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                        }
                    } else {
                        Log.d("Debug", response.code().toString())
                        Log.d("Debug", response.body().toString())
                    }
                }
            }
        })
    }
}

class HomeAdapter(private val myDataset: Image) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    class MyViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HomeAdapter.MyViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list, parent, false) as ImageView
        return MyViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("debug", myDataset.link)
    }

    override fun getItemCount() = 1
}