package com.example.loginmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.annotation.Path;
import com.example.arouter.ARouter;

@Path("loginmodule.LoginActivity")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ARouter.getInstance().init(this.getApplication());
    }
}
