package custom.sdk.com.myfacebook.model;

import java.util.ArrayList;

/**
 * Created by Shiva on 10-06-2017.
 */

public class FBFirstLevelComments {


    private String fromName;
    private String message;
    private ArrayList<String> reactions;
    ArrayList<FBSecondLevelComments> comments;


    public void setFromName(String name) {
        fromName = name;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void setReactions(ArrayList<String> likes) {
        reactions = likes;
    }

    public void setComments(ArrayList<FBSecondLevelComments> comms) {
        comments = comms;
    }
}
