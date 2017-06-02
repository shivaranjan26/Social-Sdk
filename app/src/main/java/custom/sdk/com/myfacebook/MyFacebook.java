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

    public static List<String> permissionNeeds = Arrays.asList("public_profile",
            "email", "user_posts", "user_photos", "user_birthday",
            "user_friends", "read_custom_friendlists");

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
        LoginManager.getInstance().logInWithReadPermissions(context, MyFacebook.permissionNeeds);
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



    public void fetchUserAlbums(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        try {
                            JSONObject albums = new JSONObject(object.getString("albums"));
                            JSONArray data_array = albums.getJSONArray("data");
                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject _pubKey = data_array
                                        .getJSONObject(i);
                                String arrayfinal = _pubKey.getString("id");
                                albumsId.add(arrayfinal);
                            }
                            callbacks.onFetchCompleted();
                        } catch (JSONException E) {
                            callbacks.onFbRetrieveJsonError(E);
                        }

                    }

                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "albums");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void fetchAlbumPictures(ArrayList<String> Album_id_list, int albumPosition) {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(), "/" + Album_id_list.get(albumPosition)
                        + "/photos/", new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        try {
                            JSONArray data_array1 = object.getJSONArray("data");
                            for (int i = 0; i < data_array1.length(); i++) {
                                JSONObject _pubKey = data_array1
                                        .getJSONObject(i);
                                String picFinals = _pubKey.getString("picture");
                                albumPhotos.add(picFinals);
                            }
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,picture");
        parameters.putString("limit", ""+PIC_LIMIT);
        request.setParameters(parameters);
        request.executeAsync();
    }



    public void fetchUserFriends() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        JSONObject newresponse, totlfrndcount;
                        try {
                            newresponse = object
                                    .getJSONObject("friends");
                            JSONArray array = newresponse
                                    .getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject res = array.getJSONObject(i);
                                friendsList.add(res.getString("name"));
                            }
                            totlfrndcount = newresponse
                                    .getJSONObject("summary");
                            friendsCount = Integer.valueOf(totlfrndcount.getString("total_count"));
                            callbacks.onFetchCompleted();
                        } catch (JSONException e) {
                            callbacks.onFbRetrieveJsonError(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,friends,name");
        request.setParameters(parameters);
        request.executeAsync();
    }



    public void fetchUserDetails(){
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(token,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                if(object.has("name")) {
                                    faceBookName = object.getString("name");
                                }

                                if(object.has("email")) {
                                    fbEmailId=object.getString("email");
                                }

                                if(object.has("gender")) {
                                    gender=object.getString("gender");
                                }

                                if(object.has("id")) {
                                    fbId = object.getString("id");
                                }

                                if(object.has("birthday")) {
                                    birthday = object.getString("birthday");
                                }

                                if(object.has("location")) {
                                    JSONObject jsonobject_location = object.getJSONObject("location");
                                    if(jsonobject_location.has("name")) {
                                        location = jsonobject_location.getString("name");
                                    }
                                }

                                if(object.has("picture")) {
                                    fbProfilePicture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                }

                                if(object.has("cover")) {
                                    fbProfileCoverPicture = object.getJSONObject("cover").getString("source");
                                }
                                callbacks.onFetchCompleted();
                            } catch (JSONException e) {
                                callbacks.onFbRetrieveJsonError(e);
                            }

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, gender, cover, birthday, location, picture");
            request.setParameters(parameters);
            request.executeAsync();
        }
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

    public static final String FACEBOOK_POST_FIELDS = "reactions.limit(10000){id,link,name,type,profile_type,pic_square,can_post,picture},comments.limit(1000){user_likes,comment_count,comments.limit(10000){message,likes.limit(10000){id,name,link,username,profile_type,pic_square,can_post},comments.limit(10000){comments.limit(1000){user_likes,id,message,likes.limit(1000){name,id,link,username,profile_type,pic_square,can_post},like_count,created_time,from,comment_count,object,can_comment,can_like,can_remove,message_tags,parent,attachment},likes.limit(1000){username,id,profile_type,link,name,pic_square,can_post},message,like_count,user_likes,comment_count,object,created_time,from,id,can_like,can_comment,can_remove,parent,message_tags,attachment},user_likes,id,from,created_time,object,like_count,comment_count,can_like,can_comment,can_remove,attachment,message_tags,parent},likes.limit(10000){id,username,name,link,profile_type,pic_square,can_post},message,id,from,like_count,object,created_time,attachment,can_comment,can_like,message_tags,parent,can_remove},updated_time,created_time,from,width,images,id,name,icon,height,webp_images,link,can_delete,tags{created_time,id,name,tagging_user},name_tags";
    public static final String FACEBOOK_COMMENT_FIELDS = "comments.limit(1000){user_likes,comment_count,comments.limit(10000){message,likes.limit(10000){id,name,link,username,profile_type,pic_square,can_post},comments.limit(10000){comments.limit(1000){user_likes,id,message,likes.limit(1000){name,id,link,username,profile_type,pic_square,can_post},like_count,created_time,from,comment_count,object,can_comment,can_like,can_remove,message_tags,parent,attachment},likes.limit(1000){username,id,profile_type,link,name,pic_square,can_post},message,like_count,user_likes,comment_count,object,created_time,from,id,can_like,can_comment,can_remove,parent,message_tags,attachment},user_likes,id,from,created_time,object,like_count,comment_count,can_like,can_comment,can_remove,attachment,message_tags,parent},likes.limit(10000){id,username,name,link,profile_type,pic_square,can_post},message,id,from,like_count,object,created_time,attachment,can_comment,can_like,message_tags,parent,can_remove}";
    public static final String FACEBOOK_REACTION_FIELDS = "reactions.limit(10000){id,link,name,type,profile_type,pic_square,can_post,picture}";

    public void readFacebookUserWall(){
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            FacebookUtils.FB_POSTS = object.getJSONObject("posts").getJSONArray("data");
                            FacebookUtils.FB_POST_PREV_PAGE = object.getJSONObject("posts").getJSONObject("paging").getString("previous");
                            FacebookUtils.FB_POST_NEXT_PAGE = object.getJSONObject("posts").getJSONObject("paging").getString("next");
                            FacebookUtils.getReactionsFromPost("10210914012100909_10210122605276233");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,posts.limit(100){description,name,link,caption,full_picture,reactions.limit(100){name,pic},shares,from,to}");
        request.setParameters(parameters);
        request.executeAsync();





        /*Bundle params = new Bundle();
        params.putString("message", "This is a test message");
*//* make the API call *//*
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            *//* handle the result *//*
                    }
                }
        ).executeAsync();*/
    }

    public void readCommentsOfPosts(){
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            FacebookUtils.FB_POSTS_COMMENTS = object.getJSONObject("posts").getJSONArray("data");
                            List<ArrayList<String>> values = FacebookUtils.getFirstLevelCommentsFromPostPosition(6);

                            ArrayList<String> ids = values.get(3);
                            String id = ids.get(0);

                            String[] separated = id.split("~");
                            FacebookUtils.getSecondLevelCommentsFromPostId(separated[1], token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,posts.limit(100){comments.limit(100){comments.limit(100){id,message,reactions.limit(999){name,pic},from},message,from,reactions.limit(999){name,pic}}}");
        request.setParameters(parameters);
        request.executeAsync();
    }












    public String getFaceBookName(){
        return faceBookName;
    }
    public String getFacebookEmailId(){
        return fbEmailId;
    }
    public String getUserGender(){
        return gender;
    }
    public String getFacebookId(){
        return fbId;
    }
    public String getUserBirthday(){
        return birthday;
    }
    public String getUserLocation(){
        return location;
    }
    public String getUserDisplayPicture(){
        return fbProfilePicture;
    }
    public String getUserCoverPhoto(){
        return fbProfileCoverPicture;
    }
    public ArrayList<String> getAlbumIds(){
        return albumsId;
    }
    public ArrayList<String> getAlbumPhotos(){
        return albumPhotos;
    }

    public void onFaceBookActivityListener(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
