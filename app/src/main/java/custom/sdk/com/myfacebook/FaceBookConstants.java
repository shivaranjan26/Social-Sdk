package custom.sdk.com.myfacebook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shiva on 04-06-2017.
 */

public class FaceBookConstants {

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
}
