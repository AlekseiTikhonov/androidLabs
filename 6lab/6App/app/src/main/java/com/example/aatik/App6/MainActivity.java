package com.example.aatik.App6;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper; // объект класса DBHelper
    SQLiteDatabase db; //объект для работы с БД
    ArrayList <HashMap<String, Object>>  myLangs = new ArrayList<HashMap<String,Object>>(); //создаем массив
    private final String NAMEKEY= "langname";
    private final String YEARKEY= "langyear";
    private final String PERCENTKEY= "langpercent";
    private final String AUTHORKEY= "langauthor";
    private final int INPUT_ACTIVITY=1;
    private final int CHANGE_PICTURE=2;
    ArrayList<String> langNames = new ArrayList(Arrays.asList("Basic", "Pascal", "C", "Cplusplus", "Csharp", "Java", "PHP", "Python", "ObjectiveC"));
    ArrayList<String> langYears = new ArrayList(Arrays.asList("1964", "1975", "1972", "1983", "2000", "1995", "1994", "1991", "1983"));
    ArrayList<String>  langPercents = new ArrayList(Arrays.asList("10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%"));
    ArrayList<Object> langAuthors=new ArrayList<Object>(Arrays.asList(R.drawable.basic, R.drawable.pascal,
            R.drawable.c, R.drawable.cpp, R.drawable.c_sharp, R.drawable.java, R.drawable.php,
            R.drawable.python, R.drawable.no_picture));
    ListView langList;
    SimpleAdapter listAdapter2;
    AdapterView.AdapterContextMenuInfo info;
    HashMap<String, Object> hm; //хэш-мап для пар ключ-значение (название поля – значение поля)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        langList = (ListView) findViewById(R.id.langList);
        registerForContextMenu(langList);
        dbHelper = new DBHelper(this); //создаем объект для БД
        db = dbHelper.getWritableDatabase(); //получаем ссылку для работы с БД

        if (savedInstanceState!=null && savedInstanceState.containsKey("HashMap")) {
//то мы наш массив myLangs берем из savedInstanceState
            myLangs=(ArrayList<HashMap<String, Object>>) savedInstanceState.getSerializable("HashMap");
            Toast.makeText(langList.getContext(), "From saved",
                    Toast.LENGTH_SHORT).show(); //для отслеживания
        }
        else { //а иначе (если ничего еще не сохранялось) делаем как раньше – заполняем в цикле
            Toast.makeText(langList.getContext(), "From create", Toast.LENGTH_SHORT).show();
//цикл по всем названиям языков для формирования хэш-мапов и заполнения ими главного массива
            //создаем курсор – объект для выполнения запросов к БД и получения результатов из БД
//связываем его с таблицей mytable
            Cursor c = db.query("mytable", null, null, null, null, null, null);
//функция moveToFirst() курсора переходит на первую запись таблицы и возвращает true,
//или возвращает false, если записей нет
            if (!c.moveToFirst()) { //если нет записей в таблице mytable
                fillMyLangs(); //выносим заполнение массива myLangs в отдельную функцию (см. ниже)
//для отладки
                Toast.makeText(langList.getContext(), "DB is empty ", Toast.LENGTH_SHORT).show();
                putDataToDB(); //метод для заполнения БД текущими данными из массивов (см. ниже)
            }
            else { //иначе, если в БД есть записи, заполняем ими наши массивы
                Toast.makeText(langList.getContext(), "From DB", Toast.LENGTH_SHORT).show();
                langNames=new ArrayList<String>(); //создаем заново массивы
                langYears=new ArrayList<String>();
                langPercents = new ArrayList<String>();
                langAuthors=new ArrayList<Object>();
                int nameColIndex = c.getColumnIndex("name"); //находим индексы нужных столбцов
                int yearColIndex = c.getColumnIndex("year");
                int percentColIndex = c.getColumnIndex("percent");
                int authorColIndex = c.getColumnIndex("author");
                do { //цикл по всем записям
//из текущей записи из столбца c индексом nameColIndex берем значение поля и добавляем его
                    langNames.add(c.getString(nameColIndex)); //в массив langNames
                    langYears.add(c.getString(yearColIndex)); //то же самое для langYears
                    langPercents.add(c.getString(percentColIndex)); //то же самое для langYears
//получаем значение из колонки с описанием графического ресурса картинки автора языка
                    String authorInDB=c.getString(authorColIndex);
//пробуем преобразовать его в число, если получается, значит взято из внутренних ресурсов
//т.к. все ресурсы хранятся в виде числовых констант, и добавляем в langAuthors
                    try {
                        langAuthors.add( Integer.parseInt(authorInDB) );
//если срабатывает исключение NumberFormatException (т.е. это не число),
//значит там путь к внешнему файлу
                    } catch (NumberFormatException e) {
                        langAuthors.add(authorInDB); //записываем этот путь
                    }
                }
                while (c.moveToNext()); //пока есть записи в таблице, с которой связан курсор
                fillMyLangs(); //вызываем функцию заполнения массива myLangs
            }
            Log.d("myLogs", "--- records've been put---"); //вывод в LogCat для отладки
            c.close(); //закрываем курсор
            c = db.query("mytable", null, null, null, null, null, null);//снова соединяем его с mytable
            c.moveToFirst(); // переходим на первую запись
//и выводим сообщение о текущем кол-ве записей в таблице mytable
            Toast.makeText(langList.getContext(), " "+c.getCount(),Toast.LENGTH_SHORT).show();
            c.close(); //закрываем курсор
        }

        listAdapter2 = new SimpleAdapter(this, myLangs, R.layout.my_list,
                new String[]{
                        NAMEKEY,
                        YEARKEY,
                        PERCENTKEY,
                        AUTHORKEY
                },
                new int[]{
                        R.id.text1,
                        R.id.text2,
                        R.id.text3,
                        R.id.imageView1
                }
        );
        langList.setAdapter(listAdapter2);
        langList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view1, int pos, long id) {
                //преобразуем View к типу LinearLayout, т.к. наш элемент списка – это LinearLayout
//с помещенными в него компонентами
                LinearLayout myLay=(LinearLayout)view1;
//берем нулевой элемент в компоновке
                LinearLayout myLay2=(LinearLayout)myLay.getChildAt(0);
//во вложенной компоновке берем нулевой элемент (это TextView)
                TextView text=(TextView)myLay2.getChildAt(0);
//и показываем текст из полученного TextView во всплывающем сообщении
                showMessage(text.getText().toString());
            }
        });
    }
    private void fillMyLangs() { //метод для заполнения массива myLangs
        for (String s: langNames) {
            hm = new HashMap<String, Object>(); //создаем хэш-мап
            hm.put(NAMEKEY,s); //записываем в него название языка
            String year=langYears.get(langNames.indexOf(s)); //находим соответствующий языку год
            hm.put(YEARKEY, year); //записываем в хэш-мап год создания языка
            String percent = langPercents.get(langNames.indexOf(s)); //находим соответствующий языку год
            hm.put(PERCENTKEY, percent); //записываем в хэш-мап год создания языка
            Object author=langAuthors.get(langNames.indexOf(s));
            hm.put(AUTHORKEY,author); //записываем в хэш-мап автора языка
            myLangs.add(hm); //заносим хэш-мап в главный массив
        }
    }
    private void putDataToDB() { //метод для заполнения БД текущими данными из массивов
        db.delete("mytable", null, null); //очищаем таблицу mytable
        for (String s: langNames) { // цикл по всем элементам массива langNames
            int i=langNames.indexOf(s); //получаем номер текущего элемента
//создаем объект, аналог хэш-мапа, но для полей таблиц (задаем пары «имя столбца» – значение)
            ContentValues cv = new ContentValues();
            cv.put("number", i); //для поля number задаем значение i
            cv.put("name", s); //для поля name задаем значение s
            cv.put("year", langYears.get(i)); //для поля year задаем значение langYears.get(i)
            cv.put("percent", langPercents.get(i));
            cv.put("author", langAuthors.get(i).toString()); // то же самое для author
            db.insert("mytable", null, cv); //вставляем все данные из cv в mytable
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_picture:
                onChangePictureClick();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void onChangePictureClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //создаем намерение для выбора файла
        intent.setType("file/*"); // указываем тип открываемых ресурсов
        startActivityForResult(intent, CHANGE_PICTURE); //стартуем активити и ждем от него
    } //результата с кодом CHANGE_PICTURE

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
        info = (AdapterView.AdapterContextMenuInfo)menuInfo; //сохраняем информацию об элементе,
//у которого выбрано контекстное меню объект info должен быть объявлен как поле класса нашего
// AdapterView.AdapterContextMenuInfo info;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) { //оператор выбора для определения, от какого активити пришел результат
            case INPUT_ACTIVITY: //идентификатор нашего активити ввода нового языка
//создаем массив строк, в который записываем результаты из второго окна
                String[] newItem = data.getStringArrayExtra("newItem");
//далее из полученого массива переносим данные в список элементов
                langNames.add(0, newItem[0]); //добавляем текст из edit1 в langNames
                langYears.add(0, newItem[1]); //добавляем текст из edit2 в langYears
                langPercents.add(0, newItem[2]);
                langAuthors.add(0,R.drawable.no_picture); //для корректной работы с массивом
                hm = new HashMap<String, Object>(); //создаем хэш-мап
                hm.put(NAMEKEY, langNames.get(0)); //записываем в него название языка
                hm.put(YEARKEY, langYears.get(0));
                hm.put(PERCENTKEY, langPercents.get(0));
                hm.put(AUTHORKEY, R.drawable.no_picture);
                myLangs.add(0,hm); //заносим хэш-мап в начало главного массива
                listAdapter2.notifyDataSetChanged();
                putDataToDB();
                break;
            case CHANGE_PICTURE:
                if (resultCode==-1) { //если файл был выбран
//создаем объект класса Uri (URI – Uniform Resource Identifier – унифицированный идентификатор ресурсов)
                    Uri uri = data.getData(); //берем URI из данных, полученных из активити
                    String path=null; //создаем переменную для хранения пути к выбранному файлу
//следующий код нужен для правильного отображения пути к графическому файлу
                    if ("content".equalsIgnoreCase(uri.getScheme())) {//если открыт был файл с
//предопределенным системой содержимым (графический файл)
                        String[] projection = { "_data" }; //то создаем проекцию
                        Cursor cursor = null; //создаем курсор (для обращения к системной БД)
                        //связываем курсор с запросом к системной БД с указанием URI и проекции
                        cursor = this.getContentResolver().query(uri, projection, null, null, null);
                        //получаем индекс колонки, в котором хранится нужная нам информация
                        int column_index = cursor.getColumnIndexOrThrow("_data");
                        //переходим к первой записи и одновременно проверяем на null
                        if (cursor.moveToFirst()) {
                            path=cursor.getString(column_index);//получаем путь к графическому файлу
                        }
                    } //если файл обычный (например, текстовый),
                    else path=uri.getPath(); //то просто получаем путь
//выводим сообщение с путем к файлу
                    Toast.makeText(langList.getContext(), "File Uri: " + path,
                            Toast.LENGTH_SHORT).show();
//в массиве langAuthors меняем элемент с номером info.position (из onCreateContextMenu, это номер
//элемента в списке, у которого было вызвано контекстное меню) на путь к выбранному файлу
                    langAuthors.set(info.position, path);
//создаём хэш-мап и приравниваем его к хэш-мапу из myLangs с номером info.position
                    HashMap<String, Object> tempHash=myLangs.get(info.position);
//удаляем из него данные о картинке с автором
                    tempHash.remove(AUTHORKEY);
//и вставляем новые данные с путем к выбранному графическому файлу из langAuthors
                    tempHash.put(AUTHORKEY, langAuthors.get(info.position));
//заменяем в myLangs элемент с нужным номером на новый, только что созданный
                    myLangs.set(info.position, tempHash);
//сигнализируем адаптеру, что данные изменились
                    listAdapter2.notifyDataSetChanged();
                    putDataToDB();

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data); //метод родительского класса
    }

    protected void showMessage(String str) {
//создаем диалоговое окно, параметр – контекст, который берем у списка
        AlertDialog.Builder builder = new AlertDialog.Builder(langList.getContext());
        builder.setTitle(str); //заголовок диалогового окна
//создаем переменную для нахождения строкового ресурса (см. текст после примера)
//ищем в строковых ресурсах строку с именем, которое совпадает с значением str
//и берем её идентификатор
        int strId = getResources().getIdentifier(str, "string", getPackageName());
        String strValue ="";
//если ресурс был найден, т.е. strId!=0, то по найденному идентификатору получаем значение строки
        if (strId!=0) strValue=getString(strId);
        builder.setMessage(strValue); //задаем содержимое окна
//создаем в окне кнопку ОК и задаем ее функционал
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Закрывает диалоговое окно
            }
        });
        AlertDialog dialog = builder.create(); //создаем диалоговое окно через построитель
        dialog.show(); //показываем диалоговое окно
    }


    @Override //данный метод вызывается при нажатии на любой пункт меню
    public boolean onOptionsItemSelected(MenuItem item) {//item – пункт меню, на который нажали
        switch (item.getItemId()) { //в зависимости от нажатого пункта (по его идентификатору)
            case R.id.about: //если это пункт меню «О программе», то выполняем след. Действия:
                onAboutClick(item); //вызываем метод для показа окна с сообщением (см. ниже)
                break;
            case R.id.addNew: //для пункта меню «Добавить язык»
                onAddNewClick(item); //этот метод нужно создать
                break;
        }
        return super.onOptionsItemSelected(item); //это вставляется по-умолчанию
    }

    private void onAddNewClick(MenuItem item) { //сам метод для вызова нового Activity
        Intent newAct = new Intent(getApplicationContext(), InputActivity.class);
        startActivityForResult(newAct, INPUT_ACTIVITY); //вызываем активити и ждем от него результат
        // второй параметр нужен для отличия результатов при нескольких дополнительных активити

    }

    protected void onSaveInstanceState(Bundle outState) {
        Toast.makeText(langList.getContext(), "saved", Toast.LENGTH_SHORT).show(); //сообщение для отслеживания
        outState.putSerializable("HashMap", myLangs); //помещаем наш основной массив в хранилище
        super.onSaveInstanceState(outState); //вызываем метод родительского класса
    }


    //в строковых ресурсах нужна строка с именем about_content, в ней текст как на рис.23 в окошке
    public void onAboutClick(MenuItem item){ //наш метод для показа диалогового окна с информацией
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.getTitle());
//ищем id строкового ресурса с именем about_content – в нем текст для диалогового окна
        int strId = getResources().getIdentifier("about_content", "string", getPackageName());
        String strValue = ""; //объявляем пустую строку
        if (strId!=0) strValue=getString(strId); //получаем строку с нужным id
        builder.setMessage(strValue);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
