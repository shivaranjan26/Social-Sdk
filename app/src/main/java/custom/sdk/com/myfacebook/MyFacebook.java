package custom.sdk.com.myfacebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shiva on 23-05-2017.
 */

public class MyFacebook {

    Activity context;

    AccessToken token;

    String faceBookName;
    String fbEmailId;
    String gender;
    String fbId;
    String birthday;
    String location;
    String fbProfilePicture;
    String fbProfileCoverPicture;



    private ArrayList<String> albumsId = new ArrayList<String>();
    private ArrayList<String> albumPhotos = new ArrayList<String>();
    private ArrayList<String> friendsList = new ArrayList<String>();

    public static int PIC_LIMIT = 100;
    private int friendsCount = -1;



    CallbackManager callbackManager;
    ShareDialog dialog;

    FaceBookCallbacks callbacks;
    FBCustomShareCallbacks customCallbacks;
    FBShareCallbacks shareCallbacks;


    public MyFacebook(Activity ctContext, FaceBookCallbacks helper, FBShareCallbacks shareCallback) {
        context = ctContext;
        callbacks = helper;
        shareCallbacks = shareCallback;
    }

    public MyFacebook(Activity ctContext, FaceBookCallbacks helper, FBCustomShareCallbacks shareCallback) {
        context = ctContext;
        callbacks = helper;
        customCallbacks = shareCallback;
    }

    public void loginToFacebook(final boolean readOnly){
        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(context, FacebookUtils.readPermissionNeeds);
        if(!readOnly) {
            dialog = new ShareDialog(context);
            dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    shareCallbacks.onFbShareSuccess(result);
                }

                @Override
                public void onCancel() {
                    shareCallbacks.onFbShareCancelled();
                }

                @Override
                public void onError(FacebookException error) {
                    shareCallbacks.onFbShareError(error);
                }
            });
            LoginManager.getInstance().logInWithPublishPermissions(context, Arrays.asList("publish_actions"));
        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        setAccessToken(loginResult.getAccessToken());
                        callbacks.onFbLoginSuccess(loginResult);
                    }

                    @Override
                    public void onCancel()
                    {
                        callbacks.onFbLoginCancelled();
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {
                        callbacks.onFbLoginError(exception);
                    }
                });
    }



    public void setAccessToken(AccessToken accessToken) {
        token = accessToken;
    }



    public AccessToken getAccessToken(){
        return token;
    }



    public boolean isLoggedIn() {
        token = AccessToken.getCurrentAccessToken();
        return token != null;
    }



    public void publishLinkToFacebook(String quote, String link, String hashTag){
        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(quote)
                    .setContentUrl(Uri.parse(link))
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();
            dialog.show(content);
        } else {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(quote)
                    .setContentUrl(Uri.parse(link))
                    .build();
            dialog.show(content);
        }
    }



    public ShareLinkContent getShareLinkContent(String quote, String link, String hashTag) {
        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(quote)
                    .setContentUrl(Uri.parse(link))
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();
            return content;
        } else {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(quote)
                    .setContentUrl(Uri.parse(link))
                    .build();
            return content;
        }
    }



    public void publishPhotoToFacebook(Bitmap image, String hashTag) {
        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            dialog.show(content);
        } else {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            dialog.show(content);
        }
    }



    public SharePhotoContent getSharePhotoContent(Bitmap image, String hashTag) {
        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();
            return content;
        } else {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            return content;
        }
    }



    public void publishPhotosToFacebook(List<Bitmap> images, String hashTag) {
        SharePhoto photo1 = new SharePhoto.Builder()
                .setBitmap(images.get(0))
                .build();
        SharePhoto photo2 = new SharePhoto.Builder()
                .setBitmap(images.get(1))
                .build();
        SharePhoto photo3 = new SharePhoto.Builder()
                .setBitmap(images.get(2))
                .build();
        SharePhoto photo4 = new SharePhoto.Builder()
                .setBitmap(images.get(3))
                .build();
        SharePhoto photo5 = new SharePhoto.Builder()
                .setBitmap(images.get(4))
                .build();
        SharePhoto photo6 = new SharePhoto.Builder()
                .setBitmap(images.get(5))
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareMediaContent content = new ShareMediaContent.Builder()
                    .addMedium(photo1).addMedium(photo2).addMedium(photo3).addMedium(photo4).addMedium(photo5).addMedium(photo6)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();
            dialog.show(content);
        } else {
            ShareMediaContent content = new ShareMediaContent.Builder()
                    .addMedium(photo1).addMedium(photo2).addMedium(photo3).addMedium(photo4).addMedium(photo5).addMedium(photo6)
                    .build();
            dialog.show(content);
        }
    }



    public ShareMediaContent getShareMediaContent(List<Bitmap> images, String hashTag) {
        SharePhoto photo1 = new SharePhoto.Builder()
                .setBitmap(images.get(0))
                .build();
        SharePhoto photo2 = new SharePhoto.Builder()
                .setBitmap(images.get(1))
                .build();
        SharePhoto photo3 = new SharePhoto.Builder()
                .setBitmap(images.get(2))
                .build();
        SharePhoto photo4 = new SharePhoto.Builder()
                .setBitmap(images.get(3))
                .build();
        SharePhoto photo5 = new SharePhoto.Builder()
                .setBitmap(images.get(4))
                .build();
        SharePhoto photo6 = new SharePhoto.Builder()
                .setBitmap(images.get(5))
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareMediaContent content = new ShareMediaContent.Builder()
                    .addMedium(photo1).addMedium(photo2).addMedium(photo3).addMedium(photo4).addMedium(photo5).addMedium(photo6)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();
            return content;
        } else {
            ShareMediaContent content = new ShareMediaContent.Builder()
                    .addMedium(photo1).addMedium(photo2).addMedium(photo3).addMedium(photo4).addMedium(photo5).addMedium(photo6)
                    .build();
            return content;
        }
    }



    public void publishPhotoWithLinkToFacebook(Bitmap image, String link, String hashTag) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .addPhoto(photo)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            dialog.show(content);
        } else {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .addPhoto(photo)
                    .build();

            dialog.show(content);
        }
    }



    public SharePhotoContent getSharePhotoContentWithLink(Bitmap image, String link, String hashTag) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .addPhoto(photo)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            return content;
        } else {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .addPhoto(photo)
                    .build();

            return content;
        }
    }



    public void publishVideoToFacebook(Uri uri, String hashTag) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(uri)
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            dialog.show(content);
        } else {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .build();

            dialog.show(content);
        }
    }



    public ShareVideoContent getShareVideoContent(Uri uri, String hashTag) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(uri)
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            return content;
        } else {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .build();

            return content;
        }
    }



    public void publishVideoWithLinkToFacebook(Uri uri, String link, String hashTag) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(uri)
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .setVideo(video)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            dialog.show(content);
        } else {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .setVideo(video)
                    .build();

            dialog.show(content);
        }
    }



    public ShareVideoContent getShareVideoContentWithLink(Uri uri, String link, String hashTag) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(uri)
                .build();

        if(hashTag != null || !hashTag.trim().equalsIgnoreCase("")) {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .setVideo(video)
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag(hashTag)
                            .build())
                    .build();

            return content;
        } else {
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .setVideo(video)
                    .build();

            return content;
        }
    }



    public void shareContentUsingCustomInterface(ShareLinkContent content){
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                customCallbacks.onFbCustomShareSuccess(result);
            }

            @Override
            public void onCancel() {
                customCallbacks.onFbCustomShareCancelled();
            }

            @Override
            public void onError(FacebookException error) {
                customCallbacks.onFbCustomShareError(error);
            }
        });
    }



    public void shareContentUsingCustomInterface(SharePhotoContent content){
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                customCallbacks.onFbCustomShareSuccess(result);
            }

            @Override
            public void onCancel() {
                customCallbacks.onFbCustomShareCancelled();
            }

            @Override
            public void onError(FacebookException error) {
                customCallbacks.onFbCustomShareError(error);
            }
        });
    }



    public void shareContentUsingCustomInterface(ShareMediaContent content){
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                customCallbacks.onFbCustomShareSuccess(result);
            }

            @Override
            public void onCancel() {
                customCallbacks.onFbCustomShareCancelled();
            }

            @Override
            public void onError(FacebookException error) {
                customCallbacks.onFbCustomShareError(error);
            }
        });
    }



    public void shareContentUsingCustomInterface(ShareVideoContent content){
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                customCallbacks.onFbCustomShareSuccess(result);
            }

            @Override
            public void onCancel() {
                customCallbacks.onFbCustomShareCancelled();
            }

            @Override
            public void onError(FacebookException error) {
                customCallbacks.onFbCustomShareError(error);
            }
        });
    }



    public void shareApplicationInvite() {
        String applicationUrl = "https://fb.me/767616443419771";
        String imgUrl = "https://mi-od-live-s.legocdn.com/r/www/r/catalogs/-/media/catalogs/characters/lbm%20characters/primary/70900_1to1_batman_360_480.png?l.r2=1668006940";

        AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(applicationUrl).setPreviewImageUrl(imgUrl)
                .build();

        AppInviteDialog appInviteDialog = new AppInviteDialog(context);
        appInviteDialog.registerCallback(callbackManager,
                new FacebookCallback<AppInviteDialog.Result>() {
                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        callbacks.onAppInviteSuccess(result);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                        callbacks.onAppInviteError(e);
                    }
                });

        appInviteDialog.show(content);
    }




    public void getUserInformation(){
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if(object.has("about")){
                                FB_USER_ABOUT = object.getString("about");
                            }
                            if(object.has("birthday")){
                                FB_USER_BIRTHDAY = object.getString("birthday");
                            }
                            if(object.has("age_range")){
                                FB_USER_AGE = object.getString("age_range");
                            }
                            if(object.has("first_name")){
                                FB_USER_FIRST_NAME = object.getString("first_name");
                            }
                            if(object.has("last_name")){
                                FB_USER_LAST_NAME = object.getString("last_name");
                            }
                            if(object.has("middle_name")){
                                FB_USER_MIDDLE_NAME = object.getString("middle_name");
                            }
                            if(object.has("cover")){
                                FB_USER_COVER_PIC = object.getString("cover");
                            }
                            if(object.has("picture")){
                                FB_USER_PROFILE_PIC = object.getString("picture");
                            }
                            if(object.has("gender")){
                                FB_USER_GENDER = object.getString("gender");
                            }
                            if(object.has("hometown")){
                                FB_USER_HOMETOWN = object.getJSONObject("hometown").getString("name");
                            }
                            if(object.has("relationship_status")){
                                FB_USER_RELATIONSHIP = object.getString("relationship_status");
                            }
                            if(object.has("religion")){
                                FB_USER_RELIGION = object.getString("religion");
                            }
                            if(object.has("timezone")){
                                FB_USER_TIMEZONE = object.getString("timezone");
                            }
                            if(object.has("education")){
                                FB_USER_EDUCATION = object.getJSONArray("education");
                            }
                            if(object.has("languages")) {
                                for (int i = 0; i < object.getJSONArray("languages").length(); i++) {
                                    if(FB_USER_KNOWN_LANGUAGES.equals("")) {
                                        FB_USER_KNOWN_LANGUAGES = object.getJSONArray("languages").getJSONObject(i).getString("name");
                                    } else {
                                        FB_USER_KNOWN_LANGUAGES = FB_USER_KNOWN_LANGUAGES +
                                                "~" + object.getJSONArray("languages").getJSONObject(i).getString("name");
                                    }
                                }
                            }
                            if(object.has("work")) {
                                FB_USER_WORK = object.getJSONArray("work");
                            }
                            if(object.has("friends")) {
                                FB_USER_FRIENDS_COUNT = object.getJSONObject("friends").getJSONObject("summary").getString("total_count");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,about,birthday,age_range,education,first_name,last_name,cover,picture,gender,hometown,languages,middle_name,relationship_status,religion,timezone,work,website,friends");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getFamilyInformation() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if(object.has("family")) {
                                JSONArray familyObj = object.getJSONObject("family").getJSONArray("data");
                                for (int i = 0; i < familyObj.length(); i++) {
                                    FB_USER_FAMILY.add(i, familyObj.getJSONObject(i).getString("name") + "~" +
                                            familyObj.getJSONObject(i).getString("relationship"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        FB_USER_TIMEZONE = "Asas";
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,family");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getUserLikedPages() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if(object.has("likes")) {
                                JSONArray likesObj = object.getJSONObject("likes").getJSONArray("data");
                                for (int i = 0; i < likesObj.length(); i++) {
                                    String website = "";

                                    if(likesObj.getJSONObject(i).has("website")) {
                                        website = likesObj.getJSONObject(i).getString("website");
                                        FB_USER_PAGES_LIKED.add(i, likesObj.getJSONObject(i).getString("name")
                                                + "~" + website);
                                    } else {
                                        FB_USER_PAGES_LIKED.add(i, likesObj.getJSONObject(i).getString("name"));
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,likes.limit(9999){name,website}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getTaggedPosts() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            FB_TAGGED_POSTS = object.getJSONObject("tagged");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,tagged.limit(9999){name,from,message,story,picture,description,link,reactions.limit(9999){name,pic},comments.limit(9999){message,from,reactions.limit(9999){name,pic},comments.limit(9999){message,from,reactions.limit(9999){name,pic}}}}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getUserSharedPosts() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            FB_USER_POSTS = object.getJSONObject("posts");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,posts.limit(9999){name,description,caption,link,picture,reactions.limit(9999){name,pic},message,story,comments.limit(9999){from,message,reactions.limit(9999){name,pic},comments.limit(9999){from,message,reactions.limit(9999){name,pic}}}}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getAlbumPhotosWithoutComments() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            FB_USER_ALBUM_NO_COMMENTS = object.getJSONObject("albums");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,albums.limit(9999){description,name,place,photos{name,picture,reactions.limit(9999){name}},reactions.limit(9999){name}}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getAlbumPhotosWithComments() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            FB_USER_ALBUM_WITH_COMMENTS = object.getJSONObject("albums");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,albums.limit(9999){description,name,place,photos{name,picture,reactions.limit(9999){name},comments.limit(9999){from,message,reactions.limit(9999){name}}},reactions.limit(9999){name},comments.limit(9999){from,message,reactions.limit(9999){name}}}");
        request.setParameters(parameters);
        request.executeAsync();
    }







    //User Info
    public static String FB_USER_FIRST_NAME = "";
    public static String FB_USER_LAST_NAME = "";
    public static String FB_USER_MIDDLE_NAME = "";
    public static String FB_USER_ABOUT = "";
    public static String FB_USER_BIRTHDAY = "";
    public static String FB_USER_AGE = "";
    public static String FB_USER_COVER_PIC = "";
    public static String FB_USER_PROFILE_PIC = "";
    public static String FB_USER_GENDER = "";
    public static String FB_USER_HOMETOWN = "";
    public static String FB_USER_KNOWN_LANGUAGES = "";
    public static String FB_USER_RELATIONSHIP = "";
    public static String FB_USER_RELIGION = "";
    public static String FB_USER_TIMEZONE = "";
    public static String FB_USER_WEBSITE = "";
    public static String FB_USER_FRIENDS_COUNT = "";
    public static JSONArray FB_USER_EDUCATION ;
    public static JSONArray FB_USER_WORK;


    //User Family Info
    public static ArrayList<String> FB_USER_FAMILY = new ArrayList<>();

    //User Page Likes
    public static ArrayList<String> FB_USER_PAGES_LIKED = new ArrayList<>();

    //User Tagged Posts
    public static JSONObject FB_TAGGED_POSTS ;

    //User User Shared Posts
    public static JSONObject FB_USER_POSTS ;

    //User Albums
    public static JSONObject FB_USER_ALBUM_NO_COMMENTS ;
    public static JSONObject FB_USER_ALBUM_WITH_COMMENTS ;

    public void onFaceBookActivityListener(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
