package com.honjane.compress;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.honjane.compress.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    private String mFilePath;
    private ImageView mDisplayIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_path);
        findViewById(R.id.btn_display);
        mDisplayIv = (ImageView) findViewById(R.id.img_view);

    }


    public void onGetPath(View view) {
        try {

           /* InputStream in = getResources().getAssets()
                    .open("test.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            File dirFile = getExternalCacheDir();
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }*/

            //获取图片旋转的角度
//            int degrees = readPictureDegree(Environment.getExternalStorageDirectory() + "/DCIM/Camera/20171207_154228.jpg");
            int degrees = readPictureDegree(Environment.getExternalStorageDirectory() + "/DCIM/Camera/aa.jpg");


            Bitmap  bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/DCIM/Camera/aa.jpg");


            File dirFile = new File(Environment.getExternalStorageDirectory().toString());

            String filename = System.currentTimeMillis() + ".jpg";
            File jpegFile = new File(dirFile, filename);

            mFilePath = jpegFile.getAbsolutePath();
            boolean flag = false;
            if (bitmap != null) {
                flag = ImageUtils.compressBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), mFilePath, 20);
                if(degrees!=0)
                {
                    Bitmap result=bitmapRotate(degrees);
                    mDisplayIv.setImageBitmap(result);
                }
                else
                {
                    onDisplay(mDisplayIv);
                }
            } else {
                Toast.makeText(MainActivity.this, "file not found", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, flag ? "success 文件大小" + (jpegFile.length() / 1024) + "kb" : "fail", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void onDisplay(View view) {
        if (TextUtils.isEmpty(mFilePath)) {
            Toast.makeText(MainActivity.this, "请先压缩图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
        mDisplayIv.setImageBitmap(bitmap);
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 图片旋转
     */
    protected Bitmap bitmapRotate(float degrees) {
        Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
        // 创建一个和原图一样大小的图片
        Matrix matrix = new Matrix();
        // 根据原图的中心位置旋转
        matrix.setRotate(degrees, bitmap.getWidth() / 2,
                bitmap.getHeight() / 2);

        Bitmap afterBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
                bitmap.getHeight(),matrix,true);

        return  afterBitmap;
    }


}
