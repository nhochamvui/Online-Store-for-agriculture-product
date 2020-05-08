package com.example.thofarm3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cuView extends AppCompatActivity {
//    Toolbar toolbar;
    ListView listView;
    cuAdapter cuAdapter;
    static ArrayList<sanpham> mangsanpham;
//    Button button_back;

    View loadingbarView;
    boolean isLoading = false;
    mHandler mHandler;
    boolean isLimitData = false;
    boolean hasbeenPaused = false;
    private boolean userMode;
    private boolean isAdmin;

    int page = 1;
    int id_loaisanpham = 1;// Category: Củ
    public static Activity trigger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trigger = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_cu_view);

        setTitle("Sản phẩm từ các nông trại");


        Intent intent = getIntent();
        id_loaisanpham = intent.getIntExtra("id_loaisanpham",-1);
        userMode = intent.getBooleanExtra("usermode",false);
        isAdmin = intent.getBooleanExtra("isAdmin",false);
        /*Log.d(cuView.class.getSimpleName(),"\nintent! id_loaisanpham="+id_loaisanpham);*/
        initialize();
        getDataFromHost(page);
        fetchMoreData();
    }



    public void fetchMoreData() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && isLoading == false && isLimitData == false)
                {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    public void getDataFromHost(int page) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String link = connector.link_getsanpham_webhost+String.valueOf(page)+"&id_loaisanpham="+String.valueOf(id_loaisanpham);
        /*String link = "http://192.168.1.3/thofarm/getsanpham.php?page=1&id_loaisanpham=1";*/
        Log.e(cuView.class.getSimpleName(),"\nTien hanh lay du lieu, page="+page);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Ten="";
                int Gia = 0;
                String Hinhanh = "";
                String Donvi = "";
                String Mota = "";
                String xuatxu = "";
                Log.e(cuView.class.getSimpleName(),"\nresponse.length="+response.length());
                if(response.length() > 3)//neu json tra ve la rong thi response = 3, nguoc lai response luon lon hon 3
                {
                    listView.removeFooterView(loadingbarView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i  = 0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id_sanpham");
                            Ten = jsonObject.getString("ten_sanpham");
                            Gia = jsonObject.getInt("gia_sanpham");
                            Donvi = jsonObject.getString("donvi_sanpham");
                            xuatxu = jsonObject.getString("xuatxu_sanpham");
                            Hinhanh = jsonObject.getString("hinhanh_sanpham");
                            Mota = jsonObject.getString("mota_sanpham");
                            mangsanpham.add(new sanpham(id,Ten,Gia,Donvi,xuatxu,Mota,Hinhanh,1));
                            Log.e("add","\nAdding -> mang size: "+mangsanpham.size());
                            Log.e("hehe","\nlay du lieu thanh cong!\n"+id+Ten+Gia+Donvi+Mota+Hinhanh);
                            cuAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("hehe","\nloi lay du lieu??????????????????\n"+id+Ten+Gia+Donvi+Mota+Hinhanh);
                    }
                }
                else
                {
                    isLimitData = true;
                    listView.removeFooterView(loadingbarView);
                    Log.e("getDataFromHost","\nHet du lieu!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Hehe","\nloi listener???????????????????????"+error.toString());
            }
        });
        requestQueue.add(stringRequest);
        Log.e("END","\nKet Thuc! mang size: "+mangsanpham.size());
    }

    public void initialize() {
        isLoading = false;
        isLimitData = false;
        page = 1;
        mHandler = new mHandler();
        mangsanpham = new ArrayList<>();
        Log.e("hehe","\nsize mang: "+mangsanpham.size());
        listView = findViewById(R.id.listview_sanpham);
        cuAdapter = new cuAdapter(getApplicationContext(),mangsanpham);

        //Admin command
        if(isAdmin)
            registerForContextMenu(listView);

        listView.setAdapter(cuAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),productDetail.class);
                intent.putExtra("thongtin",mangsanpham.get(position));
                intent.putExtra("usermode",userMode);
                startActivity(intent);
            }
        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if(shoppingView.isAdmin) {
//                    Log.e("Admin", "admin has clicked!");
//                    showAlertDialog(mangsanpham.get(position));
//                }
//                return true;
//            }
//        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        loadingbarView = inflater.inflate(R.layout.loadingbar, null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_command, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        sanpham sanpham = mangsanpham.get(info.position);
        switch (item.getItemId())
        {
            case R.id.menu_delete_product:
                showAlertDialog(sanpham);
                return true;
            case R.id.menu_edit_product:
                Intent intent = new Intent(this, uploadProduct3.class);
                intent.putExtra("thongtin",sanpham);
                onPause();
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void showAlertDialog(sanpham sanpham)
    {
        final String id = String.valueOf(sanpham.getId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc muốn xóa sản phẩm này không?");
        builder.setCancelable(true);
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("Xác nhận","xác nhận xóa!");
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String link = "http://thofarm.000webhostapp.com/thofarm/deleteProduct.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,link, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("POST","\nloi listener "+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> param = new HashMap<>();
                        param.put("id",id);
                        Log.e("id san pham bi xoa",id);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public class mHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0: listView.addFooterView(loadingbarView);
                    break;
                case 1:
                    getDataFromHost(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread
    {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        hasbeenPaused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(hasbeenPaused)
        {
            Log.e("hasbeenPaused","true!");
            initialize();
            getDataFromHost(page);
            fetchMoreData();
        }
        else Log.e("hasbeenPaused","false!");
        super.onResume();
    }
}
