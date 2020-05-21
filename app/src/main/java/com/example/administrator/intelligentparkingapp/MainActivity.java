package com.example.administrator.intelligentparkingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.intelligentparkingapp.fragment.TabMapFragment;
import com.example.administrator.intelligentparkingapp.fragment.TabMeFragment;
import com.example.administrator.intelligentparkingapp.fragment.TabParkFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mRgGroup;
    private FragmentManager fragmentManager;

    private static final String[] FRAGMENT_TAG = {"tab_map","tab_park", "tab_me"};

    private final int show_tab_park = 1;//找停车场
    private final int show_tab_map = 0;//地图
    private final int show_tab_me = 2;//我的
    private int mrIndex = show_tab_map;//默认选中地图

    private int index = -100;// 记录当前的选项
    /**
     * 上一次界面 onSaveInstanceState 之前的tab被选中的状态 key 和 value
     */
    private static final String PRV_SELINDEX = "PREV_SELINDEX";
    private TabParkFragment tabParkFragment;
    private TabMapFragment tabMapFragment;
    private TabMeFragment tabMeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决底部选项按钮被输入文字框顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //显示界面
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        //防止app闪退后fragment重叠
        if (savedInstanceState != null) {
            //读取上一次界面Save的时候tab选中的状态
            mrIndex = savedInstanceState.getInt(PRV_SELINDEX, mrIndex);

            tabParkFragment = (TabParkFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[1]);
            tabMapFragment = (TabMapFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[0]);
            tabMeFragment = (TabMeFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[2]);
        }

        //初始化
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PRV_SELINDEX, mrIndex);
        super.onSaveInstanceState(outState);
    }
    protected void initView() {
        //获得RadioGroup控件
        mRgGroup = (RadioGroup)findViewById(R.id.rg_group);
        //选择设置Fragment
        setTabSelection(show_tab_map);
        //点击事件
        mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_car://导航
                        setTabSelection(show_tab_park);
                        break;
                    case R.id.rb_map://地图
                        setTabSelection(show_tab_map);
                        break;
                    case R.id.rb_me://我的
                        setTabSelection(show_tab_me);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param id 传入的选择的fragment
     */
    private void setTabSelection(int id) {    //根据传入的index参数来设置选中的tab页。
        if (id == index) {
            return;
        }
        index = id;
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case show_tab_park://停车场的fragment
                mRgGroup.check(R.id.rb_car);
                if (tabParkFragment == null) {
                    tabParkFragment = new TabParkFragment();
                    transaction.add(R.id.fl_container, tabParkFragment, FRAGMENT_TAG[index]);
                } else {
                    transaction.show(tabParkFragment);
                }
                transaction.commit();
                break;
            case show_tab_map://地图的fragment
                mRgGroup.check(R.id.rb_map);
                if (tabMapFragment == null) {
                    tabMapFragment = new TabMapFragment();
                    transaction.add(R.id.fl_container, tabMapFragment, FRAGMENT_TAG[index]);
                    //tabMapFragment.getActivity().startActivity(new Intent(Test.this,MainActivity.class));
                } else {
                    transaction.show(tabMapFragment);
                }
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case show_tab_me://我的的fragment
                mRgGroup.check(R.id.rb_me);//我的的fragment
                if (tabMeFragment == null) {
                    tabMeFragment = new TabMeFragment();
                    transaction.add(R.id.fl_container, tabMeFragment, FRAGMENT_TAG[index]);
                } else {
                    transaction.show(tabMeFragment);
                }
                transaction.commit();
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (tabParkFragment != null) {
            transaction.hide(tabParkFragment);
        }
        if (tabMapFragment != null) {
            transaction.hide(tabMapFragment);
        }
        if (tabMeFragment != null) {
            transaction.hide(tabMeFragment);
        }
    }

}
