package cn.adminzero.passwordshield_demo0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.adminzero.passwordshield_demo0.FaceManger.ResultClass.*;
import cn.adminzero.passwordshield_demo0.FaceManger.Face;
import cn.adminzero.passwordshield_demo0.util.Base64Util;
import cn.adminzero.passwordshield_demo0.util.CameraSurfaceView;
import cn.adminzero.passwordshield_demo0.util.CameraUtils;
import cn.adminzero.passwordshield_demo0.util.FileUtil;
import cn.adminzero.passwordshield_demo0.util.ImageUtils;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 0x01;
    private ProgressDialog progressDialog;
    private CameraSurfaceView mCameraSurfaceView;
    private Button mBtnTake;
    private Boolean safeTakePicture = false;
    private int mOrientation;
    private addFaceResult addfaceresult;
    // CameraSurfaceView 容器包装类
    private FrameLayout mAspectLayout;
    private boolean mCameraRequested;
    private boolean faceIsRegistered;
    private AlertDialog.Builder dialog;
    private AlertDialog.Builder dialog1;
    private boolean face_check_valid;
    private getFaceListResult getfacelistresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor sharedPreferenceEditor;
        sharedPreferenceEditor = sharedPreferences.edit();
        //检查配置中是否存在该键 如果不存在
        if (!sharedPreferences.contains("face_is_registered")) {
            sharedPreferenceEditor.putBoolean("face_is_registered", false);
        }
        //检查是否注册人脸，默认false
        faceIsRegistered = sharedPreferences.getBoolean("face_is_registered", false);

        face_check_valid = false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog = new AlertDialog.Builder(CameraActivity.this);
        dialog1 = new AlertDialog.Builder(CameraActivity.this);
        dialog.setTitle("欢迎使用");
        dialog1.setCancelable(false);
        dialog.setCancelable(false);
        dialog1.setTitle("成功");
        dialog.setMessage("点击确认开始注册");
        dialog1.setMessage("注册成功!");
        dialog1.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //修改sharedPreferences
                sharedPreferenceEditor.putBoolean("face_is_registered", true);
                sharedPreferenceEditor.commit();

                Intent mainIntent = new Intent(CameraActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentAuthentication = new Intent(CameraActivity.this, AuthenticationActivity.class);
                startActivity(intentAuthentication);
                finish();
            }
        });

        progressDialog = new ProgressDialog(CameraActivity.this);
        progressDialog.setTitle("请耐心等待");
        progressDialog.setMessage("正在核实您的身份...");
        progressDialog.setCancelable(false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        // Android 6.0相机动态权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initView();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_CAMERA);
        }
        if (!faceIsRegistered)
            dialog.show();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mAspectLayout = (FrameLayout) findViewById(R.id.layout_aspect);
        ;
        mCameraSurfaceView = new CameraSurfaceView(this);
        mAspectLayout.addView(mCameraSurfaceView);
        mOrientation = CameraUtils.calculateCameraPreviewOrientation(CameraActivity.this);
        mBtnTake = (Button) findViewById(R.id.btn_take);
        mBtnTake.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // 相机权限
            case REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCameraRequested = true;
                    initView();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraRequested) {
            CameraUtils.startPreview();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraUtils.stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take:
                d(String.valueOf(safeTakePicture));
                if (!safeTakePicture) {
                    try {
                        takePicture();
                        if (!faceIsRegistered) {
                            //   progressDialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //处理耗时逻辑
                                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                                            + "temp" + ".jpg";
                                    String encode = null;
                                    try {
                                        encode = Base64Util.encode(FileUtil.readFileByBytes(path));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String Result = Face.addFace(encode, "965954485", "groupTest");
                                    Gson gson = new Gson();
                                    addfaceresult = gson.fromJson(Result, addFaceResult.class);
                                    progressDialog.dismiss();
                                    Looper.prepare();
                                    switch (addfaceresult.getError_code()) {
                                        case 0: {
                                            //   t("人脸添加成功");
                                            progressDialog.dismiss();
                                            face_check_valid = true;
                                            dialog1.setMessage("注册成功!");
                                            dialog1.show();
                                            break;
                                        }
                                        case 222210: {
                                            progressDialog.dismiss();
                                            dialog1.setMessage("检测到您在人脸库已有资料，可直接使用人脸识别功能!");
                                            dialog1.show();
                                        }
                                        case 222202: {
                                            t("没有在您的照片中检测到人脸，请重试!");
                                            break;
                                        }
                                        case 222205:
                                        case 222206: {
                                            t("服务端请求失败!");
                                            break;
                                        }
                                        case 223113: {
                                            t("对不起，检测到您的脸部被遮挡，请重试!");
                                            break;
                                        }
                                        case 223114: {
                                            t("对不起，检测到您的脸部模糊，请重试!");
                                            break;
                                        }
                                        case 223115: {
                                            t("对不起，您所处的环境光线不佳，请重试!");
                                            break;
                                        }
                                        case 223116: {
                                            t("对不起，检测到您的人脸不完整，请重试!");
                                            break;
                                        }
                                        case 223120: {
                                            t("对不起，活体检测未通过，请勿使用照片!");
                                            break;
                                        }
                                        default: {
                                            t(addfaceresult.getError_Message());
                                            break;
                                        }
                                    }
                                    Looper.loop();
                                }
                            }).start();
                            progressDialog.setTitle("正在检查照片质量");
                            progressDialog.setMessage("请耐心等待...");
                            progressDialog.show();
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String result = Face.getFaceList("abcd", "groupTest");
                                    Gson gson = new Gson();
                                    getfacelistresult = gson.fromJson(result, getFaceListResult.class);
                                    matchResult matchresult = gson.fromJson(Face.match(getfacelistresult.getResult().getFace_list().get(0).getFace_token()), matchResult.class);

                                    progressDialog.dismiss();
                                    Looper.prepare();
                                    switch (matchresult.getError_code()) {
                                        case 0: {
                                            if (matchresult.getResult().getScore() >= 90) {
                                                t("核查通过!");
                                                progressDialog.dismiss();
                                                Intent mainIntent = new Intent(CameraActivity.this, MainActivity.class);
                                                startActivity(mainIntent);
                                                finish();
                                            }
                                            else {
                                                t("身份校验失败，请重试!");
                                            }
                                            break;
                                        }
                                        case 222202: {
                                            t("没有在您的照片中检测到人脸，请重试!");
                                            break;
                                        }
                                        case 222207: {
                                            t("身份验证失败，请重试!");
                                            break;
                                        }
                                        case 222205:
                                        case 222206: {
                                            t("服务端请求失败!");
                                            break;
                                        }
                                        case 223113: {
                                            t("对不起，检测到您的脸部被遮挡，请重试!");
                                            break;
                                        }
                                        case 223114: {
                                            t("对不起，检测到您的脸部模糊，请重试!");
                                            break;
                                        }
                                        case 223115: {
                                            t("对不起，您所处的环境光线不佳，请重试!");
                                            break;
                                        }
                                        case 223116: {
                                            t("对不起，检测到您的人脸不完整，请重试!");
                                            break;
                                        }
                                        case 223120: {
                                            t("对不起，活体检测未通过，请勿使用照片!");
                                            break;
                                        }
                                        default: {
                                            t(matchresult.getError_msg());
                                            break;
                                        }
                                    }
                                    Looper.loop();
                                }
                            }).start();
                            progressDialog.setTitle("请耐心等待");
                            progressDialog.setMessage("正在核实您的身份...");
                            progressDialog.show();
                        }
                    } catch (RuntimeException e) {
                    }
                }
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        CameraUtils.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                safeTakePicture = true;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    CameraUtils.startPreview();
                    bitmap = ImageUtils.getRotatedBitmap(bitmap, mOrientation);
                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                            + "temp" + ".jpg";
                    try {
                        FileOutputStream fout = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        fout.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                safeTakePicture = false;
                CameraUtils.startPreview();

            }
        });
    }


    /**
     * 切换相机
     */
//    private void switchCamera() {
//        if (mCameraSurfaceView != null) {
//            CameraUtils.switchCamera(1 - CameraUtils.getCameraID(), mCameraSurfaceView.getHolder());
//            // 切换相机后需要重新计算旋转角度
//            mOrientation = CameraUtils.calculateCameraPreviewOrientation(CameraActivity.this);
//        }
//    }

}