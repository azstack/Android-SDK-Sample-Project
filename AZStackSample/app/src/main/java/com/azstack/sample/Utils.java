package com.azstack.sample;

import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.azstack.AzStackClient;
import com.azstack.database.model.AzContact;
import com.azstack.exception.AzStackException;
import com.azstack.listener.AzStackAuthenticateListener;
import com.azstack.listener.AzStackConnectListener;
import com.azstack.listener.AzStackUserListener;

import sample.azstack.azstacksample.R;

public class Utils {
    public static void connectAZStackForGCM(final AzStackClient azStackClient,
                                            final Context context) {
        AzStackClient.getInstance().registerConnectionListenter(new AzStackConnectListener() {
            @Override
            public void onConnectionConnected(AzStackClient client) {
                Log.i("AzStack", "onConnectionConnected");
            }

            @Override
            public void onConnectionDisconnected(AzStackClient client) {
                Log.i("AzStack", "onConnectionDisconnected");
            }

            @Override
            public void onConnectionError(AzStackClient client, AzStackException e) {
                Log.i("AzStack", "onConnectionError");
            }
        });
        AzStackClient.getInstance().registerUserListener(new AzStackUserListener() {
            @Override
            public void getUserInfo(String azStackUserId, int purpose) {
                try {
                    JSONObject ob = new JSONObject();
                    ob.put("azStackUserId", azStackUserId);
                    ob.put("name", "name_" + azStackUserId);
                    AzStackClient.getInstance().getUserInfoComplete(ob, purpose);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void viewUserInfo(String azStackUserId) {
            }

            @Override
            public JSONArray getListFriend() {
                JSONArray array = new JSONArray();
                try {
                    String[] azStackUserIds = context.getResources().getStringArray(com.azstack.R.array.azstack_list_azstack_userid);
                    for (String azStackUserId : azStackUserIds) {
                        JSONObject ob = new JSONObject();
                        ob.put("azStackUserId", azStackUserId);
                        ob.put("name", "name_" + azStackUserId);
                        array.put(ob);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return array;
            }
        });

        azStackClient.connect(Config.my_azstack_userid, Config.my_name);
    }
}
