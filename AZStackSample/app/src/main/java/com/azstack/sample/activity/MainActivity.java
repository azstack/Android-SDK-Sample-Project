package com.azstack.sample.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.azstack.AzOptions;
import com.azstack.AzStackClient;
import com.azstack.sample.common.Constants;
import com.azstack.sample.R;
import com.azstack.sample.common.Utils;

public class MainActivity extends Activity implements OnClickListener {
    private ProgressDialog dialog;
    private EditText txtAzStackUserId;
    private AzStackClient azStackClient;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtAzStackUserId = (EditText) findViewById(R.id.txt_azstack_user_id);
        Button btnContinue = (Button) findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
    }

    private void connect(String azStackUserId) {
        dialog = ProgressDialog.show(this, "", getString(R.string.connecting));
        dialog.setCancelable(false);
        dialog.show();
        if (azStackClient == null) {
            AzOptions azOptions = new AzOptions();
            azOptions.setGoogleCloudMessagingId(Constants.senderId);
            azStackClient = AzStackClient.newInstance(this, Constants.appId,
                    azOptions);
        }

        Utils.connectAZStack(azStackClient, this, azStackUserId, handler, dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                Utils.hideKeyboard(this);
                String azStackUserId = txtAzStackUserId.getText().toString().trim()
                        .toLowerCase();
                if (Utils.isValidUserName(azStackUserId)) {
                    connect(azStackUserId);
                } else {
                    Toast toast = Toast.makeText(MainActivity.this,
                            getString(R.string.app_user_id_invalid),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            default:
                break;
        }
    }
}