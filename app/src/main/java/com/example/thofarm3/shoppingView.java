package com.example.thofarm3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class shoppingView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    ViewFlipper adFlipper;
    NavigationView navigationView;
    Button rauButton;
    Button cuButton;
    Button traiButton;
    //ListView listMenu;
    DrawerLayout drawerLayout;
    public static boolean userMode = false;
    private ActionBarDrawerToggle mToggle;
    /*ViewFlipper viewFlipper;*/
    Intent intent = getIntent();
    //show and hide cart icon on the right top bar
    private MenuItem cart;
    private MenuItem userInfo;
    private String username="";
    public static boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAdmin = false;
        setContentView(R.layout.activity_shopping_view);
        Log.e("usermode",String.valueOf(userMode));
        Log.e("isAdmin",String.valueOf(isAdmin));
        //INITIALIZE
        adFlipper = findViewById(R.id.adFlipper);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        if(username.equals("admin"))
            isAdmin = true;
        else isAdmin = false;
        if(!username.equals("")) {
            userMode = true;
            Log.e("username",username);
        }
        else userMode = false;
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        Log.e("usermode",String.valueOf(userMode));
        Log.e("isAdmin",String.valueOf(isAdmin));
        if(userMode) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_usermode);
            Menu menu = navigationView.getMenu();
            userInfo = menu.findItem(R.id.menu_user_info);

            userInfo.setTitle(username);
        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_anonymousmode);
        }
        navigationView.setNavigationItemSelectedListener(this);
        //listMenu = findViewById(R.id.listMenu);
        drawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* viewFlipper = findViewById(R.id.view_flipper);*/

        /*int images[] = {R.drawable.logo3,R.drawable.logo2,R.drawable.logo1};
        for(int image:images)
        {
            flipperImage(image);
        }*/

        //getSupportActionBar().hide();
        if(userInfo == null)
            Log.e("object","\nNULL");
        else Log.e("object","\nnot NULL");
        categoryButtonListener();
        actionAdFlipper();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        //-1000222
/*        Log.e("menu:","clicked -> "+id);*/
        switch (id) {
            case R.id.menu_home:
                Log.e("menu:", "clicked!!");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://thofarm.000webhostapp.com"));
                startActivity(browserIntent);
                return true;
            case R.id.menu_logout:
            {
                finish();
                Intent intent = new Intent(getApplicationContext(), shoppingView.class);
                userMode = false;
                intent.putExtra("username","");
                startActivity(intent);
                Log.e("upload","logout clicked!");
                return true;
            }
            case R.id.menu_uploadproduct:
            {
                Intent intent = new Intent(shoppingView.this, uploadProduct3.class);
                Log.e("upload","upload clicked!");
                startActivity(intent);
                return true;
            }
            case R.id.menu_login:
            {
                finish();
                Intent intent = new Intent(shoppingView.this,LoginActivity.class);
                startActivity(intent);
                Log.e("upload","logout clicked!");
                return true;

            }
            case R.id.menu_invoice:
            {
                Log.e("upload","hoa don clicked!");
                return true;
            }
            case R.id.menu_shopping_cart:
            {
                Log.e("upload","gio hang clicked!");
                return true;
            }
            case R.id.menu_user_info:
            {
                Log.e("upload","thong tin user clicked!");
                return true;
            }
            case R.id.menu_register:
            {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW);
                browserIntent2.setData(Uri.parse("http://thofarm.000webhostapp.com/register.php"));
                startActivity(browserIntent2);
                return true;
            }
        }
        return false;
    }

    public void categoryButtonListener() {
        rauButton = findViewById(R.id.button_rau);
        cuButton = findViewById(R.id.button_cu);
        traiButton = findViewById(R.id.button_trai);
        rauButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shoppingView.this, cuView.class);
                intent.putExtra("id_loaisanpham", 1);
                intent.putExtra("usermode",userMode);
                intent.putExtra("isAdmin",isAdmin);
                startActivity(intent);
            }
        });
        cuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shoppingView.this, cuView.class);
                intent.putExtra("id_loaisanpham", 2);
                intent.putExtra("usermode",userMode);
                intent.putExtra("isAdmin",isAdmin);
                startActivity(intent);
            }
        });
        traiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shoppingView.this, cuView.class);
                intent.putExtra("id_loaisanpham", 3);
                intent.putExtra("usermode",userMode);
                intent.putExtra("isAdmin",isAdmin);
                startActivity(intent);
            }
        });
    }

    //SIDE BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;
        switch(item.getItemId())
        {
            case R.id.right_side_cart: {
                Log.e("cart", "Clicked!");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    /*public void flipperImage(int image)
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, R.anim.slide_to_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_to_right);
    }*/


    private void actionAdFlipper()
    {
        ArrayList<String> featureProducts = new ArrayList<>();
        featureProducts.add("https://www.upsieutoc.com/images/2019/10/24/momo-upload-api-tiki_kv_770x370-181109084737.png");
        featureProducts.add("https://www.upsieutoc.com/images/2019/10/24/ad2.jpg");
        featureProducts.add("https://www.upsieutoc.com/images/2019/10/24/ad1.jpg");
        featureProducts.add("https://www.upsieutoc.com/images/2019/10/24/ad3.jpg");
        featureProducts.add("https://www.upsieutoc.com/images/2019/10/24/ad5.jpg");
        for(int i = 0; i<featureProducts.size(); i++)
        {
            ImageView temp = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(featureProducts.get(i)).into(temp);
            temp.setScaleType(ImageView.ScaleType.FIT_XY);
            adFlipper.addView(temp);
        }
        adFlipper.setFlipInterval(3000);
        adFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_to_left);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_disappear_on_left);
        adFlipper.setInAnimation(slide_in);
        adFlipper.setOutAnimation(slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(userMode) {
            getMenuInflater().inflate(R.menu.cart, menu);
            cart = menu.findItem(R.id.right_side_cart);
            cart.setVisible(true);
        }


        return true;
    }
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
