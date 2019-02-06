package epicture.epitech.eu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.net.URI


data class Auth (
    val access_token: String = "",
    val refresh_token: String = "",
    val account_id: String = "",
    val account_username: String = ""
)

class Epicture : AppCompatActivity() {
    companion object {
        const val URIUSER = "fesse";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getStringExtra(Epicture.URIUSER);
        val uri = "http://callback?" + user.substring(user.indexOf("#") + 1)
        val uri2 = Uri.parse(uri)
        var access_token = uri2.getQueryParameter("access_token")
        var refresh_token = uri2.getQueryParameter("refresh_token")
        Log.d("tag", user)
    }
}
