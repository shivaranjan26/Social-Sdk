package custom.sdk.com.socialsdk;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shiva on 10-06-2017.
 */

public class SocialSDKUtils {

    //FaceBook Variables
    public static List<String> readPermissionNeeds = Arrays.asList(
            "public_profile",
            "email", "user_posts", "user_photos", "user_birthday",
            "user_friends", "read_custom_friendlists", "user_about_me", "user_education_history",
            "user_games_activity", "user_hometown", "user_likes", "user_location", "user_relationship_details",
            "user_relationships", "user_religion_politics", "user_status", "user_tagged_places",
            "user_videos", "user_website", "user_work_history");

    public static List<String> writePermissionNeeds = Arrays.asList("publish_actions");
}
