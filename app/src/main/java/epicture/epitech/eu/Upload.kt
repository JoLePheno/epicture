package epicture.epitech.eu

import android.Manifest
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import kotlinx.android.synthetic.main.upload_epicture.*
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException


class Upload : Activity() {
    private var BitmapString: String = ""
    private var title: String = ""
    private var description: String = ""
    private var access_token: String = ""
    private val GALLERY = 1
    private val CAMERA = 2
    private val CAMERA_REQUEST_CODE = 0
    private var type = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_epicture)
        retrieveImageInfo()
    }

    override fun onStart() {
        super.onStart()
        SendButton.setOnClickListener() {
            title = TitleField.text.toString()
            description = DescriptionField.text.toString()
            var body = JSONObject()
            body.put("image", BitmapString)
            body.put("title", title)
            body.put("description", description)
            body.put("type", "base64")
            val url = "https://api.imgur.com/3/image"
            Log.d("Debug", body.toString())
            url.httpPost()
                .header(Pair("Authorization", "Bearer $access_token"), Pair("content-type", "application/json"))
                .body(body.toString())
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.d("debug", ex.toString())
                            Toast.makeText(this@Upload, "Failed to post image!", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            val data = result.get()
                            Log.d("Debug", data)
                            Toast.makeText(this@Upload, "Post Send!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
        if (type != -1) {
            when (type) {
                1 -> choosePhotoFromGallary()
                2 -> takePhotoFromCamera()
            }
            type = -1
        }
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    Toast.makeText(this@Upload, "Image Saved!", Toast.LENGTH_SHORT).show()
                    Log.d("REQ", "Sending Post request")
                    ImageDisplayer.setImageBitmap(bitmap)
                    BitmapString = bitmapToBase64(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@Upload, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            ImageDisplayer.setImageBitmap(thumbnail)
            BitmapString = bitmapToBase64(thumbnail)
            Log.d("REQ", "Sending post request")
            Toast.makeText(this@Upload, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun grantPermission(Permission: String) : Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Permission)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Permission)) {
                Log.d("PERMISSION", "Show to user explanation permission")
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Permission),
                    CAMERA_REQUEST_CODE)
                return (CAMERA_REQUEST_CODE == 1)
            }
        } else {
            return true
        }
        return false
    }

    fun choosePhotoFromGallary() {
        if (!grantPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this@Upload, "You must grant access to gallery to take a picture", Toast.LENGTH_SHORT).show()
            return
        }
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        if (!grantPermission(Manifest.permission.CAMERA)) {
            Toast.makeText(this@Upload, "You must grant access to camera to take a picture", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("PERMISSION", "accessing camera")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun base64ToBitmap(b64: String): Bitmap {
        val imageAsBytes = Base64.decode(b64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

    fun retrieveImageInfo() {
        type = intent.getIntExtra("type", -1)
        access_token = intent.getStringExtra("access_token")
    }
}

