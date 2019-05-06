package com.example.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    LinearLayout layout1;
    MyGraphView myGraphView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myGraphView=new MyGraphView(this); //создаем объект нашего графического класса
        layout1=(LinearLayout)findViewById(R.id.layout1); //находим объект-компоновку
        layout1.addView(myGraphView); //добавляем на компоновку наш объект

    }

    public void onCircleClick(View view){
        Toast.makeText(this, "Circle", Toast.LENGTH_SHORT).show();
        myGraphView.drawCircle(); //вызываем функцию для рисования окружности
    }
    public void onSquareClick(View view){
        Toast.makeText(this, "Square", Toast.LENGTH_SHORT).show();
        myGraphView.drawSquare(); //вызываем функцию для рисования квадрата
    }
    public void onFaceClick(View view){
        Toast.makeText(this, "Face", Toast.LENGTH_SHORT).show();
        myGraphView.drawFace(); //вызываем функцию для рисования лица
    }
}
