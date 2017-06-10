package custom.sdk.com.myfacebook.model;

import java.util.ArrayList;

/**
 * Created by Shiva on 10-06-2017.
 */

public class AlbumPhotos {

    private String albumName;
    private ArrayList<String> albumPhotos;

    public void setAlbumName(String name){
        albumName = name;
    }

    public void setAlbumPhotos(ArrayList<String> photos) {
        albumPhotos = photos;
    }
}
