package com.example.myapplication.patient;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.IOException;

public class SkinActivity extends AppCompatActivity {
    private Classifier mClassifier;
    private Bitmap mBitmap;
    private Button mCameraButton;
    private Button mGalleryButton;
    private Button mDetectButton;
    private ImageView mPhotoImageView;
    private TextView mResultTextView;

    private final int mCameraRequestCode = 0;
    private final int mGalleryRequestCode = 2;

    private final int mInputSize = 224;
    private final String mModelPath = "model.tflite";
    private final String mLabelPath = "labels.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_skin);
        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);

        mCameraButton = findViewById(R.id.mCameraButton);
        mGalleryButton = findViewById(R.id.mGalleryButton);
        mDetectButton = findViewById(R.id.mDetectButton);
        mPhotoImageView = findViewById(R.id.mPhotoImageView);
        mResultTextView = findViewById(R.id.mResultTextView);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(callCameraIntent, mCameraRequestCode);
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGalleryIntent = new Intent(Intent.ACTION_PICK);
                callGalleryIntent.setType("image/*");
                startActivityForResult(callGalleryIntent, mGalleryRequestCode);
            }
        });

        mDetectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classifier.Recognition results = mClassifier.recognizeImage(mBitmap).get(0);
                mResultTextView.setText(results.title + "\n Confidence: " + results.confidence);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = (Bitmap) data.getExtras().get("data");
                mBitmap = scaleImage(mBitmap);
                Toast toast = Toast.makeText(this, "Image crop to: w= " + mBitmap.getWidth() + " h= " + mBitmap.getHeight(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 20);
                toast.show();
                mPhotoImageView.setImageBitmap(mBitmap);
                mResultTextView.setText("Your photo image set now.");
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == mGalleryRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mBitmap = scaleImage(mBitmap);
                mPhotoImageView.setImageBitmap(mBitmap);
            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap scaleImage(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = (float) mInputSize / originalWidth;
        float scaleHeight = (float) mInputSize / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }
}
