package com.example.lixiang.mydiary.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.lixiang.mydiary.callback.OnLogInCallback;
import com.example.lixiang.mydiary.callback.OnSignUpCallback;

@AVClassName("User")
public class User extends AVUser {

    public static final Creator CREATOR = AVObjectCreator.instance;

    public static boolean isLogin(){
        return AVUser.getCurrentUser() != null;
    }

    public static void logInInBackground(String username, String password, final OnLogInCallback callback) {
        logInInBackground(username, password, new LogInCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                callback.done(user, e);
            }
        }, User.class);
    }

    public void signUpInBackground(final OnSignUpCallback callback) {
        signUpInBackground(new SignUpCallback() {
            @Override public void done(AVException e) {
                callback.done(e);
            }
        });
    }
}
