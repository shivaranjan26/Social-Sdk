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

import custom.sdk.com.myfacebook.FBCustomShareCallbacks;
import custom.sdk.com.myfacebook.FBShareCallbacks;
import custom.sdk.com.myfacebook.FaceBookCallbacks;
import custom.sdk.com.myfacebook.MyFacebook;

public class MainActivity extends Activity implements FaceBookCallbacks, FBCustomShareCallbacks {

    MyFacebook facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebook = new MyFacebook(this, (FaceBookCallbacks) this, (FBCustomShareCallbacks) this);

        Button b = (Button) findViewById(R.id.btn);
        Button b2 = (Button) findViewById(R.id.btn2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facebook.isLoggedIn()) {
                    //LoginManager.getInstance().logOut();
                    facebook.shareApplicationInvite();
                } else {

                    facebook.loginToFacebook(false);
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.onFaceBookActivityListener(requestCode, resultCode, data);
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
    public void onFbRetrieveJsonError(JSONException e) {

    }

    @Override
    public void onFbCustomShareSuccess(Sharer.Result result) {

    }

    @Override
    public void onFbCustomShareError(FacebookException error) {

    }

    @Override
    public void onFbCustomShareCancelled() {

    }
}
