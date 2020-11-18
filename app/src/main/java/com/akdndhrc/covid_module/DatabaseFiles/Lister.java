package com.akdndhrc.covid_module.DatabaseFiles;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import io.requery.android.database.sqlite.SQLiteDatabase;


public class Lister {


    private Context context;

    public Lister(Context context) {
        this.context = context;
    }

    private Helper help;
    private SQLiteDatabase sq_db;

    public SQLiteDatabase createAndOpenDB() {
        help = new Helper(context);
        sq_db = help.getWritableDatabase();
        sq_db.disableWriteAheadLogging();
        Log.d("000258", "Lister Open !!!!!!!!!!!!!");

        return sq_db;
    }

   /* public SQLiteDatabase createAndOpenDB(String password) {
        help = new Helper(context);
        sq_db = help.getWritableDatabase(password);

        return sq_db;
    }*/

    public boolean executeNonQuery(String command) {
        try {
            sq_db.execSQL(command);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String[][] executeReader(String command) {
        Cursor cursor = sq_db.rawQuery(command, null);
        int col = cursor.getColumnCount();
        int row = cursor.getCount();
        String[][] str_arry = new String[row][col];
        int j = 0, i = 0;

        cursor.moveToFirst();
        if (row != 0) {
            for (i = 0; i < row; i++) {
                for (j = 0; j < col; j++) {
                    str_arry[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();

            return str_arry;
        } else
            return null;
    }


    public String[][] executeReader(String command, String[] Args) {
        Cursor cursor = sq_db.rawQuery(command, Args);
        int col = cursor.getColumnCount();
        int row = cursor.getCount();
        String[][] str_arry = new String[row][col];
        int j = 0, i = 0;

        cursor.moveToFirst();
        if (row != 0) {
            for (i = 0; i < row; i++) {
                for (j = 0; j < col; j++) {
                    str_arry[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();

            return str_arry;
        } else
            return null;
    }


    public void closeDB() {
        sq_db.close();
    }


}


//
//	public String[][] executeReader64(String command)
//	{
//		Cursor cursor = sq_db.rawQuery(command, null);
//		int col = cursor.getColumnCount();
//		int row = cursor.getCount();
//		String[][] str_arry = new String[row][col];
//		int j = 0, i = 0;
//
//		if(row != 0)
//		{
//			for(i = 0 ; i < row ; i++)
//			{
//				cursor.moveToNext();
//				for(j = 0 ; j < col ; j++)
//				{
//					str_arry[i][j] = new String(Base64.decode(cursor.getString(j), Base64.NO_PADDING));
//				}
//			}
//			cursor.close();
//			return str_arry;
//		}
//		else
//			return null;
//	}