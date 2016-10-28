package com.azstack.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.azstack.AzOptions;
import com.azstack.AzStackClient;
import com.azstack.exception.AzStackException;
import com.azstack.listener.AzStackConnectListener;
import com.azstack.listener.AzStackUserListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by tantn on 12/25/2015.
 */
public class SampleActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spFriend;
    private Button btnChat11, btnCall, btnChatHistory, btnChatGroup, btnVideoCall;
    private View vConnect;
    private TextView tvConnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        //init view
        spFriend = (Spinner) findViewById(R.id.sp_user);
        btnChat11 = (Button) findViewById(R.id.btn_chat);
        btnCall = (Button) findViewById(R.id.btn_call);
        btnChatHistory = (Button) findViewById(R.id.btn_chat_history);
        btnChatGroup = (Button) findViewById(R.id.btn_create_group);
        btnVideoCall = (Button) findViewById(R.id.btn_video_call);

        vConnect = findViewById(R.id.v_connect);
        tvConnect = (TextView) findViewById(R.id.tv_no_connection);
        vConnect.setVisibility(View.GONE);

        btnChat11.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnChatHistory.setOnClickListener(this);
        btnChatGroup.setOnClickListener(this);
        btnVideoCall.setOnClickListener(this);

        //init azstack
        initAzStack();

        //init listener
        initAzstackListener();

        //connect to azstackserver
        //if you change my_azstack_userid, you must disconnect from AZStack server and clear all cached data on client
        //AzStackClient.getInstance().logout();
        vConnect.setVisibility(View.VISIBLE);
        tvConnect.setText(R.string.connecting);
        AzStackClient.getInstance().connect(Config.my_azstack_userid, Config.user_credentials, Config.my_name);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_chat) {
            int pos = spFriend.getSelectedItemPosition();
            AzStackClient.getInstance().startChat(SampleActivity.this, Config.listFriendAzStackUserId[pos], Config.listFriendName[pos], null);
        } else if (id == R.id.btn_call) {
            int pos = spFriend.getSelectedItemPosition();
            AzStackClient.getInstance().startCall(SampleActivity.this, Config.listFriendAzStackUserId[pos], Config.listFriendName[pos], null);
        } else if (id == R.id.btn_chat_history) {
            AzStackClient.getInstance().viewChatHistory(SampleActivity.this);
        } else if (id == R.id.btn_create_group) {
            AzStackClient.getInstance().createGroup(SampleActivity.this);
        } else if (id == R.id.btn_video_call) {
            int pos = spFriend.getSelectedItemPosition();
            AzStackClient.getInstance().startVideoCall(SampleActivity.this, Config.listFriendAzStackUserId[pos], Config.listFriendName[pos], null);
        }
    }

    private void initAzStack() {
        AzOptions azOptions = new AzOptions();
        azOptions.setGoogleCloudMessagingId(Config.SENDER_ID);
        AzStackClient.newInstance(getBaseContext(), Config.app_id, Config.public_key, azOptions);
    }

    private void initAzstackListener() {
        //register connect to azstackserver
        AzStackClient.getInstance().registerConnectionListenter(new AzStackConnectListener() {
            @Override
            public void onConnectionConnected(AzStackClient client) {
                System.out.println("================ connected");
                vConnect.post(new Runnable() {
                    @Override
                    public void run() {
                        tvConnect.setText(R.string.connected);
                        vConnect.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                vConnect.setVisibility(View.GONE);
                                spFriend.setEnabled(true);
                                btnChat11.setEnabled(true);
                                btnCall.setEnabled(true);
                                btnChatHistory.setEnabled(true);
                                btnChatGroup.setEnabled(true);
                                btnVideoCall.setEnabled(true);
                            }
                        }, 800);
                    }
                });
            }

            @Override
            public void onConnectionDisconnected(AzStackClient client) {
            }

            @Override
            public void onConnectionError(AzStackClient client, AzStackException e) {
                System.out.println("================ error: " + e.toString());
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
    }
}
