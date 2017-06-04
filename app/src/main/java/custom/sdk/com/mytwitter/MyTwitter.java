package custom.sdk.com.mytwitter;

import android.app.Activity;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.models.Configuration;

import custom.sdk.com.thirdpartysdk.MainActivity;

/**
 * Created by Shiva on 04-06-2017.
 */

public class MyTwitter {

    Activity ctx;




    public MyTwitter(Activity context) {
        ctx = context;
        Twitter.initialize(ctx);
    }

    public MyTwitter(Activity context, String consumerKey, String secretKey) {
        ctx = context;
        TwitterConfig config = new TwitterConfig.Builder(ctx)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, secretKey))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    public void login(){

    }
}
