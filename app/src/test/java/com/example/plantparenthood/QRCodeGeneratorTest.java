package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(JUnit4.class)
public class QRCodeGeneratorTest {

    Bitmap qrCode;

    @Test
    public void createNegCode(){
        qrCode = QRCodeManager.generateQRCodeBitmap("-500", 256);
        assertEquals(qrCode.getWidth(), 256);
        assertEquals(qrCode.getWidth(), 256);
    }

    @Test
    public void createZeroCode(){
        qrCode = QRCodeManager.generateQRCodeBitmap("0", 256);
        assertEquals(qrCode.getWidth(), 256);
        assertEquals(qrCode.getWidth(), 256);
    }

    @Test
    public void createPosCode(){
        qrCode = QRCodeManager.generateQRCodeBitmap("400", 256);
        assertNotNull(qrCode);
        assertEquals(qrCode.getWidth(), 256);
        assertEquals(qrCode.getWidth(), 256);
    }
}
