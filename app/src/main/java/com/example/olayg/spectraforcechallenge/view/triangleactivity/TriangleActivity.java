package com.example.olayg.spectraforcechallenge.view.triangleactivity;

import android.os.Bundle;

import com.example.olayg.spectraforcechallenge.util.OpenGLView;
import com.example.olayg.spectraforcechallenge.view.base.BaseActivity;

public class TriangleActivity extends BaseActivity {
    OpenGLView gLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLSurfaceView = new OpenGLView(this);
        setContentView(gLSurfaceView);
        activateToolbar(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gLSurfaceView.onResume();
    }
}
