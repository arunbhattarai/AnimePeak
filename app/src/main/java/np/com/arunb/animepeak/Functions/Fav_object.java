package np.com.arunb.animepeak.Functions;


import java.io.Serializable;

import np.com.arunb.animepeak.Activity.MainActivity;

public class Fav_object implements Serializable {

    public String title;
    public String id;
    public String img;

    // Default constructor (no-argument constructor)
    public Fav_object() {
        // Required for Firebase deserialization
    }

    public Fav_object(String title, String id, String img) {
        this.title = title;
        this.id = id;
        this.img = img;


    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return id;
    }

    public String getImg() {
        return img;
    }


    public static void removeFavByID(String id) {
        for (int i = 0; i < MainActivity.fav_list.size(); i++) {
            if (MainActivity.fav_list.get(i).getID().equals(id)) {
                MainActivity.fav_list.remove(i);
                break;
            }
        }
    }

}
