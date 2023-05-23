/*package de.bfwi.sqlite;*/
package com.example.UebergabebuchEntw;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBTools
{
    final static String MY_DB_NAME = "bfwiDB";
    final static String MY_DB_TABLE = "geburtstage";
    final static String DEBUG_TAG ="de.bfwi.sqlite";

    SQLiteDatabase myDB = null;
    String[] spalten = new String[]{"id", "fullname", "birthday"};

    public void openDB(Activity activity)
    {
        try
        {
            myDB = activity.openOrCreateDatabase(MY_DB_NAME, Context.MODE_PRIVATE, null);
            Log.i(DEBUG_TAG,activity.getLocalClassName() + " - DB is opened.");
        }
        catch (Exception ex)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() + " - Open DB Error: " + ex.getMessage());
        }
    }

    public void closeDB(Activity activity)
    {
        if (myDB != null)
        {
            myDB.close();
            myDB = null;
            Log.i(DEBUG_TAG, activity.getLocalClassName() + " - DB is closed.");
        }
    }

    public void createTable(Activity activity)
    {
        try
        {
            Log.i(DEBUG_TAG, activity.getLocalClassName() + " - Create DB ...");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + MY_DB_TABLE + " (" + "id integer primary key autoincrement, " + "fullname varchar(30) not null, " + "birthday varchar(10) not null)");
            Log.i(DEBUG_TAG, activity.getLocalClassName() + " - DB is created.");
        }
        catch (Exception ex)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() + " - Create DB or create table Error: " + ex.getMessage());
        }
    }

    public Boolean dropTable(Activity activity)
    {

        Boolean rc = false;
        Log.i(DEBUG_TAG, "DROP TABLE IF EXISTS " + MY_DB_TABLE);
        try
        {
            myDB.execSQL("DROP TABLE IF EXISTS " + MY_DB_TABLE);

            Log.i(DEBUG_TAG, activity.getLocalClassName() + " - DB is dropped.");
            rc = true;
        }
        catch (SQLiteException ex)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() +  " - Drop table Error: " + ex.getMessage());
        }
        return rc;
    }

    public Cursor getCursor(Activity activity)
    {
        Cursor cursor = null;
        try
        {
            cursor = myDB.query(MY_DB_TABLE, spalten, null, null, null, null, null, null);
        }
        catch (Exception e)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() + " Cursor Error: " + e.getMessage());
        }
        return cursor;
    }

    public Cursor getCursor(Activity activity, String sqlString)
    {
        Cursor cursor = null;
        try
        {
            cursor = myDB.query(MY_DB_TABLE, spalten, sqlString,null, null, null, null, null);
        }
        catch (Exception e)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() + " Cursor Error: " + e.getMessage());
        }
        return cursor;
    }


    public Boolean insertRecord(Activity activity, String name, String gebdat)
    {
        Boolean rc = false;
        try
        {
            ContentValues daten = new ContentValues();
            daten.put("fullname", name);
            daten.put("birthday",gebdat);
            long id = myDB.insert(MY_DB_TABLE, null, daten);
            Log.i(DEBUG_TAG, activity.getLocalClassName() + "- Insert Record with ID:"+ id);
            rc = true;
        }
        catch (Exception ex)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() + "- Insert Record Error:" + ex.getMessage());
        }
        return rc;
    }


    public Boolean modifyRecord(Activity activity, Cursor cu, ContentValues daten)
    {
        Boolean rc = false;
        try
        {
            long id = myDB.update(MY_DB_TABLE, daten, "id=" + cu.getInt(0), null);

            Log.i(DEBUG_TAG, activity.getLocalClassName() + " - Update Record Index: " + cu.getInt(0));
            rc = true;
        }
        catch (Exception ex)
        {
            Log.e(DEBUG_TAG, activity.getLocalClassName() + " - Update Record Error: " + ex.getMessage());
        }
        return rc;
    }

    public Boolean deleteRecord(Activity activity, Cursor cu)
    {
        Boolean rc = false;
        try
        {
            myDB.beginTransaction();
            long id = myDB.delete(MY_DB_TABLE,"id=" + cu.getInt(0),null);
            myDB.execSQL("COMMIT");
            Log.i(DEBUG_TAG,activity.getLocalClassName() + " - Remove Record Index: " + cu.getInt(0));
            rc = true;
        }
        catch (Exception ex)
        {
            Log.e(DEBUG_TAG,activity.getLocalClassName() + " - Remove Record Error: " + ex.getMessage());
        }
        return rc;
    }

    public long getDate(Activity activity, String suchDatum)
    {
        long rc = 0L;
        java.util.Date datum = null;

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try
        {
            datum = df.parse(suchDatum);
        }
        catch (ParseException e)
        {
            Log.e(DEBUG_TAG,activity.getLocalClassName() + " - Parse Date Error: " + e.getMessage());
        }

        rc = datum.getTime();

        return rc;
    }
}

