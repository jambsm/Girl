package com.yf.girl.girl;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/16.
 */

public class ListDetailAdapter extends BaseAdapter {
    public ArrayList<Imglist> imglists;
    public int layoutId;
    public File cache ;
    LayoutInflater inflater;

    public ListDetailAdapter(Context context,ArrayList<Imglist> imglists, int layoutId, File cache) {
        this.imglists = imglists;
        this.layoutId = layoutId;
        this.cache = cache;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public int getCount() {
        Log.i("get list size",imglists.size()+"");
        return imglists.size();

    }

    @Override
    public Object getItem(int position) {
        return imglists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        if(convertView==null){
            convertView= inflater.inflate(layoutId,null);
            imageView=(ImageView)convertView.findViewById(R.id.item_img);
            textView=(TextView) convertView.findViewById(R.id.item_title);
            dataHelper helper=new dataHelper(imageView,textView);
            convertView.setTag(helper);
        }else{
            dataHelper helper=(dataHelper)convertView.getTag();
            imageView=helper.imageView;
            textView=helper.textView;
        }
        Imglist img=imglists.get(position);
        textView.setText(img.title);

        asyncImageLoader loader=new asyncImageLoader(imageView);
        loader.execute(img.targetUrl);
        return convertView;
    }

    private final class asyncImageLoader extends AsyncTask<String,Integer,Uri> {
        private ImageView imageView;

        public asyncImageLoader(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override

        protected Uri doInBackground(String... params) {
            return ImglistService.getImage(params[0],cache);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            imageView.setImageURI(uri);
        }
    }


    private class dataHelper{
        public ImageView imageView;
        public TextView textView;

        public dataHelper(ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
        }
    }
}
