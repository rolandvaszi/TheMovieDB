package com.example.themoviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MovieDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "app_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Profile.CREATE_TABLE);
        db.execSQL(Movie.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Profile.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Movie.TABLE_NAME);
        onCreate(db);
    }

    public int updateProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Profile.COLUMN_USERNAME, profile.getUserName());
        values.put(Profile.COLUMN_PASSWORD, profile.getPassword());
        values.put(Profile.COLUMN_PROFILE_PICTURE, profile.getProfilePicture());

        int retVal = db.update(Profile.TABLE_NAME, values, Profile.COLUMN_ID + " = ?",
                new String[]{String.valueOf(profile.getId())});

        db.close();
        return retVal;
    }

    public Profile getProfile(){
        Log.d(TAG, "Get profile for " + MainActivity.currentUser);
        String[] selectionArgs = new String[]{MainActivity.currentUser};
        String[] projection = new String[]{Profile.COLUMN_ID,
                Profile.COLUMN_USERNAME,
                Profile.COLUMN_PASSWORD,
                Profile.COLUMN_PROFILE_PICTURE};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Profile.TABLE_NAME,
                projection,
                Profile.COLUMN_USERNAME + " =?", selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            Profile profile = new Profile(cursor.getInt(cursor.getColumnIndex(Profile.COLUMN_ID)),
                    MainActivity.currentUser,
                    cursor.getString(cursor.getColumnIndex(Profile.COLUMN_PASSWORD)),
                    cursor.getBlob(cursor.getColumnIndex(Profile.COLUMN_PROFILE_PICTURE)));

            cursor.close();
            db.close();
            return profile;
        }

        cursor.close();
        db.close();
        return null;
    }

    public Boolean login(String username, String password){
        Log.d(TAG, "Logging in: " + username);
        String[] selectionArgs = new String[]{username, password};
        String[] projection = new String[]{Profile.COLUMN_ID};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Profile.TABLE_NAME,
                projection,
                Profile.COLUMN_USERNAME + " =? AND " + Profile.COLUMN_PASSWORD + "=?", selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            MainActivity.currentUserID = cursor.getInt(cursor.getColumnIndex(Profile.COLUMN_ID));
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }

    public void register(Profile profile) {
        Log.d(TAG, "Registering");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Profile.COLUMN_USERNAME, profile.getUserName());
        values.put(Profile.COLUMN_PASSWORD, profile.getPassword());
        values.put(Profile.COLUMN_PROFILE_PICTURE, profile.getProfilePicture());

        db.insert(Profile.TABLE_NAME, null, values);

        //getting id
        login(profile.getUserName(), profile.getPassword());

        db.close();
    }

    public void addMovie(Movie movie){
        Log.d(TAG, "Add movie to favorites");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Movie.COLUMN_ID, movie.getId());
        values.put(Movie.COLUMN_TITLE, movie.getTitle());
        values.put(Movie.COLUMN_OVERVIEW, movie.getOverview());
        values.put(Movie.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(Movie.COLUMN_USER_ID, MainActivity.currentUserID);

        db.insert(Movie.TABLE_NAME, null, values);
        db.close();
    }

    public List<Movie> getMovies(){
        Log.d(TAG, "Get saved movies");
        List<Movie> movies = new ArrayList<>();

        String[] projection = new String[]{Movie.COLUMN_ID, Movie.COLUMN_TITLE, Movie.COLUMN_OVERVIEW, Movie.COLUMN_POSTER_PATH};
        String[] selectionArgs = new String[]{String.valueOf(MainActivity.currentUserID)};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Movie.TABLE_NAME,
                projection, Movie.COLUMN_USER_ID + " =? ", selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {

            while(cursor.moveToNext()){
                Movie movie = new Movie(cursor.getInt(cursor.getColumnIndex(Movie.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Movie.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Movie.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(Movie.COLUMN_POSTER_PATH)));

                movies.add(movie);
            }

            cursor.close();
            db.close();
            return movies;
        }

        cursor.close();
        db.close();
        return null;
    }

    public void deleteMovie(int id){
        Log.d(TAG, "Delete movie" + id);
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = Movie.COLUMN_ID + " =? and " + Movie.COLUMN_USER_ID + " =?";
        String[] selectionArgs = new String[]{Integer.toString(id), Integer.toString(MainActivity.currentUserID)};
        db.delete(Movie.TABLE_NAME, whereClause, selectionArgs);
        db.close();
    }
}
