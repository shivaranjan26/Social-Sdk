package custom.sdk.com.myfacebook.model;

import java.util.ArrayList;

/**
 * Created by Shiva on 09-06-2017.
 */

public class FBPostDetails {


    private String fromName;
    private String message;
    private String story;
    private String picture;
    private String link;
    private ArrayList<String> reactions;
    private ArrayList<FBFirstLevelComments> firstLevelComments;
    private String postName;
    private String description;

    public void setFromName(String name) {
        fromName = name;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void setStory(String info) {
        story = info;
    }

    public void setPicture(String pic) {
        picture = pic;
    }

    public void setLink(String url) {
        link = url;
    }

    public void setReactions(ArrayList<String> likes) {
        reactions = likes;
    }

    public void setFirstLevelComments(ArrayList<FBFirstLevelComments> comments) {
        firstLevelComments = comments;
    }

    public void setPostName(String name) {
        postName = name;
    }

    public void setDescription(String desc) {
        description = desc;
    }
}
