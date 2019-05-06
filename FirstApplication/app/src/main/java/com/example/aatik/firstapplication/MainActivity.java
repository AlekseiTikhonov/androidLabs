package com.example.aatik.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView arr;
    TextView average;
    Button calculate;
    EditText arrSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrSize=(EditText)findViewById(R.id.arrSize);
        calculate=(Button)findViewById(R.id.calculate);
        arr=(TextView)findViewById(R.id.arr);
        average=(TextView)findViewById(R.id.average);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = 0;
                float result = 0;
                String strArr = "";
                try {
                    size = Integer.parseInt(arrSize.getText().toString());
                }
                catch (Exception e)
                {

                }
                int[] array = new int[size];
                for (int i = 0; i<array.length; i++)
                {
                    array[i] = (int) (Math.random() * 10 );
                    strArr += array[i] + ",";
                    result += array[i];
                }
                result = result / size;
                if (size>0)
                {
                    arr.setText(strArr);
                    average.setText(Float.toString(result));
                }

            }
        });


    }




}
