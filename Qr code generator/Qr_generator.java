package com.sayed.smartattendencestudentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.sayed.smartattendencestudentapp.singletones.App;
import com.sayed.smartattendencestudentapp.singletones.FirebaseAuthController;

import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MainActivity extends AppCompatActivity {

    public final static int WIDTH = 500;
    TextView displayTv, userNameTv,userIdTv,userEmailTv;
//    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView =  findViewById(R.id.qr_view);
        CircleImageView userImage = findViewById(R.id.user_image);
        displayTv = findViewById(R.id.display_text);
        userNameTv =findViewById(R.id.user_name);
        userEmailTv = findViewById(R.id.user_email);
        userIdTv = findViewById(R.id.student_id);
        FloatingActionButton fabScan = findViewById(R.id.fab_scanner);
        ImageView logOut = findViewById(R.id.log_out);


        userEmailTv.setText(FirebaseAuthController.getCurrentUser().getEmail());
        userNameTv.setText(App.preferences.getString("student_name",""));
        userIdTv.setText(App.preferences.getString("student_id",""));

        Glide.with(this)
                .load(FirebaseAuthController.getCurrentUser().getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_student)
                .into(userImage);



//generate Qr CODE....................... 
---------------------------------------------------------------------------------------------
        try {
            Bitmap bitmap = encodeAsBitmap(
                    App.preferences.getString("student_name","")+","
                            +App.preferences.getString("student_id","")+","
                            +App.preferences.getString("device_id","")
            );
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }



//scan code Qr....................... 
---------------------------------------------------------------------------------------------
        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuthController.signOut(MainActivity.this);
                startActivity( new Intent(MainActivity.this, AuthActivity.class) );
                finish();
            }
        });

    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

                displayTv.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
