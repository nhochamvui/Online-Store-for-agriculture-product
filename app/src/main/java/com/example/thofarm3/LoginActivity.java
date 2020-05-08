package com.example.thofarm3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener
{
    private EditText editText_username;
    private EditText editText_password;
    private CheckBox checkbox_rememberme;
    private Button button_login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    private TextView textview_registerlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Đăng nhập");
        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editText_username = findViewById(R.id.edittext_username);
        editText_password = findViewById(R.id.edittext_password);
        checkbox_rememberme = findViewById(R.id.checkbox_rememberme);
        textview_registerlink = findViewById(R.id.edittext_registerlink);
        textview_registerlink.setText(Html.fromHtml("<u>Chưa có tài khoản? Đăng ký ngay tại đây!<u>"));
        textview_registerlink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://thofarm.000webhostapp.com/register.php"));
                startActivity(browserIntent);
            }
        });
        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            checkbox_rememberme.setChecked(true);
        else checkbox_rememberme.setChecked(false);
        editText_username.setText(sharedPreferences.getString(KEY_USERNAME,""));
        editText_password.setText(sharedPreferences.getString(KEY_PASS,""));
        editText_username.addTextChangedListener(this);
        editText_username.addTextChangedListener(this);
        checkbox_rememberme.setOnCheckedChangeListener(this);
        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("login","\nTIEN HANH CHECK TAI KHOAN!!!!\n");
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String link = "https://thofarm.000webhostapp.com/thofarm/checkLogin.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*if(response.contains("not ok"))
                            Log.e("JSON","\nsai mat khau, "+response);
                        else Log.e("JSON","\ndung mat khau, "+response);*/
                        Log.e("Response","\nResponse: "+response);
                        if(response.equals("ok")){
                            finish();
                            Intent intent = new Intent(getApplicationContext(), shoppingView.class);
                            intent.putExtra("username",String.valueOf(editText_username.getText()));
                            Log.e("intent content","\nintent send: "+String.valueOf(editText_username.getText()));
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Sai tài khoản hoặc mật khẩu! Xin thử lại!",Toast.LENGTH_LONG).show();
                            Log.e("Response", "\nmat khau sai: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login","\nLOI: "+error);

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> param = new HashMap<>();
                        param.put("username",String.valueOf(editText_username.getText()));
                        param.put("password",String.valueOf(editText_password.getText()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        managePrefs();
    }
    private void managePrefs(){
        if(checkbox_rememberme.isChecked()){
            editor.putString(KEY_USERNAME, editText_username.getText().toString().trim());
            editor.putString(KEY_PASS, editText_password.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }
}
