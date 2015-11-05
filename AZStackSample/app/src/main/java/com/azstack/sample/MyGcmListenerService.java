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

import android.content.Context;
import android.os.Bundle;

import com.azstack.AzStackClient;
import com.azstack.sample.common.Constants;
import com.azstack.sample.common.Utils;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * IntentService responsible for handling GCM messages.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // Implement connect AZStack service here
        AzStackClient azStackClient = AzStackClient.getInstance();
        if (azStackClient == null) {
            azStackClient = AzStackClient.newInstance(this,
                    Constants.appId);
        }
        String appUserId = getSharedPreferences(Constants.PREF_AZSTACK_SAMPlE, Context.MODE_PRIVATE).getString(Constants.PREF_APP_USERID, "");
        Utils.connectAZStackForGCM(azStackClient, this, appUserId);
    }
}
