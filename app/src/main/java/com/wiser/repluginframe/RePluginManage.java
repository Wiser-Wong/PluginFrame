package com.wiser.repluginframe;

import java.io.File;
import java.io.IOException;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

/**
 * @author Wiser
 *
 *         外置插件工具
 */
public class RePluginManage {

	private RePluginManage() {}

	private static final class RePluginHolder {

		private static final RePluginManage instance = new RePluginManage();
	}

	public static RePluginManage getInstance() {
		return RePluginHolder.instance;
	}

	/**
	 * 打开插件
	 *
	 * @param context
	 * @param pluginName
	 * @param activityName
	 * @param installListener
	 */
	public void openPlugin(Context context, String pluginName, String activityName, InstallListener installListener) {
		this.installListener = installListener;
		if (RePlugin.isPluginInstalled(pluginName)) {// 判断是否已经安装，安装了的话，就打开Activity，并且检查插件版本，需要更新的话就下载插件
			RePlugin.startActivity(context, RePlugin.createIntent(pluginName, activityName));
			if (installListener != null) {
				installListener.onSuccess();
			}
			PluginInfo info = RePlugin.getPluginInfo(pluginName);
			if (info.getVersion() < 2) {// 版本号由你们接口获得，然后进行对比，插件版本低于接口的版本就下载更新
				downPlugin(context, "http://插件地址", pluginName, activityName, true);
			}
		} else {
			// downPlugin(context, "http://插件地址", pluginName, activityName, false);
			// 本例子我们不进行下载直接进行安装插件
			installPlugin(context, pluginName, activityName, false);
		}
	}

	/**
	 * 安装插件
	 *
	 * @param context
	 * @param pluginName
	 * @param activityName
	 */
	public void installPlugin(final Context context, final String pluginName, final String activityName, boolean isUpdate) {
		final PluginInfo info = RePlugin.install(Environment.getExternalStorageDirectory() + "/" + pluginName + ".apk");
		if (info != null) {
			if (isUpdate) {// 判断，是否为更新，如果是更新就预加载，下次打开就是最新的插件，不是更新就开始安装
				RePlugin.preload(info);
			} else {
				new Thread(new Runnable() {

					@Override public void run() {
						RePlugin.startActivity(context, RePlugin.createIntent(info.getName(), activityName));
						((Activity) context).runOnUiThread(new Runnable() {

							@Override public void run() {
								if (installListener != null) {
									installListener.onSuccess();
								}
							}
						});
					}
				}).start();
			}
		} else {
			if (installListener != null) {
				installListener.onFail("安装失败");
			}
		}
	}

	/**
	 * 安装所有插件apk
	 *
	 * @param installListener
	 *            插件apk
	 */
	public void installAllPluginApk(InstallListener installListener) {
		File readFile = Environment.getExternalStorageDirectory();
		if (readFile.exists() && readFile.isDirectory()) {
			File[] files = readFile.listFiles();
			if (files == null || files.length == 0) {
				if (installListener != null) installListener.onFail("外置没有可用的插件");
				return;
			}
			for (final File file : files) {
				if (!file.exists() || file.isDirectory()) {
					continue;
				}
				String fileName = file.getName();
				if (fileName.endsWith(".apk") && fileName.startsWith("app.")) {
					try {
						// 加载插件Apk文件到宿主APK中
						RePlugin.install(file.getCanonicalPath());
						if (installListener != null) installListener.onSuccess();
					} catch (IOException e) {
						e.printStackTrace();
						if (installListener != null) installListener.onFail("加载外置插件异常");
					}
				}
			}
		}
	}

	/**
	 * 下载插件
	 *
	 * @param context
	 * @param fileUrl
	 * @param pluginName
	 * @param activityName
	 * @param isUpdate
	 *            是否是更新
	 */
	public void downPlugin(final Context context, String fileUrl, final String pluginName, final String activityName, final boolean isUpdate) { // 获取文件存储权限
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
		} // 下载插件，里面的下载方法可以换成你们自己的，例如okhttp，xutils3等等下载都行，然后在回调中处理那几个方法就行
			// OkGo.<File>get(fileUrl).tag(context).execute(new
			// FileCallback(Environment.getExternalStorageDirectory().getPath(), pluginName
			// + ".apk") {
			// @Override
			// public void onSuccess(Response<File> response) {
			// installPlugin(context, pluginName, activityName, isUpdate);
			// }
			//
			// @Override
			// public void downloadProgress(Progress progress) {
			// super.downloadProgress(progress);
			// if (installListener != null) {
			// installListener.onInstalling((int) (progress.fraction * 100));
			// }
			// }
			//
			// @Override
			// public void onError(Response<File> response) {
			// super.onError(response);
			// if (installListener != null) {
			// installListener.onFail("下载失败");
			// }
			// }
			// });
	}

	/**
	 * 打开插件的Activity
	 *
	 * @param context
	 * @param pluginName
	 * @param activityName
	 */
	public void openActivity(Context context, String pluginName, String activityName) {
		RePlugin.startActivity(context, RePlugin.createIntent(pluginName, activityName));
	}

	/**
	 * 打开插件的Activity 可带参数传递
	 *
	 * @param context
	 * @param intent
	 * @param pluginName
	 * @param activityName
	 */
	public void openActivity(Context context, Intent intent, String pluginName, String activityName) {
		intent.setComponent(new ComponentName(pluginName, activityName));
		RePlugin.startActivity(context, intent);
	}

	/**
	 * 打开插件的Activity 带回调
	 *
	 * @param activity
	 * @param intent
	 * @param pluginName
	 * @param activityName
	 * @param requestCode
	 */
	public void openActivityForResult(Activity activity, Intent intent, String pluginName, String activityName, int requestCode) {
		intent.setComponent(new ComponentName(pluginName, activityName));
		RePlugin.startActivityForResult(activity, intent, requestCode, null);
	}

	private InstallListener installListener;

	public abstract class InstallListener {

		void onInstalling(int progress){}

		void onFail(String msg){}

		void onSuccess(){}

	}
}