package com.example.plantparenthood;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
public class LightLevelScanner extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    protected void onCreate(Bundle saveInstancedState){
        super.onCreate(saveInstancedState);
        setContentView(R.layout.light_level_scanner);

        requestCameraPermission();
        // Open the camera
        Camera camera = Camera.open();
        //List<Camera.Size> allSizes = camera.getParameters().getSupportedPreviewSizes();
        //for(int i = 0; i < allSizes.size(); i++)
        //{
        //    System.out.println(allSizes.get(i).width + " " +allSizes.get(i).height);
        //}

        // Set the camera parameters
        Camera.Parameters params = camera.getParameters();
        //params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        //params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //camera.setParameters(params);

        // Create a SurfaceView for the camera preview
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

                // You need to choose the most appropriate previewSize for your app
                Camera.Size previewSize = camera.new Size(1280,720);
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                camera.setParameters(parameters);
                camera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
            }
        });

        // Take a photo when the user clicks a button
        Button button = findViewById(R.id.takePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File pictureFile = getOutputMediaFile();
                if (pictureFile == null) {
                    Log.d("CAMERA", "Error creating media file, check storage permissions");
                    return;
                }
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Log.d("CAMERA", "File not found: " + e.getMessage());
                        } catch (IOException e) {
                            Log.d("CAMERA", "Error accessing file: " + e.getMessage());
                        }
                    }
                });
                // Load the image into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());

                // Display the image in an ImageView
                ImageView imageView = findViewById(R.id.imageView3);
                imageView.setImageBitmap(bitmap);
            }
        });
    }
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CAMERA", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public void requestCameraPermission(){
        //request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }
}
