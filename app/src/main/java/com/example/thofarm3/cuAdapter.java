package com.example.thofarm3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class cuAdapter extends BaseAdapter {
    Context context;
    ArrayList<sanpham> mangcu;

    public cuAdapter(Context context, ArrayList<sanpham> mangcu) {
        this.context = context;
        this.mangcu = mangcu;
    }

    @Override
    public int getCount() {
        return mangcu.size();
    }

    @Override
    public Object getItem(int position) {
        return mangcu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder
    {
        public TextView textview_ten, textview_gia, textview_donvi, textview_xuatxu;
        public ImageView imageView_cu;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_holder, null);
            viewHolder.textview_ten = (TextView) view.findViewById(R.id.textview_tensanpham);
            viewHolder.textview_gia = (TextView) view.findViewById(R.id.textview_giasanpham);
            viewHolder.textview_donvi = (TextView) view.findViewById(R.id.textview_donvi);
            viewHolder.imageView_cu = (ImageView) view.findViewById(R.id.imageview_sanpham);
//            viewHolder.textview_xuatxu = (TextView) view.findViewById(R.id.textview_xuatxu);
            view.setTag(viewHolder);


        }
        else
        {
           viewHolder = (ViewHolder) view.getTag();
        }
        sanpham sanpham = (sanpham) getItem(i);
        viewHolder.textview_ten.setText(sanpham.getTen());
        /*DecimalFormat decimalFormat = new DecimalFormat("##.###");*/
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        viewHolder.textview_gia.setText("" + decimalFormat.format(sanpham.getGia()) + " đ");
        viewHolder.textview_donvi.setText(sanpham.getDonvi());
        Picasso.with(context).load(sanpham.getHinhanh())
                .placeholder(R.drawable.ic_invoice)
                .error(R.drawable.ic_home)
                .into(viewHolder.imageView_cu);
        return view;
    }
}
