package com.starlead.starleadhprecision.Loader;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.starlead.starleadhprecision.entity.mapper.RowMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PatrolSchemeLoader extends HandlerThread {
    private static final String TAG = "PatrolSchemeLoader";
    private int MESSAGE_DOWNLOAD = 1;
    private int MESSAGE_LIST = 2;
    private Handler mRequestHandler;
    private Handler mResponseHandler;

    public PatrolSchemeLoader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    final DataInformation dataInformation = (DataInformation) msg.obj;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Log.i(TAG, "run: " + dataInformation.url);
                            Request request = new Request.Builder().url(dataInformation.url).build();
                            Response response = null;
                            JSONObject jsonObject = null;
                            List list;
                            try {

                                response = new OkHttpClient().newCall(request).execute();
                                jsonObject = new JSONObject(response.body().string());
                                list = dataInformation.RowMapper.mapRow(jsonObject);
                                mResponseHandler.obtainMessage( dataInformation.Type, list).sendToTarget();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();


                }
            }
        };
    }



    public void queuePatrolScheme(RowMapper rowMapper, String url,int Type) {

        String URL = "http://192.168.1.102:8080/" +url;

        mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, new DataInformation(URL,rowMapper,Type)).sendToTarget();
    }

    class DataInformation{
        String url;
        int Type;
        RowMapper RowMapper;


        public DataInformation(String url, RowMapper rowMapper,int Type) {
            this.url = url;
            RowMapper = rowMapper;
            this.Type = Type;
        }
    }
}
