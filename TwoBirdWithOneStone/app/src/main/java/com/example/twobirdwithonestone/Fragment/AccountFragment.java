package com.example.twobirdwithonestone.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twobirdwithonestone.Activity.Coupon;
import com.example.twobirdwithonestone.Activity.CouponRecyclerViewAdapter;
import com.example.twobirdwithonestone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AccountFragment extends Fragment {
    private static final int REQUEST_ZEROPAY = 200;
    private FirebaseFirestore db;
    private CouponRecyclerViewAdapter couponAdapter;
    private String currentTime;
    private String currentUID;
    private boolean queryIsDone = false;
    private RecyclerView recyclerView;
    Button btn_zeropay;
    private Coupon myCounpon = null;
    public ArrayList<Coupon> couponList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        //제로페이 전환 버튼
//        btn_zeropay = view.findViewById(R.id.zeropay_bnt);
//        btn_zeropay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ZeropayActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivityForResult(intent,REQUEST_ZEROPAY);
//            }
//        });

        //DB의 User정보 가져와서 보여주기
        final TextView tvUserPoint = view.findViewById(R.id.tvUserPoint);
        //final TextView tvUserName = view.findViewById(R.id.tvUserName);
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    tvUserPoint.setText(snapshot.get("point").toString());
                    //tvUserName.setText(snapshot.get("uid").toString());
                    Log.d(TAG, "Current data: " + snapshot.get("point"));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = view.findViewById(R.id.recyclerViewCoupon) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        couponAdapter = new CouponRecyclerViewAdapter(getActivity(), couponList);
        couponAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(couponAdapter);


        //2019-09-25
        //For Degub. Do not delete this message. - hwanseok
        Log.d("QueryCoupons", "return view 합니다?");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //쿠폰 Recycler View default 객체 생성
        couponList = new ArrayList<Coupon>();
        currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Coupons")
                .whereEqualTo("userUID", currentUID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        //로그아웃 시 여기서 앱이 터져서 null 체크를 해주어야 한다.
                        if(documentSnapshots == null) return;
                        if(documentSnapshots.isEmpty()){
                            Log.d("QueryCoupons", "쿠폰이 비었어요");
                        }else{
                            couponList.clear();
                            for(DocumentSnapshot snapshot :documentSnapshots.getDocuments()){
                                if(snapshot.exists()){

                                    //쿠폰의 각 필드를 일일이 가져와서 새로운 쿠폰 객체를 만든다.
                                    //.toObject를 쓰면 터져서 그냥 이렇게 한다. 변수명은 여기 부분에서만 쓰이니까 무시해도 됨.
                                    String str1 = snapshot.get("couponCreateTime").toString();
                                    Log.d("QueryCoupons", "이건 가져와요" + str1);
                                    String str2 = snapshot.get("couponName").toString();
                                    String str3 = snapshot.get("couponUID").toString();
                                    Boolean a = (Boolean)snapshot.get("couponUesrOrNot");
                                    String str4 = snapshot.get("userUID").toString();
                                    int b = Integer.parseInt(snapshot.get("couponImgIndex").toString());
                                    myCounpon = new Coupon(str1, str2, str3, a, str4, b);
                                    couponList.add(myCounpon);
                                    Log.d("QueryCoupons", "쿠폰이 추가했어요 "+couponList.size());
                                }else{
                                    Log.d("QueryCoupons", "쿠폰이 이상하네요");
                                }
                            }
                        }
                    }
                });


        //If Default coupon exists, add default coupon to couponList.
//        for (int i=0; i<1; i++) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
//            Calendar cal = Calendar.getInstance();
//            currentTime = sdf.format(cal.getTime());
//            Coupon c = new Coupon(currentTime, "쿠폰hh", currentUID, false, currentUID,1 );
//            couponList.add(c) ;
//            Log.d("QueryCoupons", "default 쿠폰이 추가했어요");
//        }

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
//        Calendar cal = Calendar.getInstance();
//        currentTime = sdf.format(cal.getTime());
//        Coupon welcomeCoupon = new Coupon(currentTime, "가입 축하 쿠폰 : 아메리카노", currentUID, false, currentUID,301 );
//        couponList.add(welcomeCoupon) ;
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ZEROPAY){
            String strPointBeforeTransform = getArguments().getString("PointBeforeTransform");
            String strPointHowMuchTransform = getArguments().getString("PointHowMuchTransform");
            String strPointAfterTransform = getArguments().getString("PointAfterTransform");
        }
    }*/
}
