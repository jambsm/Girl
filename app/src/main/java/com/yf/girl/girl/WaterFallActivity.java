package com.yf.girl.girl;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/20.
 */

public class WaterFallActivity extends AppCompatActivity {
   WaterFallAdapter adapter;
    ArrayList<Imglist> data=null,imgdatalist=null;
    RecyclerView mRecycle=null;
    File cache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waterfall);

        cache=new File(Environment.getExternalStorageDirectory(),"cache");


        if(!cache.exists()){
            cache.mkdir();
        }

        mRecycle=(RecyclerView)findViewById(R.id.recycle);
        mRecycle.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        imgdatalist=new ArrayList<Imglist>();
        asyncTask astask=new asyncTask();
        astask.execute(getIntent().getStringExtra("imgPath"));

    }



    private class asyncTask extends AsyncTask<String,Integer,ArrayList<Imglist>> {

        @Override
        protected ArrayList<Imglist> doInBackground(String... params) {
            data=ImglistService.getDetailList(params[0]);
            ImglistService.preLoadImage(data,cache);
           // Log.i("data size",data.size()+"");
            return data;
        }

        @Override
        protected void onPostExecute(final ArrayList<Imglist> imglists) {
            super.onPostExecute(imglists);

            WaterFallAdapter adapter=new WaterFallAdapter(imglists,R.layout.waterfall_detail,cache,WaterFallActivity.this);

            adapter.setItemActionListener(new WaterFallAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    //Toast.makeText(WaterFallActivity.this,pos+"click",Toast.LENGTH_LONG).show();
                    Imglist img=imglists.get(pos);
                    String path=img.targetUrl;
                    File LocalFile=new File(cache,MD5Util.MD5(path)+path.substring(path.lastIndexOf(".")));
                    Intent intent=new Intent(WaterFallActivity.this,largeImageActivity.class);
                    intent.putExtra("ImgPath",LocalFile.toString());
                    startActivity(intent);

                }
            });

            mRecycle.setAdapter(adapter);



        }
    }

}
