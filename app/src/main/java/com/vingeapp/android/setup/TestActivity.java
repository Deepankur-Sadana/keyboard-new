//package com.vingeapp.android.setup;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.widget.TextView;
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.vingeapp.android.R;
//
///**
// * Created by deepankursadana on 12/03/17.
// */
//
//public class TestActivity extends Activity {
//    private TextView info;
//    private LoginButton loginButton;
//    private CallbackManager callbackManager;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        setContentView(R.layout.activity_facebook);
//        info = (TextView)findViewById(R.id.info);
//        loginButton = (LoginButton)findViewById(R.id.login_button);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
//                );
//            }
//
//            @Override
//            public void onCancel() {
//
//                info.setText("Login attempt canceled.");
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                info.setText("Login attempt failed.");
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//}
