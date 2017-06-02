package custom.sdk.com.myfacebook;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiva on 01-06-2017.
 */

public class FacebookUtils {

    public static String FB_POST_PREV_PAGE = "";
    public static String FB_POST_NEXT_PAGE = "";
    public static JSONArray FB_POSTS = null;
    public static JSONArray FB_POSTS_COMMENTS = null;

    public static ArrayList<String> getPostIds(){
        ArrayList<String> postIds = new ArrayList<>();

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                postIds.add(FB_POSTS.getJSONObject(i).getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return postIds;
    }

    public static String getDescriptionFromPost(String id){
        String description = null;

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                if(FB_POSTS.getJSONObject(i).getString("id").equals(id)) {
                    description = FB_POSTS.getJSONObject(i).getString("description");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return description;
    }

    public static String getNameFromPost(String id){
        String name = null;

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                if(FB_POSTS.getJSONObject(i).getString("id").equals(id)) {
                    name = FB_POSTS.getJSONObject(i).getString("name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return name;
    }

    public static String getLinkFromPost(String id){
        String link = null;

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                if(FB_POSTS.getJSONObject(i).getString("id").equals(id)) {
                    link = FB_POSTS.getJSONObject(i).getString("link");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return link;
    }

    public static String getCaptionFromPost(String id){
        String caption = null;

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                if(FB_POSTS.getJSONObject(i).getString("id").equals(id)) {
                    caption = FB_POSTS.getJSONObject(i).getString("caption");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return caption;
    }

    public static String getPictureFromPost(String id){
        String pic = null;

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                if(FB_POSTS.getJSONObject(i).getString("id").equals(id)) {
                    pic = FB_POSTS.getJSONObject(i).getString("full_picture");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return pic;
    }

    public static ArrayList<String> getReactionsFromPost(String id) {
        String name = null;
        String pic = null;
        ArrayList<String> name_pic = new ArrayList<>();

        for (int i = 0; i < FB_POSTS.length(); i++) {
            try {
                if(FB_POSTS.getJSONObject(i).getString("id").equals(id)) {
                    JSONArray reactionArray = FB_POSTS.getJSONObject(i).getJSONObject("reactions").getJSONArray("data");
                    for (int j = 0; j < reactionArray.length(); j++) {
                        name = reactionArray.getJSONObject(j).getString("name");
                        pic = reactionArray.getJSONObject(j).getString("pic");
                        name_pic.add(j, name + "~" + pic);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return name_pic;
    }

    public static List<ArrayList<String>> getFirstLevelCommentsFromPostPosition(int pos) {
        List<ArrayList<String>> values = new ArrayList<>();

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> comments = new ArrayList<>();
        ArrayList<String> likes = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        try {
        JSONObject comment_object = FB_POSTS_COMMENTS.getJSONObject(pos);
        JSONArray message_object = null;
            if(comment_object.has("comments")) {
                message_object = comment_object.getJSONObject("comments").getJSONArray("data");
                for (int i = 0; i < message_object.length(); i++) {
                    String id = "";
                    String name = message_object.getJSONObject(i).getJSONObject("from").getString("name");
                    String comment_firstLevel = message_object.getJSONObject(i).getString("message");
                    String comment_firstLevel_likes = "";

                    if(message_object.getJSONObject(i).has("reactions")) {
                        JSONArray likeArray = message_object.getJSONObject(i).getJSONObject("reactions").getJSONArray("data");
                        for (int j = 0; j < likeArray.length(); j++) {
                            if(comment_firstLevel_likes.equals("")) {
                                comment_firstLevel_likes = likeArray.getJSONObject(j).getString("name");
                            } else {
                                comment_firstLevel_likes = comment_firstLevel_likes + "~" + likeArray.getJSONObject(j).getString("name");
                            }

                        }
                    } else {
                        comment_firstLevel_likes = "";
                    }

                    if(message_object.getJSONObject(i).has("comments")) {
                        JSONArray comments_ids_obj = message_object.getJSONObject(i).getJSONObject("comments").getJSONArray("data");
                        for (int k = 0; k < comments_ids_obj.length(); k++) {
                            if(id.equals("")) {
                                id = comments_ids_obj.getJSONObject(k).getString("id");
                            } else {
                                id = id + "~" + comments_ids_obj.getJSONObject(k).getString("id");
                            }

                        }
                    }


                    names.add(i, name);
                    comments.add(i, comment_firstLevel);
                    likes.add(i, comment_firstLevel_likes);
                    ids.add(i, id);
                }
            } else {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        values.add(0, names);
        values.add(1, comments);
        values.add(2, likes);
        values.add(3, ids);
        return values;
    }


    public static void getSecondLevelCommentsFromPostId(String id, AccessToken token) {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                id,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject resp = response.getJSONObject();
                        //FB_POST_PREV_PAGE = "Sda";
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "from,message,reactions");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
