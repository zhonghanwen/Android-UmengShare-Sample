package com.zhw.umengshare2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhw.share.utils.ShareUtils;

/**
 * Created by zhonghanwen on 2015/09/12.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareUtils.initShareSDK(this);
    }
}
