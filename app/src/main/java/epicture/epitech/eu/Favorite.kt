package epicture.epitech.eu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

class Favorite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.imgur.com/3/image/xSMsAJi/favorite"))
        startActivity(browserIntent)
        val intent = getIntent()
        val uri = intent.getData()
        if (uri != null) {
            val home = Intent(this@Login, Epicture::class.java)
            home.putExtra(Epicture.URIUSER, uri.toString())
            startActivity(home);
        }
    }
}