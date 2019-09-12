package com.example.twobirdwithonestone.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twobirdwithonestone.R;

import java.util.ArrayList;

public class CouponRecyclerViewAdapter extends RecyclerView.Adapter<CouponRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Coupon> mData = null ;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public CouponRecyclerViewAdapter(ArrayList<Coupon> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public CouponRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_item, parent, false) ;
        return new ViewHolder(view) ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Coupon coupon = mData.get(position) ;
        holder.imgCoupon.setImageResource(R.drawable.circle_logo_b);
        holder.tvCouponName.setText(coupon.couponName);
        holder.tvCouponCreateTime.setText(coupon.couponCreateTime);
        holder.tvCouponUsedOrNot.setText(coupon.couponUesrOrNot.toString());
        holder.btnCouponUse.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(), "SETTINGS에서 START SERVICE", Toast.LENGTH_LONG).show();
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgCoupon;
        public TextView tvCouponName;
        public  TextView tvCouponCreateTime;
        public  TextView tvCouponUsedOrNot;
        public  Button btnCouponUse;

        public ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            imgCoupon = itemView.findViewById(R.id.imgCoupon);
            tvCouponName = itemView.findViewById(R.id.tvCouponName);
            tvCouponCreateTime = itemView.findViewById(R.id.tvCouponCreateTime);
            tvCouponUsedOrNot = itemView.findViewById(R.id.tvCouponUsedOrNot);
            btnCouponUse = itemView.findViewById(R.id.btnCouponUse);
        }
    }
}