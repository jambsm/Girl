package com.yf.girl.girl;



import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2016/10/14.
 */
public class ImglistService {
    static  ArrayList<Imglist> GirlsList=null;
    static Imglist girllist=null;
    public static ArrayList<Imglist> getImgList() {
        String httpurl="http://apis.baidu.com/txapi/mvtp/meinv";
        String httpargs="num=10";
        httpurl=httpurl+"?"+httpargs;

        try {
            URL url=new URL(httpurl);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey","456f9c6efec8f68ac1cd03df7a5e7c06");
            connection.connect();
            if(connection.getResponseCode()==200){
                JSONObject jsonObject=new JSONObject(getJsonString(connection.getInputStream()));

                JSONArray result=jsonObject.getJSONArray("newslist");
                GirlsList=new ArrayList<Imglist>();
                for(int i=0;i<result.length();i++){
                JSONObject singleJson=result.getJSONObject(i);
                    String title=singleJson.getString("title");
                    String picUrl=singleJson.getString("picUrl");
                    String targeturl=singleJson.getString("url");
                    girllist=new Imglist(title,picUrl,targeturl);

                    GirlsList.add(girllist);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//Log.i("Size:",GirlsList.size()+"");
        return GirlsList;
    }

    static String getJsonString(InputStream in){
        BufferedReader reader=null;
        StringBuilder sb=new StringBuilder();
        String line=null;
        try {
            reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
            while ((line=reader.readLine())!=null){
                sb.append(line);
                sb.append("\r\n");
            }
            in.close();
        }catch (Exception e){

        }
//Log.i("JSon数据",sb.toString());
        return sb.toString();

    }

   static public void preLoadImage(final ArrayList<Imglist> dataList,final File cache){

        ExecutorService executorService= Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (Imglist list:dataList) {
                 String  path=list.targetUrl;
                    File LocalFile=new File(cache,MD5Util.MD5(path)+path.substring(path.lastIndexOf(".")));
                    InputStream in=null;
                    FileOutputStream out=null;
                    if(LocalFile.exists()){
                        return;
                    }else {
                        try {
                            HttpURLConnection connection=(HttpURLConnection)new URL(path).openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("GET");
                            connection.connect();
                            if(connection.getResponseCode()==200){
                                out=new FileOutputStream(LocalFile);
                                in=connection.getInputStream();
                                byte buffer[]=new byte[1024];
                                int len=0;
                                while((len=in.read(buffer))!=-1){
                                    out.write(buffer,0,len);
                                }
                                in.close();
                                out.close();
                                Uri.fromFile(LocalFile);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    
                }

            }
        });
    }

    public static Uri getImage(String path,File cache) {
      //  Log.i("file ",path);
       File LocalFile=new File(cache,MD5Util.MD5(path)+path.substring(path.lastIndexOf(".")));
        InputStream in=null;
        FileOutputStream out=null;
        if(LocalFile.exists()){
            return  Uri.fromFile(LocalFile);
        }else {
            try {
                HttpURLConnection connection=(HttpURLConnection)new URL(path).openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                if(connection.getResponseCode()==200){
                     out=new FileOutputStream(LocalFile);
                     in=connection.getInputStream();
                    byte buffer[]=new byte[1024];
                    int len=0;
                    while((len=in.read(buffer))!=-1){
                        out.write(buffer,0,len);
                    }
                    in.close();
                    out.close();
                    Uri.fromFile(LocalFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

return null;
    }

    public static ArrayList<Imglist> getDetailList(String path) {

        URL url=null;
        HttpURLConnection connection= null;
        try {
            url=new URL(path);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            connection.connect();
            GirlsList=new ArrayList<Imglist>();
            if(connection.getResponseCode()==200){
                String mXiaoXiao="http://m.xxxiao.com";
                String m2727="http://www.27270.com";
               // Log.i("path",path);

                if(path.startsWith(mXiaoXiao)){

                    String html=getJsonString(connection.getInputStream());
                    Document doc= Jsoup.parse(html);
                    Elements divcontent=doc.select("div[class=rgg-imagegrid gallery]");
                    Elements imgurls=divcontent.select("img");

                   // Log.i("22",imgurls.toString());
                    for(Element imgurl:imgurls){
                        String ImgUrl=imgurl.attr("src");
                        //Log.i("11111",ImgUrl+"");
                        girllist=new Imglist(null,ImgUrl,ImgUrl);
                        GirlsList.add(girllist);

                    }

                }else if(path.startsWith(m2727)){
                    String baseUrl=path.substring(0,path.lastIndexOf("."));
                    Log.i("baseUrl",baseUrl);

                    URL strImg=null;
                    HttpURLConnection conn=null;
                    for(int i=2;i<=10;i++){
                        String strDetailUrl=baseUrl;
                        strDetailUrl=strDetailUrl+"_"+i+".html";
                       // Log.i("strurl",strDetailUrl);
                        strImg=new URL(strDetailUrl);

                        conn=(HttpURLConnection)strImg.openConnection();
                        conn.setRequestMethod("GET");
                        conn.connect();
                        if(conn.getResponseCode()==200){
                            String html=getJsonString(conn.getInputStream());
                            Document doc=Jsoup.parse(html);
                            Elements imgElements=doc.select("a[href]");
                           // Log.i("Get element",imgElements.toString()+"111");
                            Elements  imageUrl=imgElements.select("img[alt]");
                            String strUrl=imageUrl.attr("src");
                            //Log.i("Str URL",strUrl);
                            girllist=new Imglist(null,strUrl,strUrl);
                            GirlsList.add(girllist);
                        }


                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  GirlsList;
    }
}
