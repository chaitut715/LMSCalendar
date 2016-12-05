package com.chaitu.lmscalendar.sakai.util;

import android.util.Log;

import com.chaitu.lmscalendar.settings.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.protocol.HttpClientContext;
import cz.msebera.android.httpclient.impl.client.BasicCookieStore;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by CHAITU on 11/14/2016.
 */

public class SakaiUtil{


    public static final String TAG = "SakaiUtil";
    ObjectMapper mapper = new ObjectMapper();
    public static String getEventsResponse(final String url, final String username, final String password) {
        try {
            Log.i(TAG, "doInBackground:1");
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("_username", username));
            params.add(new BasicNameValuePair("_password", password));

            //create httpclient
            HttpClient httpClient = HttpClientBuilder.create().build();
            Log.i(TAG, "doInBackground:2");
            //store cookies in context
            CookieStore cookieStore = new BasicCookieStore();
            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
            Log.i(TAG, "doInBackground:3");
            //call login web service
            String webserviceURL = url+"/direct/session/new";
            HttpPost postRequest = new HttpPost(webserviceURL);
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(postRequest, httpContext);
            Log.i(TAG, "doInBackground:4");
            //get the status code
            int status = response.getStatusLine().getStatusCode();
            Log.i(TAG, "doInBackground:status:"+status);
            //if status code is 201 login is successful. So reuse the httpContext for next requests.
            //if status code is not 201, there is a problem with the request.
            if(status!=201)
                return null;
            EntityUtils.consume( response.getEntity() );
            Log.i(TAG, "doInBackground:5");
            String responseVal = getCalendarData(httpContext, url);
            //Log.i(TAG, "responseVal:"+responseVal);
            //CalendarResponse calendarResponse = formatJSON(responseVal);
            return responseVal;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean authenticate(final String url, final String username, final String password) throws IOException {
        Log.i(TAG, "doInBackground:1");
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("_username", username));
        params.add(new BasicNameValuePair("_password", password));

        //create httpclient
        HttpClient httpClient = HttpClientBuilder.create().build();
        Log.i(TAG, "doInBackground:2");
        //store cookies in context
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        Log.i(TAG, "doInBackground:3");
        //call login web service
        String webserviceURL = url+"/direct/session/new";
        HttpPost postRequest = new HttpPost(webserviceURL);
        postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = httpClient.execute(postRequest, httpContext);
        Log.i(TAG, "doInBackground:4");
        //get the status code
        int status = response.getStatusLine().getStatusCode();
        Log.i(TAG, "doInBackground:status:"+status);
        //if status code is 201 login is successful. So reuse the httpContext for next requests.
        //if status code is not 201, there is a problem with the request.
        if(status!=201)
            return false;
        else
            return true;
    }
    protected static String getCalendarData(HttpContext httpContext, final String url) throws IOException {
        Log.i(TAG, "getCalendarData:");
        HttpClient httpClient = HttpClientBuilder.create().build();
        String webserviceURL = url+"/direct/calendar/my.json";
        HttpGet getRequest = new HttpGet(webserviceURL);

        HttpResponse response = httpClient.execute(getRequest, httpContext);

        HttpEntity httpEntity = response.getEntity();
        String responseVal = EntityUtils.toString(httpEntity);
        //Log.i(TAG, "responseVal:"+responseVal);
        EntityUtils.consume(httpEntity);
        return  responseVal;
    }

}
