package com.azstack.sample.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.azstack.AzStackClient;
import com.azstack.exception.AzStackException;
import com.azstack.listener.AzStackAuthenticateListener;
import com.azstack.listener.AzStackConnectListener;
import com.azstack.listener.AzStackUserListener;
import com.azstack.sample.R;
import com.azstack.sample.activity.ChatWithActivity;
import com.azstack.sample.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Utils {

    private static final Pattern USERNAME_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\._\\-]{5,}");

    public static boolean isValidUserName(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void connectAZStack(final AzStackClient azStackClient,
                                      final Context context, final String appUserId,
                                      final Handler handler, final ProgressDialog dialog) {
        context.getSharedPreferences(Constants.PREF_AZSTACK_SAMPlE,
                Context.MODE_PRIVATE).edit()
                .putString(Constants.PREF_APP_USERID, appUserId).commit();
        azStackClient.registerConnectionListenter(new AzStackConnectListener() {

            @Override
            public void onConnectionDisconnected(AzStackClient client) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, context.getString(R.string.disconnect_success), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }

            @Override
            public void onConnectionError(AzStackClient client,
                                          AzStackException e) {
                System.out.println(context.getString(R.string.connection_error));
            }

            @Override
            public void onConnectionConnected(AzStackClient client) {
                if (!AzStackClient.getInstance().isAuthenticated()) {
                    AzStackClient.getInstance().authenticate();
                }
            }
        });
        azStackClient
                .registerAuthenticateListenter(new AzStackAuthenticateListener() {

                    @Override
                    public void onDeauthenticated(AzStackClient client) {

                    }

                    @Override
                    public void onAuthenticationError(AzStackClient client,
                                                      AzStackException e) {
                    }

                    @Override
                    public void onAuthenticationChallenge(AzStackClient client,
                                                          String nonce) {
                        // This AzStack Authentication service is only for
                        // testing purpose
                        // When going into production, you need to create your
                        // own web service to execute authentication
                        String url = "http://azstack.com/test/gen_token_test.php?azStackUserID=" + appUserId + "&nonce=" + nonce;
                        String data = HttpHelper.getData(url);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int r = jsonObject.getInt("r");
                            if (r == 1) {
                                String token = jsonObject.getString("token");
                                azStackClient
                                        .answerAuthenticationChallenge(token);
                            } else {
                                System.out.println(context
                                        .getString(R.string.get_app_token_fail));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(context
                                    .getString(R.string.get_app_token_fail));
                        }
                    }

                    @Override
                    public void onAuthenticated(AzStackClient client,
                                                String arg1) {
                        System.out.println(context
                                .getString(R.string.authen_success));
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                dialog.dismiss();
                                context.startActivity(new Intent(context,
                                        ChatWithActivity.class));
                                ((MainActivity) context).finish();
                            }
                        });
                    }
                });

        azStackClient.registerUserListener(new AzStackUserListener() {

            @Override
            public void getUserInfo(String azStackUserId, int purpose) {
                // This code implemention is only for testing purpose
                // When going into production, you have to implement your own
                // web service to get your app's user info
                JSONObject obContact = Utils.getUserInfo(azStackUserId);
                azStackClient.getUserInfoComplete(obContact, purpose);
            }

            @Override
            public void viewUserInfo(String appUserId) {

            }

            @Override
            public JSONArray getListFriend() {
                JSONArray jsonArray = new JSONArray();
                for (int i = 1; i < 10; i++) {
                    try {
                        JSONObject user = new JSONObject();
                        user.put("azStackUserId", "azstack_test" + i);
                        user.put("name", "User " + i);
                        user.put("avatar", "");
                        jsonArray.put(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return jsonArray;
            }
        });

        azStackClient.connect();
    }

    public static void connectAZStackForGCM(final AzStackClient azStackClient,
                                            final Context context, final String appUserId) {
        azStackClient.registerConnectionListenter(new AzStackConnectListener() {

            @Override
            public void onConnectionDisconnected(AzStackClient client) {
            }

            @Override
            public void onConnectionError(AzStackClient client,
                                          AzStackException e) {
                System.out.println(context.getString(R.string.connection_error));
            }

            @Override
            public void onConnectionConnected(AzStackClient client) {
                if (!AzStackClient.getInstance().isAuthenticated()) {
                    AzStackClient.getInstance().authenticate();
                }
            }
        });
        azStackClient
                .registerAuthenticateListenter(new AzStackAuthenticateListener() {

                    @Override
                    public void onDeauthenticated(AzStackClient client) {

                    }

                    @Override
                    public void onAuthenticationError(AzStackClient client,
                                                      AzStackException e) {
                    }

                    @Override
                    public void onAuthenticationChallenge(AzStackClient client,
                                                          String nonce) {
                        // This AzStack Authentication service is only for
                        // testing purpose
                        // When going into production, you need to create your
                        // own web service to execute authentication
                        String url = "http://azstack.com/test/gen_token_test.php?azStackUserID=" + appUserId + "&nonce=" + nonce;
                        String data = HttpHelper.getData(url);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int r = jsonObject.getInt("r");
                            if (r == 1) {
                                String token = jsonObject.getString("token");
                                azStackClient
                                        .answerAuthenticationChallenge(token);
                            } else {
                                System.out.println(context
                                        .getString(R.string.get_app_token_fail));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(context
                                    .getString(R.string.get_app_token_fail));
                        }
                    }

                    @Override
                    public void onAuthenticated(AzStackClient client,
                                                String arg1) {
                        System.out.println(context
                                .getString(R.string.authen_success));
                    }
                });

        azStackClient.registerUserListener(new AzStackUserListener() {

            @Override
            public void getUserInfo(String azStackUserId, int purpose) {
                // This code implemention is only for testing purpose
                // When going into production, you have to implement your own
                // web service to get your app's user info
                JSONObject obContact = Utils.getUserInfo(azStackUserId);
                azStackClient.getUserInfoComplete(obContact, purpose);
            }

            @Override
            public void viewUserInfo(String azStackUserId) {

            }

            @Override
            public JSONArray getListFriend() {
                JSONArray jsonArray = new JSONArray();
                for (int i = 1; i < 10; i++) {
                    try {
                        JSONObject user = new JSONObject();
                        user.put("azStackUserId", "azstack_test" + i);
                        user.put("name", "User " + i);
                        user.put("avatar", "");
                        jsonArray.put(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return jsonArray;
            }
        });

        azStackClient.connect();
    }

    private static JSONObject getUserInfo(String azStackUserId) {
        JSONObject obContact = new JSONObject();
        try {
            obContact.put("azStackUserId", azStackUserId);
            obContact.put("name", azStackUserId);
            obContact.put("avatar", "");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obContact;
    }
}
