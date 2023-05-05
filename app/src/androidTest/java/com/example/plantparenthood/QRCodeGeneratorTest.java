package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRCodeGeneratorTest {

    Bitmap qrCode;
    QRCodeManager qrMan;

    @Test
    public void createNegCode(){
        qrCode = qrMan.generateQRCodeBitmap("-450", 256);

        assertEquals(qrCode.getWidth(), 256);
            assertEquals(qrCode.getHeight(), 256);

    }

    @Test
    public void createZeroCode(){

        qrCode = qrMan.generateQRCodeBitmap("0", 256);

        assertEquals(qrCode.getWidth(), 256);
        assertEquals(qrCode.getHeight(), 256);

    }

    @Test
    public void createPosCode(){
        qrCode = qrMan.generateQRCodeBitmap("24", 256);

        assertEquals(qrCode.getWidth(), 256);
        assertEquals(qrCode.getHeight(), 256);

    }
}
