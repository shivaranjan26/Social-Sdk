package custom.sdk.com.myfacebook;

import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import custom.sdk.com.myfacebook.model.FBFirstLevelComments;
import custom.sdk.com.myfacebook.model.FBSecondLevelComments;

/**
 * Created by Shiva on 10-06-2017.
 */

public class FaceBookUtils {

    public static ArrayList<FBSecondLevelComments> getSecondLevelComments(JSONArray jsonArray) throws JSONException {
        ArrayList<FBSecondLevelComments> postDetails = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            FBSecondLevelComments secPost = new FBSecondLevelComments();
            JSONObject com = jsonArray.getJSONObject(i);
            secPost.setFromName(com.getJSONObject("from").getString("name"));
            secPost.setMessage(com.getString("message"));

            if(com.has("reactions")) {
                ArrayList<String> secLikes = new ArrayList<String>();
                for (int l = 0; l < com.getJSONObject("reactions").getJSONArray("data").length(); l++) {
                    secLikes.add(l, com.getJSONObject("reactions").getJSONArray("data").getJSONObject(l)
                            .getString("name"));
                }
                secPost.setReactions(secLikes);
            }
            postDetails.add(i, secPost);
        }

        return postDetails;
    }

    public static ShareLinkContent getShareLinkContent(String quote, String link, String hashTag) {
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

    public static SharePhotoContent getSharePhotoContent(Bitmap image, String hashTag) {
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

    public static ShareMediaContent getShareMediaContent(List<Bitmap> images, String hashTag) {
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

    public static SharePhotoContent getSharePhotoContentWithLink(Bitmap image, String link, String hashTag) {
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

    public static ShareVideoContent getShareVideoContent(Uri uri, String hashTag) {
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

    public static ShareVideoContent getShareVideoContentWithLink(Uri uri, String link, String hashTag) {
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
}
