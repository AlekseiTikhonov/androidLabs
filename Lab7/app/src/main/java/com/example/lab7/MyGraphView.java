package com.example.lab7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

public class MyGraphView extends View {
    private Paint mPaint; //объект для параметров рисования графических примитивов
    private Paint mBitmapPaint; //объект для параметров вывода битмапа на холст
    private Bitmap mBitmap; //сам битмап
    private Canvas mCanvas; //холст
    public MyGraphView(Context c) { //конструктор
        super(c);
//создаем объект класса Paint для параметров вывода битмапа на холст
        mBitmapPaint = new Paint(Paint.DITHER_FLAG); // Paint.DITHER_FLAG – для эффекта сглаживания
//создаем объект класса Paint для параметров рисования графических примитивов
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //устанавливаем антиалиасинг (сглаживание)
        mPaint.setColor(Color.GREEN); //цвет рисования
        mPaint.setStyle(Paint.Style.STROKE); //стиль рисования (Paint.Style.STROKE – без заполнения)
        mPaint.setStrokeJoin(Paint.Join.ROUND); //стиль соединения линий (ROUND - скруглённый)
        mPaint.setStrokeCap(Paint.Cap.ROUND); //стиль концов линий (ROUND - скруглённый)
        mPaint.setStrokeWidth(12); //толщина линии рисования
    }
    @Override //метод onSizeChanged вызывается первый раз при создании объекта,
//далее – при изменении размера объекта, нам он нужен для выяснения первичных размеров битмапа
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//создаем битмап с высотой и шириной как у текущего объекта и с параметром Bitmap.Config.ARGB_8888 –
//четырехканальный RGB (прозрачность и 3 цвета)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap); //создаем канвас и связываем его с битмапом
        Toast.makeText(this.getContext(), "onSizeChanged ", Toast.LENGTH_SHORT).show(); //для отладки
    }
    //метод перерисовки объекта, он будет срабатывать каждый раз
    protected void onDraw(Canvas canvas) { //при вызове функции invalidate() текущего объекта
        super.onDraw(canvas);

//отрисовываем на канвасе текущего объекта (не путать с созданным нами канвасом) наш битмап
        canvas.drawBitmap ( mBitmap, 0, 0, mBitmapPaint);
    }
    public void drawCircle() { //метод для рисования круга
        mCanvas.drawCircle(100, 100, 50, mPaint);
        invalidate(); //для срабатывания метода onDraw
    }
    public void drawSquare() {//метод для рисования квадрата
        mCanvas.drawRect(200, 200, 300, 300 , mPaint);
        invalidate();
    }
    public void drawFace() { //метод для рисования картинки из файла
//создаем временный битмап из файла
        Bitmap mBitmapFromSdcard = BitmapFactory.decodeFile("/storage/sdcard0/download/face.png");
        mCanvas.drawBitmap(mBitmapFromSdcard, 100, 100, mPaint); //рисуем его на нашем канвасе
        invalidate();
    }
}
