package custom.sdk.com.socialsdk;

import android.app.Activity;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import custom.sdk.com.myfacebook.FaceBookUtils;
import custom.sdk.com.myfacebook.model.EmploymentDetails;
import custom.sdk.com.myfacebook.model.FBFirstLevelComments;
import custom.sdk.com.myfacebook.model.FBPostDetails;
import custom.sdk.com.myfacebook.model.FBSecondLevelComments;
import custom.sdk.com.myfacebook.model.FacebookUser;

/**
 * Created by Shiva on 09-06-2017.
 */

public class SocialSDK {

    SocialSDKActivity callbacks;
    Activity context;

    //Facebook Variables
    CallbackManager callbackManager;
    AccessToken fbAccessToken;

    public SocialSDK(Activity activity, SocialSDKActivity socialSdk) {
        context = activity;
        callbacks = socialSdk;
    }

    public void LoginToFaceBook(boolean readlOnly) {
        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(context, SocialSDKUtils.readPermissionNeeds);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        fbAccessToken = loginResult.getAccessToken();
                        callbacks.onFacebookLogin(loginResult, false, null);
                    }

                    @Override
                    public void onCancel()
                    {
                        callbacks.onFacebookLogin(null, true, null);
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {
                        callbacks.onFacebookLogin(null, false, exception);
                    }
                });
    }

    public AccessToken getFacebookAccessToken(){
        return fbAccessToken;
    }

    public boolean isLoggedInToFacebook() {
        return fbAccessToken != null;
    }

    public void fetchFBUserInfo(final FacebookUser user){
        GraphRequest request = GraphRequest.newMeRequest(
                fbAccessToken,
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
                            if(object.has("family")) {
                                ArrayList<String> family = new ArrayList<String>();
                                JSONArray familyObj = object.getJSONObject("family").getJSONArray("data");
                                for (int i = 0; i < familyObj.length(); i++) {
                                    family.add(i, familyObj.getJSONObject(i).getString("name") + "~" +
                                            familyObj.getJSONObject(i).getString("relationship"));
                                }
                                user.setFamilyDetails(family);
                            }
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
                            callbacks.onFBUserInformationFetched(user);
                        } catch (JSONException e) {
                            callbacks.onFetchErrors(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,family,likes.limit(9999){name,website},about,birthday,age_range,education,first_name,last_name,cover,picture,gender,hometown,languages,middle_name,relationship_status,religion,timezone,work,website,friends");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void fetchFBTaggedPosts() {
        final GraphRequest request = GraphRequest.newMeRequest(
                fbAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            ArrayList<FBPostDetails> postDetails = new ArrayList<FBPostDetails>();
                            JSONObject postObj = object.getJSONObject("tagged");

                            JSONArray postArr = postObj.getJSONArray("data");

                            for (int i = 0; i < postArr.length(); i++) {
                                FBPostDetails posts = new FBPostDetails();
                                JSONObject post = postArr.getJSONObject(i);
                                if(post.has("name")) {
                                    posts.setPostName(post.getString("name"));
                                }
                                if(post.has("description")) {
                                    posts.setDescription(post.getString("description"));
                                }
                                if(post.has("from")) {
                                    posts.setFromName(post.getJSONObject("from").getString("name"));
                                }
                                if(post.has("message")) {
                                    posts.setMessage(post.getString("message"));
                                }
                                if(post.has("story")) {
                                    posts.setStory(post.getString("story"));
                                }
                                if(post.has("picture")) {
                                    posts.setPicture(post.getString("picture"));
                                }
                                if(post.has("link")) {
                                    posts.setLink(post.getString("link"));
                                }
                                if(post.has("reactions")) {
                                    JSONArray reactionArr = post.getJSONObject("reactions").getJSONArray("data");
                                    ArrayList<String> reactions = new ArrayList<String>();
                                    for (int j = 0; j < reactionArr.length(); j++) {
                                        reactions.add(j, reactionArr.getJSONObject(j).getString("name"));
                                    }
                                    posts.setReactions(reactions);
                                }
                                if(post.has("comments")) {
                                    ArrayList<FBFirstLevelComments> secPostDetails = new ArrayList<FBFirstLevelComments>();
                                    JSONArray commentsArr = post.getJSONObject("comments").getJSONArray("data");
                                    for (int k = 0; k < commentsArr.length(); k++) {
                                        FBFirstLevelComments secPost = new FBFirstLevelComments();
                                        JSONObject com = commentsArr.getJSONObject(k);
                                        secPost.setFromName(com.getJSONObject("from").getString("name"));
                                        secPost.setMessage(com.getString("message"));

                                        if(com.has("comments")) {
                                            secPost.setComments(FaceBookUtils.getSecondLevelComments(com.getJSONObject("comments").getJSONArray("data")));
                                        }

                                        if(com.has("reactions")) {
                                            ArrayList<String> secLikes = new ArrayList<String>();
                                            for (int l = 0; l < com.getJSONObject("reactions").getJSONArray("data").length(); l++) {
                                                secLikes.add(l, com.getJSONObject("reactions").getJSONArray("data").getJSONObject(l)
                                                        .getString("name"));
                                            }
                                            secPost.setReactions(secLikes);
                                        }
                                        secPostDetails.add(k, secPost);
                                    }
                                    posts.setFirstLevelComments(secPostDetails);
                                }

                                postDetails.add(i, posts);
                            }
                            callbacks.onTaggedPostsFetched(postDetails);
                        } catch (JSONException e) {
                            callbacks.onFetchErrors(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,tagged.limit(9999){name,from,message,story,picture,description,link,reactions.limit(9999){name,pic},comments.limit(9999){message,from,reactions.limit(9999){name,pic},comments.limit(9999){message,from,reactions.limit(9999){name,pic}}}}");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void fetchFBUserSharedPosts() {
        GraphRequest request = GraphRequest.newMeRequest(
                fbAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            ArrayList<FBPostDetails> postDetails = new ArrayList<FBPostDetails>();
                            JSONObject postObj = object.getJSONObject("posts");

                            JSONArray postArr = postObj.getJSONArray("data");

                            for (int i = 0; i < postArr.length(); i++) {
                                FBPostDetails posts = new FBPostDetails();
                                JSONObject post = postArr.getJSONObject(i);
                                if(post.has("name")) {
                                    posts.setPostName(post.getString("name"));
                                }
                                if(post.has("description")) {
                                    posts.setDescription(post.getString("description"));
                                }
                                if(post.has("from")) {
                                    posts.setFromName(post.getJSONObject("from").getString("name"));
                                }
                                if(post.has("message")) {
                                    posts.setMessage(post.getString("message"));
                                }
                                if(post.has("story")) {
                                    posts.setStory(post.getString("story"));
                                }
                                if(post.has("picture")) {
                                    posts.setPicture(post.getString("picture"));
                                }
                                if(post.has("link")) {
                                    posts.setLink(post.getString("link"));
                                }
                                if(post.has("reactions")) {
                                    JSONArray reactionArr = post.getJSONObject("reactions").getJSONArray("data");
                                    ArrayList<String> reactions = new ArrayList<String>();
                                    for (int j = 0; j < reactionArr.length(); j++) {
                                        reactions.add(j, reactionArr.getJSONObject(j).getString("name"));
                                    }
                                    posts.setReactions(reactions);
                                }
                                if(post.has("comments")) {
                                    ArrayList<FBFirstLevelComments> secPostDetails = new ArrayList<FBFirstLevelComments>();
                                    JSONArray commentsArr = post.getJSONObject("comments").getJSONArray("data");
                                    for (int k = 0; k < commentsArr.length(); k++) {
                                        FBFirstLevelComments secPost = new FBFirstLevelComments();
                                        JSONObject com = commentsArr.getJSONObject(k);
                                        secPost.setFromName(com.getJSONObject("from").getString("name"));
                                        secPost.setMessage(com.getString("message"));

                                        if(com.has("comments")) {
                                            secPost.setComments(FaceBookUtils.getSecondLevelComments(com.getJSONObject("comments").getJSONArray("data")));
                                        }

                                        if(com.has("reactions")) {
                                            ArrayList<String> secLikes = new ArrayList<String>();
                                            for (int l = 0; l < com.getJSONObject("reactions").getJSONArray("data").length(); l++) {
                                                secLikes.add(l, com.getJSONObject("reactions").getJSONArray("data").getJSONObject(l)
                                                        .getString("name"));
                                            }
                                            secPost.setReactions(secLikes);
                                        }
                                        secPostDetails.add(k, secPost);
                                    }
                                    posts.setFirstLevelComments(secPostDetails);
                                }

                                postDetails.add(i, posts);
                            }
                            callbacks.onTaggedPostsFetched(postDetails);
                        } catch (JSONException e) {
                            callbacks.onFetchErrors(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,posts.limit(9999){name,description,caption,link,picture,reactions.limit(9999){name,pic},message,story,comments.limit(9999){from,message,reactions.limit(9999){name,pic},comments.limit(9999){from,message,reactions.limit(9999){name,pic}}}}");
        request.setParameters(parameters);
        request.executeAsync();
    }



































    public void onSocialSDKActivityListener(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
