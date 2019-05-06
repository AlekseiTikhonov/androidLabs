package com.example.aatik.app8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) { //конструктор класса для нашей БД
        super(context, "myDB", null, 1); //вызов конструктора суперкласса
        Log.d("myLogs", "--- Конструктор ---"); //вывод в LogCat для отладки
    }
    @Override
    public void onCreate(SQLiteDatabase db) { //метод для создания БД
        Log.d("myLogs", "--- onCreate database ---"); //вывод в LogCat для отладки
        // создаем таблицу с полями
        db.execSQL("create table mytable (" // команда SQL для создания таблицы с нужными полями
                + "id integer primary key autoincrement, " //идентификатор записи
                + "number integer, " //номер языка в созданном списке
                + "name text, " //имя языка
                + "year text, " //год создания
                + "percent text, "
                + "author text"+ ");"); //поле для хранения графического ресурса изображения автора
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//метод для обновления данных в БД, мы его не будем реализовывать
    }
}