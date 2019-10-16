package stu.fp.lock;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    CameraBridgeViewBase mOpenCvCameraView;
    private Button mExecButton;
    private TextView mWarmTextView;
    private LoaderCallbackInterface mLoaderCallback;
    boolean needRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.request();
        }
        init();
    }

    private void init() {
        // 绑定View
        mOpenCvCameraView = (CameraBridgeViewBase) this.findViewById(R.id.CameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        // 注册Camera连接状态事件监听器
        mOpenCvCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
            }

            @Override
            public void onCameraViewStopped() {
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                return inputFrame.rgba();
            }
        });

        // 获取执行按钮和添加监听器
        mWarmTextView = (TextView) this.findViewById(R.id.warm_textview);
        mExecButton = (Button) this.findViewById(R.id.exec_button);
        mExecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exec();
            }
        });

        // 加载器回调函数
        mLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    // OpenCV引擎初始化加载成功
                    case LoaderCallbackInterface.SUCCESS:
                        Log.i(TAG, "OpenCV loaded successfully.");
                        // 连接到Camera
                        mOpenCvCameraView.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        //从数据库获取是否注册了信息
        needRegister = true;

        if (needRegister)
        {
            //如果首次使用
            //设置提醒文本框
            mWarmTextView.setText("请注册面部特征和声纹，点击'▷'按钮继续");
        }
        else
        {
            //如果已经注册了面部特征和声纹
            mWarmTextView.setText("正视摄像头并点击'▷'按钮获取待读的验证信息");
        }
    }

    private void exec() {
        // TODO:执行人脸识别和语音识别
        if (needRegister)
        {
            //注册
            this.register();
        }
        else
        {
            //如果已经注册了面部特征和声纹就执行识别
            Toast.makeText(this, "EXEC", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        mWarmTextView.setText("正视摄像头并点击'▷'按钮获取面部特征");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // OpenCVLoader.initDebug()静态加载OpenCV库
        // OpenCVLoader.initAsync()为动态加载OpenCV库，即需要安装OpenCV Manager
        if (!OpenCVLoader.initDebug()) {
            Log.w(TAG, "static loading library fail,Using Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.w(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 断开与Camera的连接
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 断开与Camera的连接
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void request() {
        //获取相机拍摄读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //版本判断
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                        }, 1);
            }
        }
    }

}