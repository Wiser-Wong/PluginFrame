package com.wiser.repluginframe;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginConfig;
import com.wiser.hostlibrary.HostApplication;

import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @author Wiser
 *
 *         宿主Application
 */
public class WApplication extends HostApplication {

//	@Override protected void attachBaseContext(Context base) {
//		super.attachBaseContext(base);
//		RePlugin.App.attachBaseContext(this);
//		RePlugin.enableDebugger(base, BuildConfig.DEBUG);
//		MultiDex.install(this);
//	}
//
//	@Override protected RePluginConfig createConfig() {
//		RePluginConfig config = new RePluginConfig();
//		// 允许“插件使用宿主类”。默认为“关闭”
//		config.setUseHostClassIfNotFound(true);
//		config.setPrintDetailLog(BuildConfig.DEBUG);
//		config.setMoveFileWhenInstalling(true);
//		// 打开签名校验 (true 发布版本 false 开发版本)
//		config.setVerifySign(false);
//		return config;
//	}
//
//	@Override public void onCreate() {
//		super.onCreate();
//		RePlugin.App.onCreate();
//		// 加入合法签名 (生产证书md5值)
//		RePlugin.addCertSignature("keystore.jks MD5合法签名");
//	}

}
