package com.zhw.umengshare2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.zhw.share.utils.ShareUtils;

public class MainActivity extends BaseActivity {

    private Button mQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        ShareUtils.setShareContent(this, "this is a umeng share title",
                "this is a contnet", "this is smsContnet", "http://www.tgnet.com");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.openShare(MainActivity.this);
            }
        });
        ShareUtils.setOpenShareListener(new ShareUtils.OpenShareCallBackListener() {
            @Override
            public void onStart() {
                Toast.makeText(MainActivity.this, "分享开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                Toast.makeText(MainActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
        mQQ.setTag(SHARE_MEDIA.QZONE);
        mQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IOSTaoBaoDialog iosTaoBaoDialog = new IOSTaoBaoDialog(MainActivity.this);

                iosTaoBaoDialog.setOnDialogItemClick(new IOSTaoBaoDialog.onDialogItemClick() {
                    @Override
                    public void onClick(View view) {
                        SHARE_MEDIA platform = (SHARE_MEDIA) view.getTag();
                        ShareUtils.directShare(MainActivity.this, platform);
                        iosTaoBaoDialog.dismiss();
                    }
                });
                iosTaoBaoDialog.show();
            }
        });
    }
    private Button mButton;

    private void assignViews() {
        mButton = (Button) findViewById(R.id.button);
        mQQ = (Button) findViewById(R.id.qq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
