package com.iamyeong.myfriendsplace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;
import com.kakao.sdk.user.model.User;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class LoginActivity extends AppCompatActivity {

    private String knickName, imageURL, thumbnailURL, email;
    private Long userKakaoId;
    private FirebaseFirestore db;
    private UserManager userManager;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = UserManager.getInstance();
        userModel = UserModel.getInstance();

         Function2<OAuthToken, Throwable, Unit> function2 = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {

                if (oAuthToken != null) {
                    //Detected account to do
                } else {
                    //Undetected account to do
                }

                checkKakaoLogin();

                return null;
            }
        };

        ImageView loginButton = findViewById(R.id.img_login_kakao);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function2);


                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    //Meaning of return true : Already install kakao talk application.

                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, function2);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function2);
                }


            }


        });

    }

    private void checkKakaoLogin() {

        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {

                    Intent intent = new Intent(LoginActivity.this, SplashActivity.class);

                    Account kakaoAccount = user.getKakaoAccount();

                    userKakaoId = user.getId();
                    knickName = kakaoAccount.getProfile().getNickname();
                    imageURL = kakaoAccount.getProfile().getProfileImageUrl();
                    thumbnailURL = kakaoAccount.getProfile().getThumbnailImageUrl();
                    email = kakaoAccount.getEmail();

                    userModel.setUserId(userKakaoId);
                    userModel.setThumbnailURL(thumbnailURL);
                    userModel.setImageURL(imageURL);
                    userModel.setUserName(knickName);

                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(LoginActivity.this, R.string.login_fail_5, Toast.LENGTH_SHORT).show();
                }

                return null;
            }
        });


    }





}