package com.yf.girl.girl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView listview;
    ImgAdapter adapter=null;
    ArrayList<Imglist> data;
    File cache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cache=new File(Environment.getExternalStorageDirectory(),"cache");


        if(!cache.exists()){
            cache.mkdir();
        }

        listview=(ListView)findViewById(R.id.img_list);
        if(isNetWorkOn(this)){
            asyncTask astask=new asyncTask();
            astask.execute();
        }else {
            AlertDialog.Builder mbuilder=new AlertDialog.Builder(this);
            mbuilder.setMessage("无网络连接,确认退出").setTitle("提示");
            mbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MainActivity.this.finish();
                }
            });
            mbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mbuilder.create().show();
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                Intent intent=new Intent(MainActivity.this,WaterFallActivity.class);
                intent.putExtra("imgPath",data.get(position).targetUrl);
                startActivity(intent);
            }
        });

/*
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {

                Log.i("msg obj",(ArrayList<Imglist>)msg.obj+"");
                adapter=new ImgAdapter(MainActivity.this,(ArrayList<Imglist>) msg.obj,R.layout.list_item,cache);

                listview.setAdapter(adapter);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    data=ImglistService.getImgList();
                    Log.i("Get list data ",data.size()+"");
                    handler.sendMessage(handler.obtainMessage(22,data));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
*/

    }

    private class asyncTask extends AsyncTask<String,Integer,ArrayList<Imglist>>{

        @Override
        protected ArrayList<Imglist> doInBackground(String... params) {
            data=ImglistService.getImgList();
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Imglist> imglists) {
            super.onPostExecute(imglists);
            listview.setAdapter(new ImgAdapter(MainActivity.this,imglists,R.layout.list_item,cache));
        }
    }

public boolean isNetWorkOn(Context context){
if(context!=null){
    ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mInfo=manager.getActiveNetworkInfo();
    if(mInfo!=null){
        return mInfo.isAvailable();
    }
}
    return false;

}


}
