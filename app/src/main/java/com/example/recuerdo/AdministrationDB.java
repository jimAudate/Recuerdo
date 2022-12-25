package com.example.recuerdo;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdministrationDB  extends SQLiteOpenHelper {

    public AdministrationDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
    public AdministrationDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //DDL Creation usuario table
        sqLiteDatabase.execSQL("CREATE TABLE UserTb (user_id integer PRIMARY KEY AUTOINCREMENT,firstname text, password text)");


        sqLiteDatabase.execSQL("CREATE TABLE RegisterTb (register_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"firstname text,password text,qSecrete TEXT,user_id INTEGER, CONSTRAINT fk_User FOREIGN KEY (user_id) REFERENCES UserTb(user_id) ON DELETE CASCADE)");

        sqLiteDatabase.execSQL("CREATE TABLE EventTb (event_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"titulo text, fecha TEXT,tiempo TEXT," +
                "user_id INTEGER, CONSTRAINT fk_User FOREIGN KEY (user_id) REFERENCES UserTb(user_id) ON DELETE CASCADE)");

        Log.d("TAG_", "Creación  USUARIO_TBL y Register_TBL");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
