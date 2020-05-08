package com.example.thofarm3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thofarm3.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

public class uploadProductActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button button_takePicture;
    Button button_upload;
    ImageView imageView_pictureUpload;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        button_takePicture = findViewById(R.id.button_takePicture);
        button_upload = findViewById(R.id.button_uploadPicture);
        imageView_pictureUpload = findViewById(R.id.imageView_pictureUpload);

        button_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permisson = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permisson,PERMISSION_CODE);
                    }
                    else
                    {
                        openCamera();
                    }
                }
                else
                {
                    openCamera();
                }
            }
        });
    }



    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
        if(image_uri != null) {
            Log.e("openCam()", "uri: " + image_uri);
            uploadProduct();
            Log.e("upload:", "Upload conpleted!");
        }
    }

    private void uploadProduct() {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(String.valueOf(image_uri));

        if (sourceFile.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                String upLoadServerUri = "http://thofarm.000webhost.com/thofarm/uploadProduct.php?";
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("bill", String.valueOf(image_uri));
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\"" + String.valueOf(image_uri) + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                if (serverResponseCode == 200) {
                    Toast.makeText(this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                    Log.e("upload:", "Upload conpleted!");
                }
                else Log.e("upload:", "serverResponseCode != 200!");
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                        openCamera();
                else
                    Toast.makeText(this, "Hãy cho phép ứng dụng chụp ảnh",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK)
        {
            imageView_pictureUpload.setImageURI(image_uri);


            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(String.valueOf(image_uri));

            if(sourceFile.isFile())
            {
                try
                {
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    String upLoadServerUri = "http://thofarm.000webhost.com/thofarm/uploadProduct.php?";
                    URL url = new URL(upLoadServerUri);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection","Keep-Alive");
                    conn.setRequestProperty("ENCTYPE","multipart/form-data");
                    conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
                    conn.setRequestProperty("bill", String.valueOf(image_uri));
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\"" + String.valueOf(image_uri) + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while(bytesRead > 0)
                    {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable,maxBufferSize);
                        bytesRead = fileInputStream.read(buffer,0,bufferSize);

                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();
                    if(serverResponseCode == 200)
                    {
                        Toast.makeText(this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        Log.e("upload:","Upload conpleted!");
                    }
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}
