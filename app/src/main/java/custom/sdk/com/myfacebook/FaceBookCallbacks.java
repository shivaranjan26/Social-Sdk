package custom.sdk.com.myfacebook;

import android.content.Intent;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONException;

/**
 * Created by Shiva on 24-05-2017.
 */

public interface FaceBookCallbacks {

    public void onFbLoginSuccess(LoginResult result);
    public void onFbLoginError(FacebookException error);
    public void onFbLoginCancelled();
    public void onAppInviteSuccess(AppInviteDialog.Result result);
    public void onAppInviteError(FacebookException e);
    public void onFetchData();
    public void onFetchCompleted();
    public void onFbRetrieveJsonError(JSONException e);

}
