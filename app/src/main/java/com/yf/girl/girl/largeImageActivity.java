package com.yf.girl.girl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import android.view.View.OnTouchListener;

import java.io.File;

/**
 * Created by lenovo on 2016/10/24.
 */

public class largeImageActivity extends AppCompatActivity {



    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.mytheme);

        setContentView(R.layout.dialog_show);
        ImageView largeimage=(ImageView)findViewById(R.id.myswitcher);
        File file=new File(getIntent().getStringExtra("ImgPath"));
        largeimage.setImageURI(Uri.fromFile(file));

    }

}
