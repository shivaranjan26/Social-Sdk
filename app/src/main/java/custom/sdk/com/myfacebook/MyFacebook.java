package custom.sdk.com.myfacebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import custom.sdk.com.myfacebook.model.EmploymentDetails;
import custom.sdk.com.myfacebook.model.FacebookUser;

/**
 * Created by Shiva on 23-05-2017.
 */

public class MyFacebook {

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
    public JSONObject FB_TAGGED_POSTS ;

    //User User Shared Posts
    public JSONObject FB_USER_POSTS ;

    //User Albums
    public JSONObject FB_USER_ALBUM_NO_COMMENTS ;
    public JSONObject FB_CUSTOM_DATA_JSON ;

    //Custom JSON Object
    public JSONObject FB_USER_ALBUM_WITH_COMMENTS ;


    Activity context;

    AccessToken token;



    CallbackManager callbackManager;
    ShareDialog dialog;

    FaceBookCallbacks callbacks;
    FBCustomShareCallbacks customCallbacks;
    FBShareCallbacks shareCallbacks;

    FBActivity fbActivity;

    public static List<String> readPermissionNeeds = Arrays.asList(
            "public_profile",
            "email", "user_posts", "user_photos", "user_birthday",
            "user_friends", "read_custom_friendlists", "user_about_me", "user_education_history",
            "user_games_activity", "user_hometown", "user_likes", "user_location", "user_relationship_details",
            "user_relationships", "user_religion_politics", "user_status", "user_tagged_places",
            "user_videos", "user_website", "user_work_history");


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

        LoginManager.getInstance().logInWithReadPermissions(context, readPermissionNeeds);

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
        String applicationUrl = "";
        String imgUrl = "";

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




    public void fetchUserInformation(final FacebookUser user){
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if(object.has("about")){
                                user.setAboutMe(object.getString("about"));
                            }
                            if(object.has("birthday")){
                                user.setBirthday(object.getString("birthday"));
                            }
                            if(object.has("age_range")){
                                user.setAge(object.getString("age_range"));
                            }
                            if(object.has("first_name")){
                                user.setFirstName(object.getString("first_name"));
                            }
                            if(object.has("last_name")){
                                user.setLastName(object.getString("last_name"));
                            }
                            if(object.has("middle_name")){
                                user.setMiddleName(object.getString("middle_name"));
                            }
                            if(object.has("cover")){
                                user.setCoverPicUrl(object.getString("cover"));
                            }
                            if(object.has("picture")){
                                user.setProfilePicUrl(object.getString("picture"));
                            }
                            if(object.has("gender")){
                                user.setGender(object.getString("gender"));
                            }
                            if(object.has("hometown")){
                                user.setHomeTown(object.getJSONObject("hometown").getString("name"));
                            }
                            if(object.has("relationship_status")){
                                user.setRelationshipStatus(object.getString("relationship_status"));
                            }
                            if(object.has("religion")){
                                user.setReligion(object.getString("religion"));
                            }
                            if(object.has("timezone")){
                                user.setTimeZone(object.getString("timezone"));
                            }
                            if(object.has("education")){
                                ArrayList<String> education = new ArrayList<String>();
                                for (int i = 0; i < object.getJSONArray("education").length(); i++) {
                                    JSONObject edu = object.getJSONArray("education").getJSONObject(i);
                                    education.add(i, edu.getString("type") + "~" + edu.getJSONObject("school").getString("name"));
                                }
                                user.setEducation(education);
                            }
                            if(object.has("languages")) {
                                ArrayList<String> languages = new ArrayList<String>();
                                for (int i = 0; i < object.getJSONArray("languages").length(); i++) {
                                    languages.add(object.getJSONArray("languages").getJSONObject(i).getString("name"));
                                }
                                user.setLanguages(languages);
                            }
                            if(object.has("work")) {
                                ArrayList<EmploymentDetails> details = new ArrayList<EmploymentDetails>();
                                for (int i = 0; i < object.getJSONArray("work").length(); i++) {
                                    JSONObject emp = object.getJSONArray("work").getJSONObject(i);
                                    EmploymentDetails empDetail = new EmploymentDetails();
                                    if(emp.has("employer")) {
                                        empDetail.setEmployerName(emp.getJSONObject("employer").getString("name"));
                                    }
                                    if(emp.has("location")) {
                                        empDetail.setEmployerLocation(emp.getJSONObject("location").getString("name"));
                                    }
                                    if(emp.has("position")) {
                                        empDetail.setDesignation(emp.getJSONObject("position").getString("name"));
                                    }
                                    if(emp.has("start_date")) {
                                        empDetail.setStartDate(emp.getString("start_date"));
                                    }
                                    if(emp.has("end_date")) {
                                        empDetail.setEndDate(emp.getString("end_date"));
                                    }
                                    details.add(i, empDetail);
                                }
                                user.setWorkDetails(details);
                            }
                            if(object.has("friends")) {
                                user.setFriendsCount(object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));
                            }
                            if(object.has("website")) {
                                user.setWebsite(object.getJSONObject("website").getString("name"));
                            }
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,about,birthday,age_range,education,first_name,last_name,cover,picture,gender,hometown,languages,middle_name,relationship_status,religion,timezone,work,website,friends");
        request.setParameters(parameters);
        callbacks.onFetchData();
        request.executeAsync();
    }

    public void getFamilyInformation(final FacebookUser user) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if(object.has("family")) {
                                ArrayList<String> family = new ArrayList<String>();
                                JSONArray familyObj = object.getJSONObject("family").getJSONArray("data");
                                for (int i = 0; i < familyObj.length(); i++) {
                                    family.add(i, familyObj.getJSONObject(i).getString("name") + "~" +
                                            familyObj.getJSONObject(i).getString("relationship"));
                                }
                                user.setFamilyDetails(family);
                            }
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,family");
        request.setParameters(parameters);
        callbacks.onFetchData();
        request.executeAsync();
    }

    public void getUserLikedPages(final FacebookUser user) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if(object.has("likes")) {
                                ArrayList<String> pages = new ArrayList<String>();
                                JSONArray likesObj = object.getJSONObject("likes").getJSONArray("data");
                                for (int i = 0; i < likesObj.length(); i++) {
                                    String website = "";

                                    if(likesObj.getJSONObject(i).has("website")) {
                                        website = likesObj.getJSONObject(i).getString("website");
                                        pages.add(i, likesObj.getJSONObject(i).getString("name")
                                                + "~" + website);
                                    } else {
                                        pages.add(i, likesObj.getJSONObject(i).getString("name"));
                                    }
                                }
                                user.setLikedPages(pages);
                            }
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,likes.limit(9999){name,website}");
        request.setParameters(parameters);
        callbacks.onFetchData();
        request.executeAsync();
    }

    public void getTaggedPosts() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            fbActivity.setTaggedPosts(object.getJSONObject("tagged"));
                            callbacks.onPostsFetched(object.getJSONObject("tagged"));
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,tagged.limit(9999){name,from,message,story,picture,description,link,reactions.limit(9999){name,pic},comments.limit(9999){message,from,reactions.limit(9999){name,pic},comments.limit(9999){message,from,reactions.limit(9999){name,pic}}}}");
        request.setParameters(parameters);
        callbacks.onFetchData();
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
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,posts.limit(9999){name,description,caption,link,picture,reactions.limit(9999){name,pic},message,story,comments.limit(9999){from,message,reactions.limit(9999){name,pic},comments.limit(9999){from,message,reactions.limit(9999){name,pic}}}}");
        request.setParameters(parameters);
        callbacks.onFetchData();
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
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,albums.limit(9999){description,name,place,photos{name,picture,reactions.limit(9999){name}},reactions.limit(9999){name}}");
        request.setParameters(parameters);
        callbacks.onFetchData();
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
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,albums.limit(9999){description,name,place,photos{name,picture,reactions.limit(9999){name},comments.limit(9999){from,message,reactions.limit(9999){name}}},reactions.limit(9999){name},comments.limit(9999){from,message,reactions.limit(9999){name}}}");
        request.setParameters(parameters);
        callbacks.onFetchData();
        request.executeAsync();
    }

    public void getCustomJSONObject(String fields) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        FB_CUSTOM_DATA_JSON = object;
                        callbacks.onFetchCompleted();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", fields);
        request.setParameters(parameters);
        callbacks.onFetchData();
        request.executeAsync();
    }



    public void onFaceBookActivityListener(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
