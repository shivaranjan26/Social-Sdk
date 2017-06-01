package custom.sdk.com.myfacebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shiva on 01-06-2017.
 */

public class FacebookUtils {

    public static String FB_POST_PREV_PAGE = "";
    public static String FB_POST_NEXT_PAGE = "";
    public static JSONArray FB_POSTS = null;

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
        //FB_POSTS.getJSONObject(3).getJSONObject("reactions").getJSONObject("paging").getJSONObject("cursors").getString("after")

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
}
