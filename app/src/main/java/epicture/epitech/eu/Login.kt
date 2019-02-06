package epicture.epitech.eu

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.content.Intent

class Login : AppCompatActivity() {

    val REQUEST_CODE_PICK_IMAGE = 1001

    private val AUTHORIZATION_URL = "https://api.imgur.com/oauth2/authorize"
    private val CLIENT_ID = "eb6bb0b2233a4b6"
    private val CLIENT_SECRET = "6f5f34a5c9aba37a4891f809588b631df2a1d0c5"

    private val accessToken: String? = null
    private val refreshToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_epicture)
        val login = findViewById(R.id.LoginButton) as Button

        login.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=eb6bb0b2233a4b6&response_type=token"))
                startActivity(browserIntent)
                val intent = getIntent()
                val uri = intent.getData()
                if (uri != null) {
                    val home = Intent(this@Login, Epicture::class.java)
                    home.putExtra(Epicture.URIUSER, uri.toString())
                    startActivity(home);
                }
            }
        })
    }
}
/*
class Epicture : AppCompatActivity
() {

    val REQUEST_CODE_PICK_IMAGE = 1001

    private val AUTHORIZATION_URL = "https://api.imgur.com/oauth2/authorize"
    private val CLIENT_ID = "eb6bb0b2233a4b6"
    private val CLIENT_SECRET = "6f5f34a5c9aba37a4891f809588b631df2a1d0c5"

    private val accessToken: String? = null
    private val refreshToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_epicture)
        val login = findViewById(R.id.LoginButton) as Button
        val action = intent.action

        login.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if (action == null || !action.equals(Intent.ACTION_VIEW)) {
                    Log.d(String(),"start Oauth2 authorize")

                    val uri = Uri.parse(AUTHORIZATION_URL).buildUpon()
                        .appendQueryParameter("client_id", CLIENT_ID)
                        //.appendQueryParameter("client_secret", CLIENT_SECRET)
                        .appendQueryParameter("response_type", "token")
                        .appendQueryParameter("state", "init")
                        .build();

                    val intent = Intent();
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else { // Now we have the token, can do the upload

                    Log.d(String(), "Got Access Token");

                    val uri = getIntent ().getData();
                    Log.d("Got imgur's access token", uri.toString());
                    val uriString = uri . toString ();
                    val paramsString = "http://callback?"+uriString.substring(uriString.indexOf("#")+1);
                    Log.d("tag", paramsString);
                    /*val params = URLEncodedUtils.parse (URI.create(paramsString), "utf-8");
                    Log.d("tag", Arrays.toString(params.toArray(params[0])));

                    for (NameValuePair pair : params) {
                        if (pair.getName().equals("access_token")) {
                            accessToken = pair.getValue();
                        } else if (pair.getName().equals("refresh_token")) {
                            refreshToken = pair.getValue();
                        }
                    }*/
                }
                Log.d("tag", "access_token = " + accessToken);
                Log.d("tag", "refresh_token = " + refreshToken)
            }
        })
    }
}
 */
