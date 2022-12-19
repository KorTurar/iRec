package com.example.irec;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MyCanvas extends View {
    private Context context;
    private int counter = 0;
    int[] poses = new int[]{R.drawable.boy_pose_one, R.drawable.boy_pose_two};
    int[] secondPoseTimes = new int[]{1,2,3,10,11,12,16,17,18,25,26};
    int index = 0;
    int duration = 3;
    private Bitmap boy_pose_one, boy_pose_two;
    //private int sy

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context);
        this.context = context;
        boy_pose_one = BitmapFactory.decodeResource(context.getResources(), poses[0]);
        boy_pose_one = Bitmap.createScaledBitmap(boy_pose_one, 163, 517, false);//172,474
        boy_pose_two = BitmapFactory.decodeResource(context.getResources(), poses[1]);
        boy_pose_two = Bitmap.createScaledBitmap(boy_pose_two, 172, 474, false);//172,474
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float[] points = new float[]{ 0, 500, 720, 500};
        canvas.drawLines(points, new Paint());
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.canvas_wall));
        canvas.drawRect(0,0, 720, 600, paint);
        paint.setColor(getResources().getColor(R.color.canvas_floor));
        canvas.drawRect(0,600, 720, 1280, paint);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(250,150, 690, 450, paint );
        paint.setColor(getResources().getColor(R.color.canvas_tv_screen));
        canvas.drawRect(255,155, 685, 445, paint );
        //((Activity)context).getSysUsersAmount;
        String usersAmountStr = "Количество пользователей в системе: "+ "";
        String neededStr = "Необходимо ещё: 3";
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText(usersAmountStr, 285, 200, paint);
        canvas.drawText(neededStr, 285, 230, paint);


        if ((counter%6)==1||(counter%6)==2||(counter%6)==3){
                index=1;
                canvas.drawBitmap(boy_pose_two, 150,443, new Paint());
        }
        else{
                index=0;
                canvas.drawBitmap(boy_pose_one, 150,400, new Paint());
        }



        counter++;
        postInvalidateDelayed(100);
        /*bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_neck);
        bitmap = Bitmap.createScaledBitmap(bitmap, 25, 11, false);
        canvas.drawBitmap(bitmap, 200,530, new Paint());

        Matrix matrix6 = new Matrix();
        matrix6.postRotate(30, 30, 10);
        matrix6.postTranslate(150, 570);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_left_upper_arm);
        bitmap = Bitmap.createScaledBitmap(bitmap, 35, 102, false);
        canvas.drawBitmap(bitmap, matrix6, new Paint());

        Matrix matrix8 = new Matrix();
        matrix8.postRotate(-130, 8, 0);
        matrix8.postTranslate(100, 650);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_left_lower_arm);
        bitmap = Bitmap.createScaledBitmap(bitmap, 26, 120, false);
        canvas.drawBitmap(bitmap, matrix8, new Paint());//150,650

        Matrix matrix7 = new Matrix();
        matrix7.postRotate(30, 10, 10);
        matrix7.postTranslate(210, 570);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_right_upper_arm);
        bitmap = Bitmap.createScaledBitmap(bitmap, 48, 122, false);
        canvas.drawBitmap(bitmap, matrix7, new Paint());

        Matrix matrix9 = new Matrix();
        matrix9.postRotate(-130, 15, 0);
        matrix9.postTranslate(180, 670);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_right_lower_arm);
        bitmap = Bitmap.createScaledBitmap(bitmap, 30, 114, false);
        canvas.drawBitmap(bitmap, matrix9, new Paint());//250,650

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_back);
        bitmap = Bitmap.createScaledBitmap(bitmap, 62, 163, false);
        Matrix matrix = new Matrix();

        matrix.postRotate(30);
        matrix.postTranslate(180,535);
        //bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap, matrix, new Paint());



        Matrix matrix2 = new Matrix();
        matrix2.postRotate(-30, 30, 130);
        matrix2.postTranslate(180, 710);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_left_upper_leg);
        bitmap = Bitmap.createScaledBitmap(bitmap, 28, 122, false);
        canvas.drawBitmap(bitmap, matrix2, new Paint());//180,710

        Matrix matrix4 = new Matrix();
        matrix4.postRotate(30, 15, 105);
        matrix4.postTranslate(130, 820);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_left_lower_leg);
        bitmap = Bitmap.createScaledBitmap(bitmap, 28, 109, false);
        canvas.drawBitmap(bitmap, matrix4, new Paint());

        Matrix matrix3 = new Matrix();
        matrix3.postRotate(-30, 30, 130);
        matrix3.postTranslate(180, 690);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_right_upper_leg);
        bitmap = Bitmap.createScaledBitmap(bitmap, 63, 132, false);
        canvas.drawBitmap(bitmap, matrix3, new Paint());//180,690

        Matrix matrix5 = new Matrix();
        matrix5.postRotate(30, 20, 120);
        matrix5.postTranslate(160, 810);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_right_lower_leg);
        bitmap = Bitmap.createScaledBitmap(bitmap, 41, 126, false);
        canvas.drawBitmap(bitmap, matrix5, new Paint());

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_shoes);
        bitmap = Bitmap.createScaledBitmap(bitmap, 86, 23, false);
        canvas.drawBitmap(bitmap, 150,920, new Paint());
*/

    }
}
