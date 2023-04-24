package com.example.plantparenthood;

import static android.graphics.Color.alpha;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LightScannerActivity extends AppCompatActivity implements View.OnClickListener{
    PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture= null;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_level);
        Button takePhoto = findViewById(R.id.image_capture_button);
        takePhoto.setOnClickListener(this);

        requestPermissions();
        previewView = findViewById(R.id.viewFinder);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            }catch(ExecutionException | InterruptedException e){
                e.printStackTrace();
            }

        }, getExecutor());

        //Bottom Navigation View
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.spaces);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.spaces:
                        startActivity(new Intent(getApplicationContext(),Spaces.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.plants:
                        startActivity(new Intent(getApplicationContext(),Plants.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),Calendar.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    private void takePhoto(){
        File file = new File(getApplicationContext().getExternalCacheDir() + File.separator + System.currentTimeMillis()+ ".png");

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(file).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback(){
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults){
                        calculateLightLevel(file);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception){
                        Toast.makeText(LightScannerActivity.this,"Error Saving Photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ImageWriting",exception.toString());
                    }
                }
        );
    }

    private Executor getExecutor(){
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider){
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this
                , cameraSelector
                , preview
                , imageCapture);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.image_capture_button:
                takePhoto();
                break;
        }
    }

    public void calculateLightLevel(File file){
        String photoPath = file.getPath();
        Bitmap bitmapImage = BitmapFactory.decodeFile(photoPath);
        double totalBrightness = 0;
        for(int i = 0; i < bitmapImage.getWidth(); i ++){
            for(int j = 0; j < bitmapImage.getHeight(); j++){
                int curPixel = bitmapImage.getPixel(i,j);
                double pixelBrightness = ((double)red(curPixel) + blue(curPixel) + green(curPixel))/765;
                totalBrightness += pixelBrightness;
            }
        }
        double brightness = 200*(totalBrightness/(bitmapImage.getHeight()*bitmapImage.getWidth()));
        Toast.makeText(LightScannerActivity.this
                ,"Brightness: " + String.format("%.1f",brightness) + "%"
                , Toast.LENGTH_SHORT).show();
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED)){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

    }
}
