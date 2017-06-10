package custom.sdk.com.thirdpartysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import custom.sdk.com.myfacebook.FBActivity;
import custom.sdk.com.myfacebook.FBShareCallbacks;
import custom.sdk.com.myfacebook.FaceBookCallbacks;
import custom.sdk.com.myfacebook.model.FBPostDetails;
import custom.sdk.com.myfacebook.model.FacebookUser;
import custom.sdk.com.myfacebook.MyFacebook;
import custom.sdk.com.mytwitter.MyTwitter;

public class MainActivity extends FBActivity implements FaceBookCallbacks, FBShareCallbacks {

    //MyFacebook facebook;
    MyTwitter twitter;

    MyFacebook facebook;

    FacebookUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.btn);
        Button b2 = (Button) findViewById(R.id.btn2);

        //twitter = new MyTwitter(MainActivity.this);
        //SocialSDK sdk = new SocialSDK(this, this);
        facebook = new MyFacebook(this, (FaceBookCallbacks)this, (FBShareCallbacks)this);



        user = new FacebookUser();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facebook.isLoggedIn()) {
                    facebook.fetchUserInformation(user);
                } else {
                    facebook.loginToFacebook(true);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facebook.isLoggedIn()) {
                    facebook.getTaggedPosts();
                } else {
                    facebook.loginToFacebook(true);
                }
            }
        });
    }

    @Override
    public void setTaggedPosts(JSONObject taggedPosts) {
        String s = "sad";
        s = s + "2";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.onFaceBookActivityListener(requestCode, resultCode, data);
    }

    @Override
    public void onFbShareSuccess(Sharer.Result result) {

    }

    @Override
    public void onFbShareError(FacebookException error) {

    }

    @Override
    public void onFbShareCancelled() {

    }

    @Override
    public void onFbLoginSuccess(LoginResult result) {

    }

    @Override
    public void onFbLoginError(FacebookException error) {

    }

    @Override
    public void onFbLoginCancelled() {

    }

    @Override
    public void onAppInviteSuccess(AppInviteDialog.Result result) {

    }

    @Override
    public void onAppInviteError(FacebookException e) {

    }

    @Override
    public void onFetchData() {

    }

    @Override
    public void onFetchCompleted() {

    }

    @Override
    public void onPostsFetched(JSONObject obj) {
        ArrayList<FBPostDetails> posts = new ArrayList<>();

        try {
            JSONArray data = obj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFbRetrieveJsonError(JSONException e) {

    }
}