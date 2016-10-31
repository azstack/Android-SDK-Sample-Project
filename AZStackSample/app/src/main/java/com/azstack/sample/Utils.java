package com.azstack.sample;

import android.content.Context;
import android.util.Log;

import com.azstack.AzStackClient;
import com.azstack.exception.AzStackException;
import com.azstack.listener.AzStackConnectListener;
import com.azstack.listener.AzStackUserListener;
import com.azstack.listener.AzStackUserPageListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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
                Log.i("AzStack", e.toString());
            }
        });
        AzStackClient.getInstance().registerUserListener(new AzStackUserListener() {
            @Override
            public void getUserInfo(List<String> azStackUserIds, int purpose) {
                try {
                    JSONArray arrayContact = new JSONArray();
                    for (String azStackUserId : azStackUserIds) {
                        JSONObject ob = new JSONObject();
                        ob.put("azStackUserId", azStackUserId);
                        ob.put("name", "name_" + azStackUserId);
                        arrayContact.put(ob);
                    }
                    AzStackClient.getInstance().getUserInfoComplete(arrayContact, purpose);
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

        azStackClient.connect(Config.my_azstack_userid, Config.user_credentials, Config.my_name);
    }
}
