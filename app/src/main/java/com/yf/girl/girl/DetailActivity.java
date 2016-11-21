package com.yf.girl.girl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/16.
 */

public class DetailActivity extends AppCompatActivity {
    ListView listview;
    ListDetailAdapter adapter=null;
    ArrayList<Imglist> data;
    File cache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        listview=(ListView)findViewById(R.id.detail_list);
        cache=new File(Environment.getExternalStorageDirectory(),"cache");


        if(!cache.exists()){
            cache.mkdir();
        }

        asyncTask astask=new asyncTask();
        astask.execute(getIntent().getStringExtra("imgPath"));
    }

    private class asyncTask extends AsyncTask<String,Integer,ArrayList<Imglist>> {

        @Override
        protected ArrayList<Imglist> doInBackground(String... params) {
            data=ImglistService.getDetailList(params[0]);
            Log.i("data size",data.size()+"abc");
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Imglist> imglists) {
            super.onPostExecute(imglists);
            listview.setAdapter(new ListDetailAdapter(DetailActivity.this,imglists,R.layout.item_list_detail,cache));
        }
    }
}
