package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.print.PrintHelper;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.os.Bundle;


public class QRCodeGeneratorActivity extends AppCompatActivity {
    private String qrData = "-1";

    public void setData(int qrDataIn){
        this.qrData = Integer.toString(qrDataIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //notification testing! remove in final build!
        PPNotificationManager.createNotificationChannel(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);

        //get references to the generate button and QR code image views.
        Button generateButton = findViewById(R.id.qrcode_generate_button);
        Button printButton = findViewById(R.id.qrcode_print_button);
        ImageView qrCodeImage = findViewById(R.id.qrcode_image);
        EditText debugNumberEntry = findViewById(R.id.debug_plantIDEntry);

        //set a click listener on the generate button. Generates the QR code and replaces qrCodeImage with the generated code.
        generateButton.setOnClickListener(v -> {

            //notification testing! remove in final build!
            NotificationManagerCompat notifMan = NotificationManagerCompat.from(this);
            Notification waterNotif = PPNotificationManager.getWaterNotification(1, "Alisaie", this);
            notifMan.notify(1, waterNotif);

            //generate a QR code and set it to the image view.
            //get the text from debugNumberEntry to turn into a QR code.
            //eventually delete this because we'll just implement the call to QRCodeManager.generateQRCodeBitmap elsewhere.
            qrData = (debugNumberEntry.getText().toString());
            qrCodeImage.setImageBitmap(QRCodeManager.generateQRCodeBitmap(qrData, qrCodeImage.getWidth()));

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