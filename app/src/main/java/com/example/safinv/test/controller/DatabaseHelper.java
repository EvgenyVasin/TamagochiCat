package com.example.safinv.test.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    // названия столбцов
    public static final String NAME_COLUMN = "name";
    public static final String HUNGER_COLUMN = "hunger";
    public static final String ENERGY_COLUMN = "energy";
    public static final String HAPPINESS_COLUMN = "happiness";
    public static final String HEALTH_COLUMN = "health";
    public static final String BIRTHDAY_COLUMN = "birthday";
    // имя базы данных
    private static final String DATABASE_NAME = "petsdatabase.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;
    // имя таблицы
    public static final String DATABASE_TABLE = "pets";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID  + " integer primary key autoincrement, "
                                    + NAME_COLUMN + " text not null, "
                                    + HUNGER_COLUMN + " integer not null, "
                                    + ENERGY_COLUMN + " integer not null, "
                                    + HAPPINESS_COLUMN + " integer not null, "
                                    + HEALTH_COLUMN + " integer not null, "
                                    + BIRTHDAY_COLUMN + " date not null);";



    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Date date = new Date();
        db.execSQL(DATABASE_CREATE_SCRIPT);
        ContentValues values = new ContentValues();
        // Задайте значения для каждого столбца
        values.put(this.NAME_COLUMN, "Луна");

        values.put(this.HUNGER_COLUMN, "50");
        values.put(this.ENERGY_COLUMN, "100");
        values.put(this.HAPPINESS_COLUMN, "50");
        values.put(this.HEALTH_COLUMN, "100");
        values.put(this.BIRTHDAY_COLUMN, String.valueOf(date));
        // Вставляем данные в таблицу
        db.insert(DATABASE_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }
}
