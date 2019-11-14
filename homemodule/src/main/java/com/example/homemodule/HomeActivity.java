package com.example.homemodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.arouter.ARouter;
import com.example.arouter.IRoute;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void goLogin(View view){
        ARouter.getInstance().init(this.getApplication());

        ARouter.getInstance().jupmActivity("loginmodule.LoginActivity",null);
    }

}
