package com.example.lixiang.mydiary.callback;

import com.avos.avoscloud.AVException;
import com.example.lixiang.mydiary.model.User;

public interface OnLogInCallback {
    void done(User user, AVException e);
}