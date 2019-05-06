package com.example.aatik.secondapppart1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList <HashMap<String, Object>> myLangs;
    private final String NAMEKEY= "langname";
    private final String YEARKEY= "langyear";
    private final String PERCENTKEY= "langpercent";
    ArrayList<String> langNames;
    ArrayList<String> langYears;
    ArrayList<String> langPercents;
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
        langNames = new ArrayList(Arrays.asList("Basic", "Pascal", "C", "C++", "C#", "Java", "PHP", "Python", "Objective C"));
        langYears = new ArrayList(Arrays.asList("1964", "1975", "1972", "1983", "2000", "1995", "1994", "1991", "1983"));
        langPercents = new ArrayList(Arrays.asList("10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%"));

        for (String s: langNames) {
            hm = new HashMap<String, Object>();
            hm.put(NAMEKEY,s);
            String year=langYears.get(langNames.indexOf(s));
            hm.put(YEARKEY, year);
            String percent = langPercents.get(langNames.indexOf(s));
            hm.put(PERCENTKEY, percent);
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
                },
                new int[]{
                        R.id.text1,
                        R.id.text2,
                        R.id.text3
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
    }

}
