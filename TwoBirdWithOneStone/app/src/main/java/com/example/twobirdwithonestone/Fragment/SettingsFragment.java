package com.example.twobirdwithonestone.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.twobirdwithonestone.Activity.MainActivity;
import com.example.twobirdwithonestone.Activity.SettingPopup;

import com.example.twobirdwithonestone.R;
import com.example.twobirdwithonestone.Service.LockScreenService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SettingsFragment extends Fragment {
    private MainActivity mainActivity;
    private final String TAG = "SettingsFragment";
    Boolean boolLockScreen = false;
    private FirebaseFirestore db;
    private String currentUID;
    private Switch switchLockScreen;
    Button question1; Button question2; Button question3; Button question4; Button question5;
    public Boolean getBoolLockScreen() {
        return boolLockScreen;
    }
    public void setBoolLockScreen(Boolean boolLockScreen) {
        this.boolLockScreen = boolLockScreen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        //quest(문의하기) popup
        question1 = (Button) view.findViewById(R.id.setting_question_btn);
        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingPopup.class);
                intent.putExtra("settingText","question");
                startActivity(intent);
            }
        });
        // FAQ popup
        question2 = (Button) view.findViewById(R.id.setting_faq_btn);
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingPopup.class);
                intent.putExtra("settingText","faq");
                startActivity(intent);
            }
        });
        //notice(공지사항) popup
        question3 = (Button) view.findViewById(R.id.setting_notice_btn);
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingPopup.class);
                intent.putExtra("settingText","notice");
                startActivity(intent);
            }
        });
        // developer popup
        question4 = (Button) view.findViewById(R.id.setting_developer_btn);
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingPopup.class);
                intent.putExtra("settingText","developer");
                startActivity(intent);
            }
        });
        // introduce (사용방법) popup
        question5 = (Button) view.findViewById(R.id.setting_introduce_btn);
        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingPopup.class);
                intent.putExtra("settingText","introduce");
                startActivity(intent);
            }
        });

        Button btn_copyright = (Button)view.findViewById(R.id.copyright_btn);
        btn_copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingPopup.class);
                intent.putExtra("settingText","copyright");
                startActivity(intent);
            }
        });

        switchLockScreen = view.findViewById(R.id.switch_lock_screen);
        //User data fetch. DB의 User정보 가져와기
        final TextView tvUserPoint = view.findViewById(R.id.tvUserPoint);
        //final TextView tvUserName = view.findViewById(R.id.tvUserName);

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
                    boolLockScreen = (Boolean) snapshot.get("switchLockScreen");
                    switchLockScreen.setChecked(boolLockScreen);
                    Intent intent = new Intent(mainActivity, LockScreenService.class);
                    intent.putExtra("LockScreen", boolLockScreen);
                    mainActivity.startService(intent);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        //switch
        switchLockScreen = view.findViewById(R.id.switch_lock_screen);
        //초기상태를 결정한다.
        switchLockScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "SwitchLockScreen 클릭!", Toast.LENGTH_LONG).show();
                if (boolLockScreen) {
                    boolLockScreen = false;
                    db.collection("Users").document(currentUID).update("switchLockScreen", false);

                } else {
                    boolLockScreen = true;
                    db.collection("Users").document(currentUID).update("switchLockScreen", true);
                }
                Intent intent = new Intent(getActivity(), LockScreenService.class);
                intent.putExtra("LockScreen", boolLockScreen);
                getActivity().startService(intent);
            }
        });


        //logout button , 2019,9,7 gyu-young
        Button btnLogout = (Button) view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mainActivity =(MainActivity) context;
        }
    }
}
