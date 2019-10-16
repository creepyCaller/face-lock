package com.iflytek.wordlock;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.RequiresApi;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.wordlock.app.DemoApp;
import com.iflytek.wordlock.database.Dao.AppMsgDao;
import com.iflytek.wordlock.entity.User;
import com.iflytek.wordlock.mixedverify.MixedVerifyActivity;
import com.iflytek.wordlock.util.FuncUtil;

/**
 * 主Activity
 *
 */
public class MainActivity extends Activity implements OnClickListener {
    TextView txt_tip;
    TextView txt_next;
    Button btn_exec;
    private AppMsgDao db;
    private SQLiteDatabase sql_read;
    private SQLiteDatabase sql_write;

    private boolean isRegister = false;
    private Toast mToast;
    private String WELCOME = "欢迎使用加密记事本，请确保可以清楚的拍摄到面部细节、周围环境安静.";
    private String WELCOME_NEED_REGISTER = "欢迎使用加密记事本，由于是首次使用，请点击下一步按钮以注册面部模型和声纹模型来作为解锁记事本的钥匙，请确保可以清楚的拍摄到面部细节、周围环境安静.";
    private String NEXT_STEP = "下一步";
    private String TO_VERIFY = "验证";
    private String COMFERM = "确定";
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        db = new AppMsgDao(MainActivity.this);
        sql_read = db.getReadableDatabase();
        init();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestHandler();
		}
		initUI();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 设置全局的mAuth_id
		DemoApp.mAuth_id = db.getUid(sql_read);
		DemoApp.setHostUser( (User) FuncUtil.readObject(this, DemoApp.mAuth_id));
		DemoApp.getHostUser().setUsername(DemoApp.mAuth_id);
		FuncUtil.saveObject(this,DemoApp.getHostUser(), DemoApp.mAuth_id);
        // 初始化Toast
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        // 加载控件
        btn_exec = (Button) this.findViewById(R.id.btn_exec);
        txt_tip = (TextView) this.findViewById(R.id.txt_tip);
        txt_next = (TextView) this.findViewById(R.id.txt_next);
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
        btn_exec.setOnClickListener(this);
        // 验证是否注册面部和声纹
        isRegister = db.isRegister(sql_read);
        if (isRegister) {
            // IF(已注册) 跳到混合验证
            txt_tip.setText(WELCOME);
            btn_exec.setText(TO_VERIFY);
        } else {
            // ELSE 提示需要注册，进入注册向导
            sql_write = db.getWritableDatabase();
            txt_tip.setText(WELCOME_NEED_REGISTER);
            txt_next.setText("点击下一步进入面部模型注册.");
            btn_exec.setText(NEXT_STEP);
        }
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_exec:
            if (isRegister) {
                // IF(已注册) 跳到混合验证
                intent = new Intent(MainActivity.this, MixedVerifyActivity.class);
                intent.putExtra("scenes", "mix");
                startActivity(intent);
            } else {
                registerWizard();
            }
			break;
		default:
			break;
		}
	}

    private void registerWizard() {
        // 跳转至面部模型注册页面
        Intent intent = new Intent(MainActivity.this, FaceVerify.class);
        intent.putExtra("scenes", "ifr");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent retIntent) {
        switch (resultCode) {
            case -1:
                txt_tip.setText("错误：未能注册模型.");
                txt_next.setText("点击确认关闭应用.");
                btn_exec.setText(COMFERM);
                btn_exec.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                break;
            case 1:
                // 面部模型注册完毕后返回，跳转至声纹注册页面
                txt_tip.setText("面部模型注册完成！");
                txt_next.setText("点击下一步进入声纹注册，请确认周围环境安静.");
                btn_exec.setText(COMFERM);
                btn_exec.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, VocalVerify.class);
                        intent.putExtra("scenes", "ivp");
                        startActivityForResult(intent, 0);
                    }
                });
                break;
            case 2:
                // 注册完成，把数据库的Flag改为已注册，重启应用
                db.updateData(sql_write, 1);
                txt_tip.setText("注册完成，请重启应用！");
                txt_next.setText("");
                btn_exec.setText(COMFERM);
                btn_exec.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sql_read.close();
        sql_write.close();
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
	void requestHandler() {
		//获取权限
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//版本判断
			if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,new String[] {
						android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
						android.Manifest.permission.LOCATION_HARDWARE,
						android.Manifest.permission.READ_PHONE_STATE,
						android.Manifest.permission.WRITE_SETTINGS,
						android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO,
						Manifest.permission.ACCESS_COARSE_LOCATION
				}, 1);
			} else {
                txt_tip.setText("错误：未获取需要的应用权限.");
                btn_exec.setText(COMFERM);
                btn_exec.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
		}
	}

}
