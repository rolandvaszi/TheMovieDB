package com.example.themoviedb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Profile {
    private static final String TAG = "MovieProfileClass";
    public static final String TABLE_NAME = "profile";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PROFILE_PICTURE = "profile_picture";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
                    + COLUMN_PROFILE_PICTURE + " BLOB"
                    + ")";

    private int id;
    private String userName;
    private String password;
    private byte[] profilePicture;

    public Profile(int id, String userName, String password, Bitmap profilePicture) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.profilePicture = toByteArray(profilePicture);
    }

    public Profile(int id, String userName, String password, byte[] profilePicture) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public Profile(String userName, String password, Bitmap profilePicture) {
        this.userName = userName;
        this.password = password;
        this.profilePicture = toByteArray(profilePicture);
    }

    public Profile(String userName, String password, byte[] profilePicture) {
        this.userName = userName;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public int getId(){
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public Bitmap getProfilePictureBitmap() {
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(profilePicture);
        //return BitmapFactory.decodeStream(inputStream);
        return BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = toByteArray(profilePicture);
    }

    private byte[] toByteArray(Bitmap bitmap){
        /*int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
