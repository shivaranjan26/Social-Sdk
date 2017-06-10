package custom.sdk.com.socialsdk;

import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import org.json.JSONException;

import java.util.ArrayList;

import custom.sdk.com.myfacebook.model.FBPostDetails;
import custom.sdk.com.myfacebook.model.FacebookUser;

/**
 * Created by Shiva on 09-06-2017.
 */

public abstract class SocialSDKActivity extends AppCompatActivity {

    void onFacebookLogin(LoginResult result, boolean isCancelled, FacebookException exception) {};
    void onFBUserInformationFetched(FacebookUser user) {};
    void onFetchErrors(JSONException exception) {};
    void onTaggedPostsFetched(ArrayList<FBPostDetails> postDetails) {};
}
