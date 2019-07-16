package cn.adminzero.passwordshield_demo0;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Thread.sleep;

public class WelcomeSplashActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeSplash_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Boolean isFirstLogin = true;
        Log.d(TAG, "onCreate: 进入了onCreate");
        setContentView(R.layout.activity_welcome_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
     /*   //隐藏导航栏
        //setNavigationBar(this,100 );
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// system_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        */
        //延时两秒
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onCreate: 设置了view");
        //这里用了三层结构，来展示三个gif  每个gif右上角有个透明的按钮 onClick后进入下一层
        if (MyApplication.isFirstLogin) {
            ImageView imageView = (ImageView) findViewById(R.id.splash_launcher);
            // Glide.with(this).load(R.drawable.launcher1).into(imageView);

            GifLoadOneTimeGif.loadOneTimeGif(this, R.drawable.launcher1, imageView,1, new GifLoadOneTimeGif.GifListener() {
                @Override
                public void gifPlayComplete() {
                    Button button=(Button)findViewById(R.id.next_gif);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageView imageView = (ImageView) findViewById(R.id.splash_launcher);
                            // Glide.with(this).load(R.drawable.launcher1).into(imageView);

                            GifLoadOneTimeGif.loadOneTimeGif(WelcomeSplashActivity.this, R.drawable.launcher2, imageView,1, new GifLoadOneTimeGif.GifListener() {
                                @Override
                                public void gifPlayComplete() {
                                    Button button=(Button)findViewById(R.id.next_gif);

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ImageView imageView = (ImageView) findViewById(R.id.splash_launcher);
                                            // Glide.with(this).load(R.drawable.launcher1).into(imageView);

                                            GifLoadOneTimeGif.loadOneTimeGif(WelcomeSplashActivity.this, R.drawable.launcher3, imageView,1, new GifLoadOneTimeGif.GifListener() {
                                                @Override
                                                public void gifPlayComplete() {
                                                    Button button=(Button)findViewById(R.id.next_gif);

                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent intent=new Intent(WelcomeSplashActivity.this,CreateUserActivity.class);
                                                            startActivity(intent);
                                                            finish();

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }
                    });

                }
            });
            // Glide.with(this).load(R.drawable.launcher2).into(imageView);
            //  Glide.with(this).load(R.drawable.launcher3).into(imageView);

        } else {
            Intent intent = new Intent(WelcomeSplashActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static class GifLoadOneTimeGif {

        /**
         * Gif 加载 可以设置次数，监听播放完成回调
         * @param context  上下文对象
         * @param model 传入的gif地址，可以是网络，也可以是本地，（https://raw.githubusercontent.com/Jay-YaoJie/KotlinDialogs/master/diagram/test.gif）
         * @param imageView 要显示的imageView
         * @param loopCount 播放次数
         * @param gifListener  Gif播放完毕回调
         */
        public static void loadOneTimeGif(Context context, Object model, final ImageView imageView ,final int loopCount, final GifListener gifListener) {
            Glide.with(context).asGif().load(model).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    try {
                        Field gifStateField = GifDrawable.class.getDeclaredField("state");
                        gifStateField.setAccessible(true);
                        Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                        Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                        gifFrameLoaderField.setAccessible(true);
                        Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                        Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                        gifDecoderField.setAccessible(true);
                        Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                        Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                        Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                        getDelayMethod.setAccessible(true);
                        ////设置播放次数
                        resource.setLoopCount(loopCount);
                        //获得总帧数
                        int count = resource.getFrameCount();
                        int delay = 0;
                        for (int i = 0; i < count; i++) {
                            //计算每一帧所需要的时间进行累加
                            delay += (int) getDelayMethod.invoke(gifDecoder, i);
                        }
                        imageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (gifListener != null) {
                                    gifListener.gifPlayComplete();
                                }
                            }
                        }, delay);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }).into(imageView);
        }

        /**
         * Gif播放完毕回调
         */
        public interface GifListener {
            void gifPlayComplete();
        }
    }

}
