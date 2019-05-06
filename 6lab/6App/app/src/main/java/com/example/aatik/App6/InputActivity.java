package com.example.aatik.App6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {
    EditText langName;
    EditText langYear;
    EditText langPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        langName=(EditText)findViewById(R.id.editText1);
        langYear=(EditText)findViewById(R.id.editText2);
        langPercent=(EditText)findViewById(R.id.editText3);

        Button okButton=(Button)findViewById(R.id.button1);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] newItem=new String[3]; //создаем массив, который будем передавать
//записываем в него передаваемые данные
                newItem[0]=langName.getText().toString();
                newItem[1]=langYear.getText().toString();
                newItem[2]=langPercent.getText().toString();
//создаем намерение для передачи результата в главное активити
                Intent intent = new Intent();
                intent.putExtra("newItem", newItem); //записываем в намерение наш массив с данными
                setResult(RESULT_OK, intent); //задаем результат текущего активити
                finish(); //и завершаем текущее активити
            }
        });
    }
}
