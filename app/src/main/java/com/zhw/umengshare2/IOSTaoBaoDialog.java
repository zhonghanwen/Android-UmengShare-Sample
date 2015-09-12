package com.zhw.umengshare2;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.nineoldandroids.animation.ObjectAnimator;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class IOSTaoBaoDialog extends BottomBaseDialog implements View.OnClickListener {
    private LinearLayout ll_wechat_friend_circle;
    private LinearLayout ll_wechat_friend;
    private LinearLayout ll_qq;
    private LinearLayout ll_sms;

    private onDialogItemClick mOnDialogItemClick;

    public IOSTaoBaoDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public IOSTaoBaoDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        View inflate = View.inflate(context, R.layout.dialog_ios_taobao, null);

        ll_wechat_friend_circle = (LinearLayout) inflate.findViewById(R.id.ll_wechat_friend_circle);
        ll_wechat_friend = (LinearLayout) inflate.findViewById(R.id.ll_wechat_friend);
        ll_qq = (LinearLayout) inflate.findViewById(R.id.ll_qq);
        ll_sms = (LinearLayout) inflate.findViewById(R.id.ll_sms);

        ll_wechat_friend_circle.setTag(SHARE_MEDIA.WEIXIN_CIRCLE);
        ll_wechat_friend.setTag(SHARE_MEDIA.WEIXIN);
        ll_qq.setTag(SHARE_MEDIA.QQ);
        ll_sms.setTag(SHARE_MEDIA.SMS);

        return inflate;
    }

    @Override
    public boolean setUiBeforShow() {
        ll_wechat_friend_circle.setOnClickListener(this);
        ll_wechat_friend.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        ll_sms.setOnClickListener(this);
        ll_wechat_friend_circle.setOnClickListener(this);

        return false;
    }



    private BaseAnimatorSet windowInAs;
    private BaseAnimatorSet windowOutAs;

    @Override
    protected BaseAnimatorSet getWindowInAs() {
        if (windowInAs == null) {
            windowInAs = new WindowsInAs();
        }
        return windowInAs;
    }

    @Override
    protected BaseAnimatorSet getWindowOutAs() {
        if (windowOutAs == null) {
            windowOutAs = new WindowsOutAs();
        }
        return windowOutAs;
    }

    @Override
    public void onClick(View v) {
        if (mOnDialogItemClick != null){
            mOnDialogItemClick.onClick(v);
        }
    }

    class WindowsInAs extends BaseAnimatorSet {
        @Override
        public void setAnimation(View view) {
            ObjectAnimator rotationX = ObjectAnimator.ofFloat(view, "rotationX", 10, 0f).setDuration(150);
            rotationX.setStartDelay(200);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.8f).setDuration(350),
                    ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.8f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).setDuration(350),
                    ObjectAnimator.ofFloat(view, "rotationX", 0f, 10).setDuration(200),
                    rotationX,
                    ObjectAnimator.ofFloat(view, "translationY", 0, -0.1f * dm.heightPixels).setDuration(350)
            );
        }
    }

    class WindowsOutAs extends BaseAnimatorSet {
        @Override
        public void setAnimation(View view) {
            ObjectAnimator rotationX = ObjectAnimator.ofFloat(view, "rotationX", 10, 0f).setDuration(150);
            rotationX.setStartDelay(200);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.0f).setDuration(350),
                    ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.0f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).setDuration(350),
                    ObjectAnimator.ofFloat(view, "rotationX", 0f, 10).setDuration(200),
                    rotationX,
                    ObjectAnimator.ofFloat(view, "translationY", -0.1f * dm.heightPixels, 0).setDuration(350)
            );
        }
    }

    public onDialogItemClick getOnDialogItemClick() {
        return mOnDialogItemClick;
    }

    public void setOnDialogItemClick(onDialogItemClick onDialogItemClick) {
        mOnDialogItemClick = onDialogItemClick;
    }

    public interface onDialogItemClick{
        void onClick(View view);
    }
}
