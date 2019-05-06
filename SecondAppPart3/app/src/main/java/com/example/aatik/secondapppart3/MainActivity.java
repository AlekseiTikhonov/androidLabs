package com.example.aatik.secondapppart3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    ArrayList<String> houseStreet;
    ArrayList<String> houseNumber;
    ArrayList<String> houseApartments;
    EditText edit1;
    EditText edit2;
    EditText edit3;
    ListView houseList;
    SimpleAdapter listAdapter2;
    HashMap<String, Object> hm; //хэш-мап для пар ключ-значение (название поля – значение поля)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLangs = new ArrayList<HashMap<String,Object>>(); //создаем массив
        houseStreet = new ArrayList(Arrays.asList("Чапаева", "Мира", "Ленина", "Дзержинского", "60 лет Октября",
                "Северная", "Интернациональная", "Чапаева", "Ленина"));
        houseNumber = new ArrayList(Arrays.asList("85", "93", "15", "20", "60", "40", "30", "47", "12"));
        houseApartments = new ArrayList(Arrays.asList("193", "100", "95", "46", "90", "68", "74", "88", "92"));

        for (String s: houseStreet) {
            hm = new HashMap<String, Object>();
            hm.put(NAMEKEY,s);
            String year=houseNumber.get(houseStreet.indexOf(s));
            hm.put(YEARKEY, year);
            String percent = houseApartments.get(houseStreet.indexOf(s));
            hm.put(PERCENTKEY, percent);
            myLangs.add(hm);
        }

        edit1=(EditText)findViewById(R.id.editText1);
        edit2=(EditText)findViewById(R.id.editText2);
        edit3=(EditText)findViewById(R.id.editText3);
        houseList = (ListView) findViewById(R.id.houseList);

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
        houseList.setAdapter(listAdapter2);

        Button okButton=(Button)findViewById(R.id.button1);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                houseStreet.add(0, edit1.getText().toString());
                houseNumber.add(0,edit2.getText().toString());
                houseApartments.add(0,edit3.getText().toString());
                hm = new HashMap<String, Object>();
                hm.put(NAMEKEY, houseStreet.get(0));
                hm.put(YEARKEY, houseNumber.get(0));
                hm.put(PERCENTKEY, houseApartments.get(0));
                myLangs.add(0,hm);
                listAdapter2.notifyDataSetChanged();
            }
        });
    }

}
