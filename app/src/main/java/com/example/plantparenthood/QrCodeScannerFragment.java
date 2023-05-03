package com.example.plantparenthood;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.Result;



public class QrCodeScannerFragment extends Fragment implements View.OnClickListener {

    /**
     * private class will let us asynchronously query the database.
     */
    private class DatabaseAsyncTask extends AsyncTask<Void, Void, Plant> {
        private DatabaseHandler mDb;
        private int mPlantID;

        public DatabaseAsyncTask(DatabaseHandler db, int plantID){
            mDb = db;
            mPlantID = plantID;
        }

        @Override
        protected Plant doInBackground(Void... voids) {
            return mDb.getPlantFromDBbyID(mPlantID);
        }

        @Override
        protected void onPostExecute(Plant plant) {
            // Use the plant object returned by doInBackground() here
            if (plant != null) {
                mPlantID = plant.getId();
                //do stuff with the plant we found
                //transition to the plant activity using the plant ID...
                Toast.makeText(getActivity(), Integer.toString(mPlantID), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Plant not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private CodeScanner mCodeScanner;


    public QrCodeScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr_code_scanner, container, false);



        final Activity activity = getActivity();
        DatabaseHandler db = DatabaseHandler.getDatabase(activity);

        //request camera permission
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 100);
        }


        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback(){
            @Override
            public void onDecoded(@NonNull final Result result){
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        try{
                            int parsedInt = Integer.parseInt(result.getText());
                            new DatabaseAsyncTask(db, parsedInt).execute();
                        }catch(NumberFormatException e){
                            Toast.makeText(activity, "The QR Code is not valid...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onClick(View view) {

    }

}