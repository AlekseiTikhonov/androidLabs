package com.example.lab7_Paint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    LinearLayout layout1;
    MyGraphView myGraphView;
    EditText myEdit;
    Spinner spinner;
    Spinner styles;
    private final int CHANGE_PICTURE=2;
    String fileName = "myFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myEdit=(EditText)findViewById(R.id.editText);
        myGraphView=new MyGraphView(this); //создаем объект нашего графического класса
        layout1=(LinearLayout)findViewById(R.id.layout1); //находим объект-компоновку
        layout1.addView(myGraphView); //добавляем на компоновку наш объект
        setValues();

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override //данный метод вызывается при нажатии на любой пункт меню
    public boolean onOptionsItemSelected(MenuItem item) {//item – пункт меню, на который нажали
        switch (item.getItemId()) { //в зависимости от нажатого пункта (по его идентификатору)
            case R.id.clear:
                MyGraphView.clearCanvas();
                break;
            case R.id.open:
                onOpenPictureClick();
                break;
            case R.id.save: //для пункта меню «Добавить язык»
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item); //это вставляется по-умолчанию
    }

    public void saveImage(){
        Bitmap saveBitMap= myGraphView.getBitMap(); //получаем картинку с myGraphView
//получаем путь к корневому каталогу на карте памяти
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null; //объявляем поток вывода
        File file = new File(extStorageDirectory + "/Download", fileName + ".PNG"); //создаем файл с нужным путем и названием
        try { //все действия с файлами должны быть внутри секции try - catch
            outStream = new FileOutputStream(file); //создаем объект потока и связываем его с файлом
//у нашего битмапа вызываем функцию для записи его с нужными параметрами (тип графического файла,
//качество в процентах и поток для записи)
            saveBitMap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush(); //для прохождения данных вызываем функцию flush у потока
            outStream.close(); //закрываем поток
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        } catch(Exception e) { //в случае срабатывания исключения выводим сообщение на экран
//текст сообщения берем из ресурсов, для английского варианта это может быть строка
// <string name="save_problem">Save "can't" be done (maybe problem with sdcard</string>
//для русского
//<string name="save_problem">Сохранение невозможно (могут быть проблемы с картой памяти)</string>
            Toast.makeText(this, R.string.save_problem, Toast.LENGTH_SHORT).show();
        }
    }
    public void showDialog ()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alert.setMessage("Please enter a file name");
        alert.setTitle("File name");

        alert.setView(edittext);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                if  (edittext.getText().toString().length() > 0)
                {
                    fileName = edittext.getText().toString();
                }
                saveImage();
            }
        });



        alert.show();
    }


    private void onOpenPictureClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //создаем намерение для выбора файла
        intent.setType("file/*"); // указываем тип открываемых ресурсов
        startActivityForResult(intent, CHANGE_PICTURE); //стартуем активити и ждем от него
    } //результата с кодом CHANGE_PICTURE

    public void onCircleClick(View view){
    //    Toast.makeText(this, "Circle", Toast.LENGTH_SHORT).show();
        myGraphView.drawCircle(); //вызываем функцию для рисования окружности
    }
    public void onSquareClick(View view){
     //   Toast.makeText(this, "Square", Toast.LENGTH_SHORT).show();
        myGraphView.drawSquare(); //вызываем функцию для рисования квадрата
    }

    public void setValues() {
         spinner = (Spinner) findViewById(R.id.spinner);
         styles = (Spinner) findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.styles_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styles.setAdapter(adapter2);

        myEdit.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                try
                                {
                                    String str = myEdit.getText().toString();
                                    int width =Integer.parseInt(str);
                                    myGraphView.setWidth(width);
                                }
                                catch (Exception e)
                                {

                                }
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               myGraphView.setColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        styles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Spinner item", Toast.LENGTH_SHORT).show();
                myGraphView.setStyle(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) { //оператор выбора для определения, от какого активити пришел результат
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
                    try
                    {
                        myGraphView.setPicture(path);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Try to open another file" + path,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data); //метод родительского класса
    }
}
