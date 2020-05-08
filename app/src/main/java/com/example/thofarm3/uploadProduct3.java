package com.example.thofarm3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thofarm3.Retrofit2.ApiClient;
import com.example.thofarm3.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class uploadProduct3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imageview_framePicture;
    Button button_upload;
    int Request_Code_Image = 123;
    String realPath = "";
    private EditText tensanpham;
    private EditText gia;
    private EditText danhmuc;
    private EditText xuatxu;
    private EditText mota;
    private EditText donvi;
    private Spinner spinner_danhmuc;
    private int id_loaisanpham;
    private String filename="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product3);
        Intent intent = getIntent();
        sanpham sanpham = (sanpham) getIntent().getSerializableExtra("thongtin");
        imageview_framePicture = findViewById(R.id.imageview_framePicture);
        button_upload = findViewById(R.id.button_upload);
        tensanpham = findViewById(R.id.edittext_tensanpham);
//        danhmuc = findViewById(R.id.edittext_danhmuc);
        xuatxu = findViewById(R.id.edittext_xuatxu);
        mota = findViewById(R.id.edittext_mota);
        gia = findViewById(R.id.edittext_gia);
        donvi = findViewById(R.id.edittext_donvi);
        spinner_danhmuc = findViewById(R.id.spinner_danhmuc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.danhmuc, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_danhmuc.setAdapter(adapter);
        spinner_danhmuc.setOnItemSelectedListener(this);
        if(sanpham == null) {
            imageview_framePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, Request_Code_Image);
                }
            });
            upload();
        }
        else
        {
            Picasso.with(getApplicationContext()).load(sanpham.getHinhanh())
                    .placeholder(R.drawable.ic_invoice)
                    .error(R.drawable.ic_home)
                    .into(imageview_framePicture);
            imageview_framePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, Request_Code_Image);
                }
            });
            String danhmuc_string = "";
            switch (sanpham.getId_loai())
            {
                case 1:
                    danhmuc_string = "Rau";
                    break;
                case 2:
                    danhmuc_string = "Củ";
                    break;
                case 3:
                    danhmuc_string = "Quả";
                    break;
            }
            tensanpham.append(sanpham.getTen());
            xuatxu.append(sanpham.getXuatxu());
            mota.append(sanpham.getMota());
            gia.append(String.valueOf(sanpham.getGia()));
            donvi.append(sanpham.getDonvi());
            edit(sanpham);

        }

    }
    public void upload()
    {
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(realPath);
                String file_path = file.getAbsolutePath();
                String[] arrayNameFile = file_path.split("\\.");
                file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
                Log.e("file_path", file_path.substring(file_path.lastIndexOf("/")));
                filename = file_path.substring(file_path.lastIndexOf("/"));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("productPhoto", file_path, requestBody);
                DataClient dataClient = ApiClient.getApiClient();
                retrofit2.Call<String> callback = dataClient.uploadPhoto(body);

                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                        if (response != null) {
                            String message = response.body();
                            Log.e("response FILE not null", message);
                        } else Log.e("response FILE null", response.body());
                    }

                    @Override
                    public void onFailure(retrofit2.Call<String> call, Throwable t) {
                        Log.e("FAIL", t.getMessage());
                    }
                });
                //upload thong tin san pham

                Log.e("UPLOAD", "\nTIEN HANH UPLOAD THONG TIN SAN PHAM!!!!\n");
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String link = "https://thofarm.000webhostapp.com/thofarm/uploadProductInfo.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "\nResponse: " + response);
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UPLOAD", "\nLOI: " + error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("tensanpham", String.valueOf(tensanpham.getText()));
                        param.put("danhmuc", String.valueOf(id_loaisanpham));
                        param.put("xuatxu", String.valueOf(xuatxu.getText()));
                        param.put("mota", String.valueOf(mota.getText()));
                        param.put("gia", String.valueOf(gia.getText()));
                        param.put("donvi", String.valueOf(donvi.getText()));
                        String link_hinh_anh = "http://thofarm.000webhostapp.com/thofarm" + String.valueOf(filename);
                        Log.e("link_hinh_anh", link_hinh_anh);
                        param.put("hinhanh", link_hinh_anh);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }
    public void edit(sanpham sanpham1)
    {
        final sanpham sanpham = sanpham1;
        Log.e("sanpham info", String.valueOf(sanpham.getTen()));
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(realPath);
                String file_path = file.getAbsolutePath();
                Log.e("file_path", file_path);
                if(file_path.equals("/"))
                {
                    Log.e("file_path", "file null");
                }
                else {
                    Log.e("file_path", "file not null");
                    String[] arrayNameFile = file_path.split("\\.");
                    Log.e("file_path", file_path);
                    file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
                    Log.e("file_path", file_path.substring(file_path.lastIndexOf("/")));
                    filename = file_path.substring(file_path.lastIndexOf("/"));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("productPhoto", file_path, requestBody);
                    DataClient dataClient = ApiClient.getApiClient();
                    retrofit2.Call<String> callback = dataClient.uploadPhoto(body);

                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                            if (response != null) {
                                String message = response.body();
                                Log.e("response FILE not null", message);
                            } else Log.e("response FILE null", response.body());
                        }

                        @Override
                        public void onFailure(retrofit2.Call<String> call, Throwable t) {
                            Log.e("FAIL", t.getMessage());
                        }
                    });
                }
                //upload thong tin san pham

                Log.e("EDIT", "\nTIEN HANH EDIT THONG TIN SAN PHAM!!!!\n");
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String link = "https://thofarm.000webhostapp.com/thofarm/editProduct.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Response", "\nResponse: " + response);
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UPLOAD", "\nLOI: " + error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("idsanpham", String.valueOf(sanpham.getId()));
                        param.put("tensanpham", String.valueOf(tensanpham.getText()));
                        param.put("danhmuc", String.valueOf(id_loaisanpham));
                        param.put("xuatxu", String.valueOf(xuatxu.getText()));
                        param.put("mota", String.valueOf(mota.getText()));
                        param.put("gia", String.valueOf(gia.getText()));
                        param.put("donvi", String.valueOf(donvi.getText()));
                        String link_hinh_anh;
                        if(filename.equals(""))
                             link_hinh_anh = sanpham.getHinhanh();
                        else link_hinh_anh = "http://thofarm.000webhostapp.com/thofarm" + String.valueOf(filename);
                        Log.e("link_hinh_anh", link_hinh_anh);
                        param.put("hinhanh", link_hinh_anh);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Request_Code_Image && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageview_framePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri,proj,null,null,null);
        String path = null;
        if(cursor.moveToFirst())
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text)
        {
            case "Rau":
                id_loaisanpham = 1;
                break;
            case "Củ":
                id_loaisanpham = 2;
                break;
            case "Quả":
                id_loaisanpham = 3;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
