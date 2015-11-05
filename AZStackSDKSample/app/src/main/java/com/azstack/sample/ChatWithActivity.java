package com.azstack.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.azstack.AzStackClient;
import com.azstack.activity.ConversationActivity;

public class ChatWithActivity extends Activity implements OnClickListener {

	private TextView tvError;
	private EditText txtAppUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatwith);

		txtAppUserId = (EditText) findViewById(R.id.txt_app_user_id);
		tvError = (TextView) findViewById(R.id.tv_error);

		Button btnSendMessage = (Button) findViewById(R.id.btn_chat);
		btnSendMessage.setOnClickListener(this);

		Button btnCall = (Button) findViewById(R.id.btn_call);
		btnCall.setOnClickListener(this);

		Button btnConversations = (Button) findViewById(R.id.btn_conversations);
		btnConversations.setOnClickListener(this);

		Button btnDisconnect = (Button) findViewById(R.id.btn_disconnect);
		btnDisconnect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chat:
			Utils.hideKeyboard(this);
			String appUserId = txtAppUserId.getText().toString().trim()
					.toLowerCase();
			if (Utils.isValidUserName(appUserId)) {
				AzStackClient.getInstance().startChat(appUserId, appUserId,
						"");
			} else {
				tvError.setText(R.string.app_user_id_invalid);
			}
			break;

		case R.id.btn_conversations:
			startActivity(new Intent(this, ConversationActivity.class));
			break;

		case R.id.btn_call:
			Utils.hideKeyboard(this);
			String appUserId1 = txtAppUserId.getText().toString().trim()
					.toLowerCase();
			if (Utils.isValidUserName(appUserId1)) {
				AzStackClient.getInstance().startCall(appUserId1, appUserId1,
						"");
			} else {
				tvError.setText(R.string.app_user_id_invalid);
			}
			break;
			case R.id.btn_disconnect:
				AzStackClient.getInstance().disconnect();
				break;
		default:
			break;
		}
	}
}
