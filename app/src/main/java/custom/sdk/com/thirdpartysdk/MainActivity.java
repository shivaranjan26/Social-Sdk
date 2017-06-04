package custom.sdk.com.thirdpartysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONException;

import custom.sdk.com.myfacebook.FBShareCallbacks;
import custom.sdk.com.myfacebook.FaceBookCallbacks;
import custom.sdk.com.myfacebook.MyFacebook;
import custom.sdk.com.mytwitter.MyTwitter;

public class MainActivity extends Activity {

    //MyFacebook facebook;
    MyTwitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.btn);
        Button b2 = (Button) findViewById(R.id.btn2);

        twitter = new MyTwitter(MainActivity.this);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }






}