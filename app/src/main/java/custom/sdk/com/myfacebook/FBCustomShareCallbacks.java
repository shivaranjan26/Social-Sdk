package custom.sdk.com.myfacebook;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;

/**
 * Created by Shiva on 29-05-2017.
 */

public interface FBCustomShareCallbacks {
    public void onFbCustomShareSuccess(Sharer.Result result);
    public void onFbCustomShareError(FacebookException error);
    public void onFbCustomShareCancelled();
}
