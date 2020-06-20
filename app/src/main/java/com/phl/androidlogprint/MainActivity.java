package com.phl.androidlogprint;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Throwable throwable = new Throwable();

        Log.i(TAG, "onCreate");
        Log.i(TAG, "onCreate", throwable);


        Log.d(TAG, "onCreate");
        Log.d(TAG, "onCreate", throwable);

        Log.v(TAG, "onCreate");
        Log.v(TAG, "onCreate", throwable);

        Log.e(TAG, "onCreate");
        Log.e(TAG, "onCreate", throwable);

        Log.w(TAG, "onCreate");
        Log.w(TAG, "onCreate", throwable);
        Log.w(TAG, throwable);
    }
}