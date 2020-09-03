package com.laira.hias.classes;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.laira.hias.GeniSysAiActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class hias {

    private Application application;
    private Context context;
    private RequestQueue requestQueue;
    private TextToSpeech tts;

    private ProgressBar pb;

    public hias(Application application, Context context, View view, TextToSpeech tts, ProgressBar pb){
        this.application = application;
        this.context = context;
        this.pb = pb;
        this.tts = tts;
    }

    public void loginCall(View v) {

        JSONObject json = new JSONObject();

        try {
            json.put("uname", global.getUname());
            json.put("upass", global.getUpass());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String fullURL = global.getServer() + global.getLoginEndpoint();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullURL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String responseStatus = response.getString("Response");
                            String responseMessage = response.getString("Message");

                            if(responseStatus.equals("OK")){
                                JSONObject jsnobj = response.getJSONObject("Data");
                                global.setUID(jsnobj.getInt("UID"));
                                global.setAID(jsnobj.getInt("AID"));
                                global.setAPB(jsnobj.getString("APB"));
                                global.setAPV(jsnobj.getString("APV"));
                                speech.ConvertTextToSpeech("Welcome " + jsnobj.getString("UN"),
                                        tts);
                                Intent intent = new Intent(context, GeniSysAiActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                application.startActivity(intent);
                            } else {
                                global.setUname("");
                                global.setUpass("");
                                speech.ConvertTextToSpeech(responseMessage, tts);
                                Toast.makeText(context,responseMessage,Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String rerror = "There was an error, please check your credentials and try again";
                Toast.makeText(context, rerror, Toast.LENGTH_SHORT).show();
                speech.ConvertTextToSpeech(rerror, tts);
                pb.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag("HIAS");
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public void nluCall(View v, String heard) {

        JSONObject json = new JSONObject();

        try {
            json.put("uid", global.getUID());
            json.put("uname", global.getUname());
            json.put("upass", global.getUpass());
            json.put("query", heard);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String fullURL = global.getServer() + global.getNluEndpoint();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullURL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String responseStatus = response.getString("Response");
                            String responseMessage = response.getString("Message");

                            if(responseStatus.equals("OK")){
                                JSONObject jsnobj = response.getJSONObject("Data");
                                boolean isAudio = response.getBoolean("Audio");
                                String finalResp = jsnobj.getString("Response");
                                if(!isAudio)
                                {
                                    speech.ConvertTextToSpeech(finalResp, tts);
                                }
                            } else {
                                speech.ConvertTextToSpeech(responseMessage, tts);
                                Toast.makeText(context,responseMessage,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String authKeys = global.getUname() + ":" + global.getAPV();
                String auth = Base64.encodeToString(authKeys.getBytes(),
                        Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Basic " + auth);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag("HIAS");
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
