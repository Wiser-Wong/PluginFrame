package com.wiser.repluginframe;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.List;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionAndInstallAppPluginApks();

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.tv)).setText(getPluginAndVersion());
            }
        }, 1000);
    }

    /**
     * 检测权限并加载所有插件apk
     */
    private void checkPermissionAndInstallAppPluginApks() {
        int permissionCheck1 = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
            }
        } else {
            installAllPluginApk();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 124) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                installAllPluginApk();
            }
        }
    }

    // 加载插件
    private void installAllPluginApk() {
        RePluginManage.getInstance().installAllPluginApk(RePluginManage.getInstance().new InstallListener() {
        });
    }

    /**
     * 获取插件信息数据
     *
     * @return
     */
    private String getPluginAndVersion() {
        List<PluginInfo> list = RePlugin.getPluginInfoList();

        StringBuilder sb = new StringBuilder();
        for (PluginInfo info : list) {
            sb.append(info.getName()).append(":").append(info.getVersion()).append(";");
        }

        return sb.toString();
    }

    /**
     * 跳转Home插件方法
     *
     * @param view
     */
    public void skipHomeClick(View view) {
        Intent intent = RePlugin.createIntent("home", "com.wiser.app.home.HomeActivity");
        RePlugin.startActivity(MainActivity.this, intent);
    }

    /**
     * 跳转Main插件方法
     *
     * @param view
     */
    public void skipMainClick(View view) {
        Intent intent = RePlugin.createIntent("main", "com.wiser.app.main.MActivity");
        RePlugin.startActivity(MainActivity.this, intent);
    }
}
