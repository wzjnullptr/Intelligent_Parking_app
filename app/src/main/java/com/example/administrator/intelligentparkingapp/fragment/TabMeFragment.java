package com.example.administrator.intelligentparkingapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.intelligentparkingapp.CarActivity;
import com.example.administrator.intelligentparkingapp.R;
import com.example.administrator.intelligentparkingapp.appliaction.AppVariables;
import com.example.administrator.intelligentparkingapp.entity.User;
import com.example.administrator.intelligentparkingapp.sign.LoginActivity;
import com.example.administrator.intelligentparkingapp.utils.nav_bar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabMeFragment extends Fragment {
    private ImageView hBack;
    private ImageView hHead;
    private ImageView userLine;
    private TextView userName;
    private TextView userMoney;
    private nav_bar lishi;
    private nav_bar shoucang;
    private nav_bar version;
    private nav_bar about;


    public TabMeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_tab_me, container, false);
        hBack =(ImageView) view.findViewById(R.id.h_back);
        hHead = (ImageView)  view.findViewById(R.id.h_head);
        userLine = (ImageView)  view.findViewById(R.id.user_line);
        userName = (TextView)  view.findViewById(R.id.user_name);
        userMoney = (TextView)  view.findViewById(R.id.user_val);
        lishi = (nav_bar)  view.findViewById(R.id.lishi);
        shoucang = (nav_bar)  view.findViewById(R.id.shoucang);
        version = (nav_bar)  view.findViewById(R.id.version);
        about = (nav_bar) view.findViewById(R.id.about);

        if (AppVariables.map.get("user") != null) {

            userName.setText(((User) AppVariables.map.get("user")).getUname());
            userMoney.setText(((User) AppVariables.map.get("user")).getUmoney().toString());
        } else {
            userName.setText("亲，你还没登录哦");
            userMoney.setText(" ");
            userName.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });
        }


        shoucang.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CarActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
