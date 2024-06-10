package com.example.camaccess;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView myimg;
    ImageButton btncapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        myimg = findViewById(R.id.myimg);
        btncapture = findViewById(R.id.btncapture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btncapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 1);
                    return;
                }
                startActivityForResult(intent, 99);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            myimg.setImageBitmap(photo);
            saveImage(photo);
        }
    }
    private void saveImage(Bitmap photo) {
        //gets the standard public directory on the device's external storage where images are typically saved
        File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //creates a unique filename for the image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());

        String filename = "IMG_" + timeStamp + ".jpg";
        //creates a new file in the directory with the unique filename
        File imgFile = new File(storage, filename);

        try {
            //writes the image to the file
            FileOutputStream outputStream = new FileOutputStream(imgFile);
            //compresses the image to JPEG format with a quality of 100
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //flushes the output stream and closes it
            outputStream.flush();
            outputStream.close();
            //sends a broadcast to the media scanner to refresh the gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(imgFile));
            sendBroadcast(mediaScanIntent);
            //displays a toast message to notify the user that the image was saved to the gallery
            Toast.makeText(this, "Image saved to Gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //catches any exceptions that may occur during the process
            e.printStackTrace();
        }
    }
}