package com.azstack.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.azstack.AzStackClient;
import com.azstack.activity.ConversationActivity;
import com.azstack.sample.R;
import com.azstack.sample.adapter.UserAdapter;
import com.azstack.sample.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatWithActivity extends Activity {

    private ListView lvUsers;
    private UserAdapter adapter;
    private List<User> lstUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatwith);

        lvUsers = (ListView) findViewById(R.id.lv_users);
        lstUsers.add(new User("azstack_test1", "User 1"));
        lstUsers.add(new User("azstack_test2", "User 2"));
        lstUsers.add(new User("azstack_test3", "User 3"));
        adapter = new UserAdapter(this, lstUsers);
        lvUsers.setAdapter(adapter);

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = lstUsers.get(i);
                String userId = user.getUserId();
                String name = user.getName();

                AzStackClient.getInstance().startChat(userId, name, "");
            }
        });

        Button btnConversations = (Button) findViewById(R.id.btn_conversations);
        btnConversations.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatWithActivity.this, ConversationActivity.class));
            }
        });
    }

}
