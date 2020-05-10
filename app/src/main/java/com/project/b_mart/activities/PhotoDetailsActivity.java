package com.project.b_mart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.b_mart.R;

public class PhotoDetailsActivity extends AppCompatActivity {
    private static Bitmap bm;

    private ImageView img;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        if (bm == null) {
            Toast.makeText(this, "Nothing to show!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        img = findViewById(R.id.img);
        img.setImageBitmap(bm);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        bm = null;
    }

    public static void setBm(Bitmap bm) {
        PhotoDetailsActivity.bm = bm;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.f));
            img.setScaleX(mScaleFactor);
            img.setScaleY(mScaleFactor);

            return true;
        }
    }
}
