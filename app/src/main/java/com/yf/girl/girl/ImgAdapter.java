package com.yf.girl.girl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by lenovo on 2016/10/14.
 */

public class ImgAdapter extends BaseAdapter {
   public ArrayList<Imglist> imglists;
    public int layoutId;
    public File cache ;
    LayoutInflater inflater;
    private ImageLoader imageLoader;


    public ImgAdapter(Context context,ArrayList<Imglist> imagelists, int layoutId, File cache) {

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imglists=imagelists;
        this.layoutId=layoutId;
        this.cache=cache;


    }

    @Override
    public int getCount() {

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
        dataHelper helper;
        if(convertView==null){
           convertView= inflater.inflate(layoutId,null);
             imageView=(ImageView)convertView.findViewById(R.id.first_img);
             textView=(TextView) convertView.findViewById(R.id.title);
             helper=new dataHelper(imageView,textView);
            convertView.setTag(helper);
        }else{
             helper=(dataHelper)convertView.getTag();
            imageView=helper.imageView;
            textView=helper.textView;
        }
        Imglist img=imglists.get(position);
        textView.setText(img.title);
       // asyncImageLoad(imageView,img.imgUrl);
       // asyncImageLoader loader=new asyncImageLoader(imageView);
       // loader.execute(img.imgUrl);
       // Log.i("111111",img.title);
       // Log.i("1111111",img.imgUrl);

        imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(img.imgUrl,helper.imageView,new AnimaterDisplayListener());
        return convertView;
    }

private final class asyncImageLoader extends AsyncTask<String,Integer,Uri>{
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


    private  static class AnimaterDisplayListener extends SimpleImageLoadingListener{
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            super.onLoadingComplete(imageUri, view, loadedImage);

        }
    }
    /*
    private void asyncImageLoad(final ImageView imageView, final String imgUrl) {
      final   android.os.Handler ha=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Uri uri=(Uri)msg.obj;
                if(uri!=null){
                    imageView.setImageURI(uri);
                }
            }
        };
       Runnable runnable=new Runnable() {
           @Override
           public void run() {
               Uri uri=ImglistService.getImage(imgUrl,cache);
               ha.sendMessage(ha.obtainMessage(10,uri));
           }
       };
        new Thread(runnable).start();
    }
*/

    private class dataHelper{
        private ImageView imageView;
        public TextView textView;

        public dataHelper(ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
        }
    }



}
