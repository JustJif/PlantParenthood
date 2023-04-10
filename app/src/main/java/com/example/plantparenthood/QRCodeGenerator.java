package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.os.Bundle;
import android.widget.NumberPicker;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator extends AppCompatActivity {
    private String qrData = "-1";

    public void setData(String qrData){
        this.qrData = qrData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        //get references to the generate button and QR code image views.
        Button generateButton = findViewById(R.id.qrcode_generate_button);
        ImageView qrCodeImage = findViewById(R.id.qrcode_image);
        EditText debugNumberEntry = findViewById(R.id.debug_plantIDEntry);

        //set a click listener on the generate button.
        generateButton.setOnClickListener(v -> {
            //generate a QR code and set it to the image view.
            String plantIDString = "-1";
            //get the text from debugNumberEntry to turn into a QR code.
            plantIDString = debugNumberEntry.getText().toString();
            int size = qrCodeImage.getWidth();
            QRCodeWriter writer = new QRCodeWriter();
            try{
                BitMatrix bitMatrix = writer.encode(plantIDString, BarcodeFormat.QR_CODE, size, size);
                int[] pixels = new int[size*size];
                for(int y = 0; y<size; y++){
                    int offset = y*size;
                    for(int x = 0; x<size; x++){
                        pixels[offset+x] = bitMatrix.get(x, y)? 0xFF000000: 0xFFFFFFFF;
                    }
                }
                Bitmap qrCodeBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
                qrCodeBitmap.setPixels(pixels, 0, size, 0, 0, size, size);
                qrCodeImage.setImageBitmap(qrCodeBitmap);
            }catch(WriterException e){
                e.printStackTrace();
            }

        });

        return;
    }
}