package com.iflytek.wordlock.app;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.wordlock.R;
import com.iflytek.wordlock.entity.User;

/**
 * 应用入口
 *
 * */
public class DemoApp extends Application {
	public static String mAuth_id;
	private static User mUser;
	public static final String HIS_FILE_NAME ="HistoryFile";
	


	@Override
	public void onCreate() {
		// 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用“,”分隔。
		// 设置你申请的应用appid
		StringBuffer param = new StringBuffer();
		param.append("appid="+getString(R.string.app_id));
		SpeechUtility.createUtility(getApplicationContext(), param.toString());

		//Android 7.0 出现file:// 无法解析问题：FileUriExposedException
		if (Build.VERSION.SDK_INT >= 26) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
		}

		super.onCreate();
	}
	
	
	
	public static User getHostUser() {
		if (null == mUser) {
			mUser = new User();
		}
		return mUser;
	}
	public static void setHostUser(User user) {
		mUser = user;
	}

}
