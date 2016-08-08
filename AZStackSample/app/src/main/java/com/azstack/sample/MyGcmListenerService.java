/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.azstack.sample;

import android.os.Bundle;
import android.view.View;

import com.azstack.AzStackClient;
import com.azstack.exception.AzStackException;
import com.azstack.listener.AzStackConnectListener;
import com.azstack.listener.AzStackUserListener;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * IntentService responsible for handling GCM messages.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // Implement connect AZStack service here

        AzStackClient azStackClient = AzStackClient.newInstance(this,
                Config.app_id, Config.public_key);

        AzStackClient.getInstance().registerConnectionListenter(new AzStackConnectListener() {
            @Override
            public void onConnectionConnected(AzStackClient client) {
            }

            @Override
            public void onConnectionDisconnected(AzStackClient client) {
            }

            @Override
            public void onConnectionError(AzStackClient client, AzStackException e) {
            }
        });

        //register user
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
                JSONArray arrayContact = new JSONArray();
                try {
                    for (int i = 0; i < Config.listFriendAzStackUserId.length; i++) {
                        JSONObject obContact = new JSONObject();
                        obContact.put("azStackUserId", Config.listFriendAzStackUserId[i]);
                        obContact.put("name", Config.listFriendName[i]);
                        arrayContact.put(obContact);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return arrayContact;
            }
        });

        AzStackClient.getInstance().showNotification(this, data);
    }
}
