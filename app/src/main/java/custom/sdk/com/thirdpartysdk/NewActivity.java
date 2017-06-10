package custom.sdk.com.thirdpartysdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import custom.sdk.com.myfacebook.FaceBookUtils;
import custom.sdk.com.myfacebook.model.FBPostDetails;
import custom.sdk.com.myfacebook.model.FacebookUser;
import custom.sdk.com.socialsdk.SocialSDK;
import custom.sdk.com.socialsdk.SocialSDKActivity;

/**
 * Created by Shiva on 10-06-2017.
 */

public class NewActivity extends SocialSDKActivity {

    SocialSDK socialSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.btn);
        Button b2 = (Button) findViewById(R.id.btn2);

        socialSDK = new SocialSDK(this, this);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socialSDK.isLoggedInToFacebook()) {
                    socialSDK.fetchAlbumPhotos();
                } else {
                    socialSDK.LoginToFaceBook(true);
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socialSDK.isLoggedInToFacebook()) {
                    socialSDK.shareToFacebookPage(FaceBookUtils.getShareLinkContent("quote", "www.android.com", "#tag"), false);
                } else {
                    socialSDK.LoginToFaceBook(false);
                }
            }
        });
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        socialSDK.onSocialSDKActivityListener(requestCode, resultCode, data);
    }
}
