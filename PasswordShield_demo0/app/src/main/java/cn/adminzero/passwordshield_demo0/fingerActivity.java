package cn.adminzero.passwordshield_demo0;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.adminzero.passwordshield_demo0.biometriclib.BiometricPromptManager;

public class fingerActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private BiometricPromptManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.button);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        mManager = BiometricPromptManager.from(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SDK version is " + Build.VERSION.SDK_INT);
        stringBuilder.append("\n");
        stringBuilder.append("isHardwareDetected : " + mManager.isHardwareDetected());
        stringBuilder.append("\n");
        stringBuilder.append("hasEnrolledFingerprints : " + mManager.hasEnrolledFingerprints());
        stringBuilder.append("\n");
        stringBuilder.append("isKeyguardSecure : " + mManager.isKeyguardSecure());
        stringBuilder.append("\n");
        mTextView.setText(stringBuilder.toString());


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mManager.isBiometricPromptEnable()) {
                    mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                        @Override
                        public void onUsePassword() {
                            Toast.makeText(fingerActivity.this, "onUsePassword", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSucceeded() {

                            Toast.makeText(fingerActivity.this, "onSucceeded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed() {

                            Toast.makeText(fingerActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(int code, String reason) {

                            Toast.makeText(fingerActivity.this, "onError", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {

                            Toast.makeText(fingerActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

        });
    }
}

