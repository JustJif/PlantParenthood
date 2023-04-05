package com.example.plantparenthood;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeManager {

    public static Bitmap generateQRCodeBitmap(String qrData, int size){
        Bitmap qrCodeBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565 );
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
            qrCodeBitmap.setPixels(pixels, 0, size, 0, 0, size, size);

        }catch(WriterException e){
            e.printStackTrace();
        }
        return qrCodeBitmap;
    }
}