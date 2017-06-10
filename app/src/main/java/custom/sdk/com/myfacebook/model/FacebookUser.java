package custom.sdk.com.myfacebook.model;

import java.util.ArrayList;

/**
 * Created by Shiva on 06-06-2017.
 */

public class FacebookUser {

    String aboutMe;
    String firstName;
    String lastName;
    String middleName;
    String birthday;
    String age;
    String coverPic;
    String profilePic;
    String gender;
    String hometown;
    String relationship;
    String religion;
    String timeZone;
    String website;
    String friendsCount;
    ArrayList<String> likedPages;
    ArrayList<EmploymentDetails> workDetails;
    ArrayList<String> familyDetails;
    ArrayList<String> educationDetails;
    ArrayList<String> languages;


    public void setFirstName(String name) {
        firstName = name;
    }

    public void setMiddleName(String name) {
        middleName = name;
    }

    public void setLastName(String name) {
        lastName = name;
    }

    public void setAboutMe(String about) {
        aboutMe = about;
    }

    public void setBirthday(String bday) {
        birthday = bday;
    }

    public void setAge(String mAge) {
        age = mAge;
    }

    public void setCoverPicUrl(String url) {
        coverPic = url;
    }

    public void setProfilePicUrl(String url) {
        profilePic = url;
    }

    public void setGender(String gen) {
        gender = gen;
    }

    public void setHomeTown(String home) {
        hometown = home;
    }

    public void setRelationshipStatus(String status) {
        relationship = status;
    }

    public void setReligion(String rel) {
        religion = rel;
    }

    public void setTimeZone(String time) {
        timeZone = time;
    }

    public void setEducation(ArrayList<String> edu) {
        educationDetails = edu;
    }

    public void setLanguages(ArrayList<String> lang) {
        languages = lang;
    }

    public void setWorkDetails(ArrayList<EmploymentDetails> work) {
        workDetails = work;
    }

    public void setFriendsCount(String count) {
        friendsCount = count;
    }

    public void setWebsite(String web) {
        website = web;
    }

    public void setFamilyDetails(ArrayList<String> family) {
        familyDetails = family;
    }

    public void setLikedPages(ArrayList<String> pages) {
        likedPages = pages;
    }
}
