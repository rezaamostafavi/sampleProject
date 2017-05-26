package com.sharifin.sharif.sampleproject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mostafavi on 5/26/2017.
 */

public class PostRequest {


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client;
    private Context context;

    public void postRequest(final Context context, final String url, final JSONObject params, final String token, final ResponseReceiver responseReceiver, boolean showLoading) {
        ProgressDialog dialog = null;
        if (showLoading)
            dialog = ProgressDialog.show(context, "", context.getString(R.string.please_wait), true, false);
        final ProgressDialog finalDialog = dialog;
        new Thread() {
            @Override
            public void run() {
                try {
                    String response = null;
                    if (params != null)
                        response = post(url, params.toString(), token);
                    else
                        response = post(url, token);
                    if (context instanceof Activity)
                        response((Activity) context, responseReceiver, response, finalDialog);
                    else
                        response(context, responseReceiver, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (responseReceiver != null) {
                        if (context instanceof Activity)
                            response((Activity) context, responseReceiver, null, finalDialog);
                        else
                            response(context, responseReceiver, null);
                    }
                }
            }
        }.start();
    }

    private void response(final Activity context, final ResponseReceiver responseReceiver, final String responseData, final Dialog finalDialog) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (finalDialog != null)
                    finalDialog.dismiss();
                if (responseData != null)
                    Log.d("@PostResponse", responseData);
                responseReceiver.getResponse(context, responseData);
            }
        });
    }

    private void response(final Context context, final ResponseReceiver responseReceiver, final String responseData) {
//        if (responseData != null)
//            Log.d("@PostResponse", responseData);
        responseReceiver.getResponse(context, responseData);
    }

    private String post(String url, String json, String token) throws IOException {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder post = new Request.Builder()
                .url(url)
                .post(body);
        if (!token.isEmpty())
            post.addHeader("token", token);
        Request request = post
                .build();
        Log.d("@PostRequest", json);
        Call call = client.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }

    private String post(String url, String token) throws IOException {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS).build();
        Request.Builder post = new Request.Builder()
                .url(url);
        if (!token.isEmpty())
            post.addHeader("token", token);
        Request request = post
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }


    public interface ResponseReceiver {

        void getResponse(Object sender, String result);
    }

}
