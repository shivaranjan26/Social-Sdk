package custom.sdk.com.socialsdk;

import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import custom.sdk.com.myfacebook.model.AlbumPhotos;
import custom.sdk.com.myfacebook.model.FBPostDetails;
import custom.sdk.com.myfacebook.model.FacebookUser;

/**
 * Created by Shiva on 09-06-2017.
 */

public abstract class SocialSDKActivity extends AppCompatActivity {

    void onFacebookLogin(LoginResult result, boolean isCancelled, FacebookException exception) {}
    void onFBUserInformationFetched(FacebookUser user) {}
    void onFetchErrors(JSONException exception) {}
    void onTaggedPostsFetched(ArrayList<FBPostDetails> postDetails) {}
    void onUserPhotosFetched(ArrayList<AlbumPhotos> postDetails) {}
    void onUserPostsFetched(ArrayList<FBPostDetails> postDetails) {}
    void onFetchCustomJSONResponse(JSONObject object) {}

    public void onContentPosted(Sharer.Result result) {}
    public void onContentCanceled() {}
    public void onContentPostingError(FacebookException error) {}
}
