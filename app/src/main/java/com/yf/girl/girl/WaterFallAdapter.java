package com.yf.girl.girl;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/20.
 */

public class WaterFallAdapter extends RecyclerView.Adapter<WaterFallAdapter.WaterFallViewHolder> {
    public ArrayList<Imglist> imglists;
    public int layoutId;
    public File cache ;
    LayoutInflater inflater;
    OnItemClickListener mListener;

    public WaterFallAdapter(ArrayList<Imglist> data, int layoutId, File cache, Context context) {
        this.imglists = data;
        this.layoutId = layoutId;
        this.cache = cache;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public WaterFallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.waterfall_detail,parent,false);
        WaterFallViewHolder holder=new WaterFallViewHolder(v,mListener);
        holder.setIsRecyclable(true);
        Log.i("ccc","aaaa");

        return holder;
    }

    @Override
    public void onBindViewHolder(final WaterFallViewHolder holder, final int position) {
          Imglist img=imglists.get(position);
          holder.textView.setText(img.title);
        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // int pos=holder.getPosition();
                    
                    mListener.onItemClick(v,position);
                }
            });
        }

        // asyncImageLoader loader=new asyncImageLoader(holder.imageView);
        Log.i("bbb","aaaa");
       // loader.execute(img.targetUrl);
        ImageLoader  imageLoader= ImageLoader.getInstance();
        imageLoader.displayImage(img.imgUrl,holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imglists.size();
    }


    private final class asyncImageLoader extends AsyncTask<String,Integer,Uri> {
        private ImageView imageView;

        public asyncImageLoader(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override

        protected Uri doInBackground(String... params) {
            Log.i("params",params[0]);
            return ImglistService.getImage(params[0],cache);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            imageView.setImageURI(uri);
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View v,int pos);
    }
    public void setItemActionListener(OnItemClickListener OnItemClickListener){
        this.mListener=OnItemClickListener;
    }

    class WaterFallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public ImageView imageView;
        public  OnItemClickListener mListener;

        public WaterFallViewHolder(View layout,OnItemClickListener listener) {
            super(layout);
            this.imageView = (ImageView)layout.findViewById(R.id.water_img);
            this.textView = (TextView)layout.findViewById(R.id.water_title);
            mListener=listener;
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v,getPosition());
        }
    }

}


