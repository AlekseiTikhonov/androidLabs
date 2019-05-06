package com.example.lab7_Paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyGraphView extends View {
    private Paint mPaint; //объект для параметров рисования графических примитивов
    private Paint mBitmapPaint; //объект для параметров вывода битмапа на холст
    private Bitmap mBitmap; //сам битмап
    private static Canvas mCanvas; //холст


    Path path;               // линия

    public MyGraphView(Context c) { //конструктор
        super(c);
//создаем объект класса Paint для параметров вывода битмапа на холст
        mBitmapPaint = new Paint(Paint.DITHER_FLAG); // Paint.DITHER_FLAG – для эффекта сглаживания
//создаем объект класса Paint для параметров рисования графических примитивов
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //устанавливаем антиалиасинг (сглаживание)
        mPaint.setColor(Color.RED); //цвет рисования
        mPaint.setStyle(Paint.Style.STROKE); //стиль рисования (Paint.Style.STROKE – без заполнения)
        mPaint.setStrokeJoin(Paint.Join.ROUND); //стиль соединения линий (ROUND - скруглённый)
        mPaint.setStrokeCap(Paint.Cap.ROUND); //стиль концов линий (ROUND - скруглённый)
        mPaint.setStrokeWidth(5); //толщина линии рисования


    }
    @Override //метод onSizeChanged вызывается первый раз при создании объекта,
//далее – при изменении размера объекта, нам он нужен для выяснения первичных размеров битмапа
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//создаем битмап с высотой и шириной как у текущего объекта и с параметром Bitmap.Config.ARGB_8888 –
//четырехканальный RGB (прозрачность и 3 цвета)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap); //создаем канвас и связываем его с битмапом
      //  clearCanvas();

       // Toast.makeText(this.getContext(), "onSizeChanged ", Toast.LENGTH_SHORT).show(); //для отладки
    }







    //метод перерисовки объекта, он будет срабатывать каждый раз
    protected void onDraw(Canvas canvas) { //при вызове функции invalidate() текущего объекта
        super.onDraw(canvas);

//отрисовываем на канвасе текущего объекта (не путать с созданным нами канвасом) наш битмап
        canvas.drawBitmap ( mBitmap, 0, 0, mBitmapPaint);
    }
    public void drawCircle() { //метод для рисования круга
        mPaint.setColor(Color.RED);
        mCanvas.drawCircle(100, 100, 50, mPaint);

        invalidate(); //для срабатывания метода onDraw
    }
    public void drawSquare() {//метод для рисования квадрата
        mCanvas.drawRect(200, 200, 300, 300 , mPaint);
        invalidate();
    }
    public void setWidth(int width)
    {
        mPaint.setStrokeWidth(width);
    }

    public void setColor(int color)
    {
        switch (color) {
            case 0:
                mPaint.setColor(Color.RED); //цвет рисования
                break;
            case 1:
                mPaint.setColor(Color.GREEN); //цвет рисования
                break;
            case 2:
                mPaint.setColor(Color.BLUE); //цвет рисования
                break;
        }
    }
    public void setStyle(int style)
    {
        switch (style) {
            case 0:
                mPaint.setStrokeJoin(Paint.Join.ROUND); //стиль соединения линий (ROUND - скруглённый)
                break;
            case 1:
                mPaint.setStrokeJoin(Paint.Join.BEVEL); //стиль соединения линий
                break;
            case 2:
                mPaint.setStrokeJoin(Paint.Join.MITER); //стиль соединения линий
                break;
        }
    }

    public static void clearCanvas() {
        mCanvas.drawColor(Color.WHITE);

    }
    public Bitmap getBitMap() {
        return mBitmap; // возвращаем объект, который мы используем для рисования (см. код выше)
    }

    public void setPicture(String path) { //метод для рисования картинки из файла
//создаем временный битмап из файла
        Bitmap mBitmapFromSdcard = BitmapFactory.decodeFile(path);
        mCanvas.drawBitmap(mBitmapFromSdcard, 0, 0, mPaint); //рисуем его на нашем канвасе
        invalidate();
    }
    @Override //этот метод будет срабатывать при касании нашего объекта пользователем
    public boolean onTouchEvent(MotionEvent event) { //параметр event хранит информацию о событии
        switch (event.getAction()) { // в зависимости от события
            case MotionEvent.ACTION_DOWN: //если пользователь только коснулся объекта
                path = new Path(); //создаем новый объект класса Path для записи линии рисования
                path.moveTo(event.getX(), event.getY()); //перемещаемся к месту касания
                break;
            case MotionEvent.ACTION_MOVE: //если пользователь перемещает палец по экрану
            case MotionEvent.ACTION_UP: //или отпустил палец
                path.lineTo(event.getX(), event.getY()); //проводим линию в объекте path до точки касания
                break;
        }
        if (path != null) { //если объект не нулевой
            mCanvas.drawPath(path, mPaint); //рисуем на канвасе объект path (и все, с ним связанное)
            invalidate(); //для срабатывания метода onDraw
        }
        return true;
    }


}
