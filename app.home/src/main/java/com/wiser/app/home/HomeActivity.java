package com.wiser.app.home;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qihoo360.replugin.loader.a.PluginFragmentActivity;

public class HomeActivity extends PluginFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);
    }

    public void skipHostClick(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.wiser.repluginframe", "com.wiser.repluginframe.MainActivity"));
        startActivity(intent);
    }
}
