package custom.sdk.com.myfacebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
}
