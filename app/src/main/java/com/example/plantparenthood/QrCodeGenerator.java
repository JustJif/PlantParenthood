package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeGenerator extends AppCompatActivity {
    private String qrData = "-1";

    public void setData(int qrDataIn){
        this.qrData = Integer.toString(qrDataIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);

        //get references to the generate button and QR code image views.
        Button generateButton = findViewById(R.id.qrcode_generate_button);
        Button printButton = findViewById(R.id.qrcode_print_button);
        ImageView qrCodeImage = findViewById(R.id.qrcode_image);
        EditText debugNumberEntry = findViewById(R.id.debug_plantIDEntry);

        //set a click listener on the generate button. Generates the QR code and replaces qrCodeImage with the generated code.
        generateButton.setOnClickListener(v -> {
            //generate a QR code and set it to the image view.
            //get the text from debugNumberEntry to turn into a QR code.
            //eventually delete this when setQRdata is implemented elsewhere.
            qrData = (debugNumberEntry.getText().toString());
            int size = qrCodeImage.getWidth();
            QRCodeWriter writer = new QRCodeWriter();
            try{
                BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, size, size);
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

        //print the qr code on button click
        printButton.setOnClickListener(v ->{
            PrintHelper printer = new PrintHelper(this);
            printer.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            Bitmap bitmap = ((BitmapDrawable) qrCodeImage.getDrawable()).getBitmap();
            printer.printBitmap("qrcode", bitmap);
        });

    }
}