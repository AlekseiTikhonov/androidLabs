package com.example.aatik.ThirdApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList <HashMap<String, Object>> myLangs;
    private final String NAMEKEY= "langname";
    private final String YEARKEY= "langyear";
    private final String PERCENTKEY= "langpercent";
    private final String AUTHORKEY= "langauthor";
    ArrayList<String> langNames;
    ArrayList<String> langYears;
    ArrayList<String> langPercents;
    ArrayList<Object> langAuthors;
    EditText edit1;
    EditText edit2;
    EditText edit3;
    ListView langList;
    SimpleAdapter listAdapter2;
    HashMap<String, Object> hm; //хэш-мап для пар ключ-значение (название поля – значение поля)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLangs = new ArrayList<HashMap<String,Object>>(); //создаем массив
        langNames = new ArrayList(Arrays.asList("Basic", "Pascal", "C", "Cplusplus", "Csharp", "Java", "PHP", "Python", "ObjectiveC"));
        langYears = new ArrayList(Arrays.asList("1964", "1975", "1972", "1983", "2000", "1995", "1994", "1991", "1983"));
        langPercents = new ArrayList(Arrays.asList("10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%"));
        langAuthors=new ArrayList<Object>(Arrays.asList(R.drawable.basic, R.drawable.pascal,
                R.drawable.c, R.drawable.cpp, R.drawable.c_sharp, R.drawable.java, R.drawable.php,
                R.drawable.python, R.drawable.no_picture));

        for (String s: langNames) {
            hm = new HashMap<String, Object>();
            hm.put(NAMEKEY,s);
            String year=langYears.get(langNames.indexOf(s));
            hm.put(YEARKEY, year);
            String percent = langPercents.get(langNames.indexOf(s));
            hm.put(PERCENTKEY, percent);
            Object author=langAuthors.get(langNames.indexOf(s));
            hm.put(AUTHORKEY,author); //записываем в хэш-мап автора языка
            myLangs.add(hm);
        }

        edit1=(EditText)findViewById(R.id.editText1);
        edit2=(EditText)findViewById(R.id.editText2);
        edit3=(EditText)findViewById(R.id.editText3);
        langList = (ListView) findViewById(R.id.langList);

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

        Button okButton=(Button)findViewById(R.id.button1);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                langNames.add(0, edit1.getText().toString());
                langYears.add(0,edit2.getText().toString());
                langPercents.add(0,edit3.getText().toString());
                hm = new HashMap<String, Object>();
                hm.put(NAMEKEY, langNames.get(0));
                hm.put(YEARKEY, langYears.get(0));
                hm.put(PERCENTKEY, langPercents.get(0));
                myLangs.add(0,hm);
                listAdapter2.notifyDataSetChanged();
            }
        });

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

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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


}
