package com.example.thofarm3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class productDetail extends AppCompatActivity {
    sanpham sanpham;
    TextView textview_tensanpham_chitiet;
    TextView textview_tensanpham_top;
    TextView textview_gia_top;
    TextView textview_xuatxu_chitiet;
    TextView textview_mota_chitiet;
    TextView textview_gia_chitiet;
    TextView textview_danhmuc_chitiet;
    ImageView imageview_product;
    Button button_chonmua;
    private boolean userMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_product_detail);
        Intent intent = getIntent();
        sanpham = (sanpham) getIntent().getSerializableExtra("thongtin");
        userMode = intent.getBooleanExtra("usermode",false);
        setTitle(sanpham.getTen());
        textview_tensanpham_top = findViewById(R.id.textview_tensanpham);
        textview_tensanpham_chitiet = findViewById(R.id.textview_ten);
        textview_gia_top = findViewById(R.id.textview_gia_top);
        textview_xuatxu_chitiet = findViewById(R.id.textview_xuatxu);
        textview_mota_chitiet = findViewById(R.id.textview_mota);
        textview_gia_chitiet = findViewById(R.id.textview_gia_chitiet);
        textview_danhmuc_chitiet = findViewById(R.id.textview_danhmuc_chitiet);
        imageview_product = findViewById(R.id.imageview_product);
        button_chonmua = findViewById(R.id.button_chonmua);

        chonMua();

        String danhmuc = "";
        switch (sanpham.getId_loai())
        {
            case 1:
                danhmuc = "Rau";
                break;
            case 2:
                danhmuc = "Củ";
                break;
            case 3:
                danhmuc = "Quả";
                break;

        }
        textview_tensanpham_top.append(sanpham.getTen());
        textview_tensanpham_chitiet.append(sanpham.getTen());
        textview_danhmuc_chitiet.append(danhmuc);
        textview_xuatxu_chitiet.append(sanpham.getXuatxu());
        textview_mota_chitiet.append(sanpham.getMota());
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        textview_gia_chitiet.append("" + decimalFormat.format(sanpham.getGia()) + " đ");
        textview_gia_top.append("" + decimalFormat.format(sanpham.getGia()) + " đ");
        Picasso.with(getApplicationContext()).load(sanpham.getHinhanh())
                .placeholder(R.drawable.ic_invoice)
                .error(R.drawable.ic_home)
                .into(imageview_product);
        Log.e("Mo ta","mo ta san pham: "+sanpham.getMota());
    }

    private void chonMua() {
        button_chonmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMode)
                    Toast.makeText(getApplicationContext(), "Đã thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_LONG).show();
                else
                {
                    finish();
                    Toast.makeText(getApplicationContext(), "Hãy đăng nhập để mua hàng!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    cuView.trigger.finish();
                }
            }
        });
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
