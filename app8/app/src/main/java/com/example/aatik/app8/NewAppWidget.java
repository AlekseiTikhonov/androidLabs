package com.example.aatik.app8;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider { //наследуемся от AppWidgetProvider
    RemoteViews remoteViews; //объект для доступа к интерфейсу нашего виджета
    String ACTION_NEXT="next"; //строки для обозначения действий в интентах
    String ACTION_PREV="prev";
    DBHelper dbHelper; //объекты для работы с БД нашего основного приложения
    SQLiteDatabase db;
    Cursor c;
    boolean nextEnabled; //для обозначения доступности кнопки Next
    boolean prevEnabled; //для обозначения доступности кнопки Prev
    static int pos=0; // статическая переменная текущей позиции в списке языков
    @Override
    public void onEnabled(Context context) { //срабатывает при активации виджета
        pos=0; //зануляем позицию в списке языков
//создаем объект класса RemoteViews, через него будем менять значения нужных параметров в интерфейсе
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        remoteViews.setViewVisibility(R.id.btnPrev, View.GONE); //делаем неактивной кнопку Prev,
//т.е. у объекта с идентификатором R.id.buttonPrev устанавливаем параметр setEnabled в значение false
        remoteViews.setViewVisibility(R.id.btnNext, View.VISIBLE);
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
        super.onEnabled(context); //вызов родительского метода
    }
    @Override //срабатывает при создании и обновлении виджета
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //Подготавливаем Intent для работы кнопки Next
        Intent next = new Intent(context, NewAppWidget.class);
        next.setAction(ACTION_NEXT); //устанавливаем действие – свою строку ACTION_NEXT
        //создаем наше событие с объектом класса Intent
        PendingIntent actionIntentNext = PendingIntent.getBroadcast(context, 0, next, 0);
        //Подготавливаем Intent для работы кнопки Prev
        Intent prev= new Intent(context, NewAppWidget.class);
        prev.setAction(ACTION_PREV);
        //создаем наше событие с объектом класса Intent
        PendingIntent actionIntentPrev = PendingIntent.getBroadcast(context, 0, prev, 0);
        //регистрируем наши события (их обработка будет ниже в методе onReceive)
        remoteViews.setOnClickPendingIntent(R.id.btnNext, actionIntentNext);
        remoteViews.setOnClickPendingIntent(R.id.btnPrev, actionIntentPrev);
        //делаем неактивной кнопку Prev
        remoteViews.setViewVisibility(R.id.btnPrev, View.GONE);
        remoteViews.setViewVisibility(R.id.btnNext, View.VISIBLE);
        prevEnabled=false; //соответствующим переменным присваиваем нужные логические значения
        nextEnabled=true;
//создаем объекты для работы с БД
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        c = db.query("mytable", null, null, null, null, null, null);
        c.moveToFirst(); //переходим на первую запись таблицы mytable (см. лаб. раб. №6)
//вызываем нашу функцию для заполнения виджета данными из БД (см. ниже)
        updateMyWidget(remoteViews,c,context);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews); //обновляем виджет
        c.close(); //закрываем все соединения с БД
        db.close();
        dbHelper.close();
        super.onUpdate(context, appWidgetManager, appWidgetIds); //метод родительского класса
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //Ловим наш интент и берем его действие, прописанное нами
        final String action = intent.getAction();
        ComponentName watchWidget; //объект для связи с виджетом, связываем его
        watchWidget = new ComponentName(context, NewAppWidget.class); // с нашим классом
//создаем объект класса AppWidgetManager для обновления через него нашего виджета
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//создаем объект класса RemoteViews, через него будем менять значения нужных параметров в интерфейсе
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//создаем объекты для работы с БД
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        c = db.query("mytable", null, null, null, null, null, null);
        if (ACTION_NEXT.equals(action)) { //если действие в интенте является «next»
            pos++; //увеличиваем позицию в списке на 1
            if (c.moveToPosition(pos)) { //если такая позиция есть,
                //то вызываем нашу функцию для заполнения виджета данными из БД (см. ниже)
                updateMyWidget(remoteViews, c, context);
            }
            if (c.isLast()) { //если текущая запись последняя
                //делаем неактивной кнопку Next
                remoteViews.setViewVisibility(R.id.btnNext, View.GONE);
                remoteViews.setViewVisibility(R.id.btnPrev, View.VISIBLE);
                nextEnabled=false; //присваиваем соответствующее значение логической переменной
            }
            if (!prevEnabled ) { //если кнопка Prev не активна
                remoteViews.setViewVisibility(R.id.btnPrev, View.VISIBLE); //делаем ее активной
                prevEnabled=true; //присваиваем соответствующее значение логической переменной
            }
        }
        if (ACTION_PREV.equals(action)) { //если действие в интенте является «prev»
            pos--; //уменьшаем позицию в списке на 1
            if (c.moveToPosition(pos)) {//если такая позиция есть,
                //то вызываем нашу функцию для заполнения виджета данными из БД (см. ниже)
                updateMyWidget(remoteViews,c,context);
            }
            if ( !nextEnabled ) { //если кнопка Next не активна
                remoteViews.setViewVisibility(R.id.btnNext, View.VISIBLE); //делаем ее активной
                nextEnabled=true; //присваиваем соответствующее значение логической переменной
            }
            if (c.isFirst()) { //если текущая запись первая
                //делаем неактивной кнопку Prev
                remoteViews.setViewVisibility(R.id.btnPrev, View.GONE);
                remoteViews.setViewVisibility(R.id.btnNext, View.VISIBLE);
                prevEnabled=false; //присваиваем соответствующее значение логической переменной
            }
        }
        appWidgetManager.updateAppWidget(watchWidget, remoteViews); //обновляем виджет
        c.close(); // закрываем все соединения с БД
        db.close();
        dbHelper.close();
        super.onReceive(context, intent); //вызываем метод родительского класса
    }
    //наша функция для заполнения виджета данными из БД
    private void updateMyWidget(RemoteViews remoteViews2, Cursor c2, Context context) {
        int nameColIndex = c.getColumnIndex("name"); //получаем номера столбцов с нужными данными
        int yearColIndex = c.getColumnIndex("year");
        int percentColIndex = c.getColumnIndex("percent");
        int authorColIndex = c.getColumnIndex("author");
//получаем строковые данные из БД
        String langName=c.getString(nameColIndex);
        String langYear=c.getString(yearColIndex);
        String langPercent=c.getString(percentColIndex);
        String authorInDB=c.getString(authorColIndex);
//выводим в интерфейс полученные данные
        remoteViews.setTextViewText(R.id.widgetLangName, langName);
        Toast.makeText(context, "text=: "+langName, Toast.LENGTH_SHORT).show();
        remoteViews.setTextViewText(R.id.widgetLangYear, langYear);
        remoteViews.setTextViewText(R.id.widgetLangPercent, langPercent);

        try { //для картинки автора пробуем организуем try … catch
            //пробуем преобразовать его в число, если получается, значит взято из внутренних ресурсов
            //т.к. все ресурсы хранятся в виде числовых констант, и выводим на ImageView
            remoteViews.setImageViewResource(R.id.widgetAuthImg, Integer.parseInt(authorInDB));
        } catch (NumberFormatException e) { // если сработало исключение, значит, там путь к файлу
            Uri uri=Uri.parse(authorInDB); //делаем из него URI (см. в лаб.раб.№5 )
            remoteViews.setImageViewUri(R.id.widgetAuthImg, uri); // и выводим на ImageView
        }
    }
    public static void setPos(int pos1) { //функция для изменения текущей позиции в списке
        pos=pos1;
    }
}
