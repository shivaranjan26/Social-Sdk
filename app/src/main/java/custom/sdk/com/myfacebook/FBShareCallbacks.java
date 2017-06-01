package custom.sdk.com.myfacebook;

import com.facebook.FacebookException;
import com.facebook.share.Sharer;

/**
 * Created by Shiva on 29-05-2017.
 */

public interface FBShareCallbacks {
    public void onFbShareSuccess(Sharer.Result result);
    public void onFbShareError(FacebookException error);
    public void onFbShareCancelled();
}
