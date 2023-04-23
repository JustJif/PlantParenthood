package com.example.plantparenthood;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
public class LightLevelScanner {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    Camera camera;
    public LightLevelScanner(AppCompatActivity originActivity) {
        requestCameraPermission(originActivity);
        camera = Camera.open();
        listResolutions();
    }

    public void createSurfaceHolder(SurfaceView surfaceView) {
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera = Camera.open();
                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

                // You need to choose the most appropriate previewSize for your app
                Camera.Size previewSize = camera.new Size(1280, 720);
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
    }

    public void takePhoto(ImageView imageView){
        File pictureFile = getOutputMediaFile();
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
        imageView.setImageBitmap(bitmap);
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

    public void requestCameraPermission(AppCompatActivity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    public void listResolutions(){
        Camera camera = Camera.open();
        List<Camera.Size> allSizes = camera.getParameters().getSupportedPreviewSizes();
        for(int i = 0; i < allSizes.size(); i++)
        {
            System.out.println(allSizes.get(i).width + " " +allSizes.get(i).height);
        }
    }
}
