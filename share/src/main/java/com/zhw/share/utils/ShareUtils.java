package com.zhw.share.utils;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.zhw.share.consts.PlatFormConst;

import java.util.ArrayList;

/**
 * Created by zhonghanwen on 2015/09/11.
 */
public class ShareUtils {

    public static UMSocialService mController = null;
    public static ArrayList<SHARE_MEDIA> mPlatForm = new ArrayList<>();
    public static DirectShareCallBackListener mListener;
    public static OpenShareCallBackListener mOpenShareListener;
    /**
     * 初始化分享平台
     * @param activity
     */
    public static void initShareSDK(Activity activity) {
        // 首先在您的Activity中添加如下成员变量
        final UMSocialService controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController = controller;
        // 配置需要分享的相关平台
        configPlatforms(activity);
    }

    private static UMSocialService getController() {
        if (mController != null){
            return mController;
        }else {
            return UMServiceFactory.getUMSocialService("com.umeng.share");
        }
    }

    /**
     * 根据不同的平台设置不同的分享内容
     * @param activity
     * @param title  分享的标题
     * @param content 分享的内容
     * @param smsContent 短信分享的内容，为空的时候，默认分享内容
     * @param targetUrl 分享的目标地址
     */
    public static void setShareContent(Activity activity,
                                 String title, String content,String smsContent, String targetUrl) {


        UMSocialService mController = getController();
        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
                PlatFormConst.QQAppId, PlatFormConst.QQAppKey);
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent(content);


        //本地图片
       // UMImage localImage = new UMImage(activity, R.drawable.device);
        //图片的url地址
        UMImage urlImage = new UMImage(activity,
                "http://www.umeng.com/images/pic/social/integrated_3.png");
//        本地图片的路径(绝对路径)
        // UMImage resImage = new UMImage(getActivity(), BitmapFactory.decodeFile("/mnt/sdcard/icon.png"));

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(targetUrl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);


        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia
                .setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);


        urlImage.setTargetUrl(targetUrl);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(targetUrl);
        qzone.setTitle(title);
        qzone.setShareMedia(urlImage);
        // qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);

        //QQ
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setShareMedia(urlImage);
        qqShareContent.setTargetUrl(targetUrl);
        mController.setShareMedia(qqShareContent);


        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        if (smsContent != null && smsContent.trim().length() > 0){
            sms.setShareContent(smsContent);
        }else {
            sms.setShareContent(content);
        }
        // sms.setShareImage(urlImage);
        mController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent
                .setShareContent(content);
        sinaContent.setShareImage(urlImage);
        mController.setShareMedia(sinaContent);
    }


    /**
     * 配置分享平台参数</br>
     */
    private static void configPlatforms(Activity activity) {
//        // 添加新浪SSO授权
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        // 添加腾讯微博SSO授权
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//        // 添加人人网SSO授权
//        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(),
//                "201874", "28401c0964f04a72a14c812d6132fcef",
//                "3bf66e42db1e4fa9829b955cc300b737");
//        mController.getConfig().setSsoHandler(renrenSsoHandler);
        // 添加QQ、QZone平台
        addQQQZonePlatform(activity);

        // 添加微信、微信朋友圈平台
        addWXPlatform(activity);

        //短信
        addSMS();

        mPlatForm.add(SHARE_MEDIA.QQ);
        mPlatForm.add(SHARE_MEDIA.WEIXIN);
        mPlatForm.add(SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatForm.add(SHARE_MEDIA.SMS);
    }



    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private static void addQQQZonePlatform(Activity activity) {
        String appId = PlatFormConst.QQAppId;
        String appKey = PlatFormConst.QQAppKey;
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }


    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private static void addWXPlatform(Activity activity) {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = PlatFormConst.WXAppId;
        String appSecret = PlatFormConst.WXAppSecret;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }


    /**
     * 添加短信平台</br>
     */
    private static void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }

    /**
     * openShare : 分享面板 -> 分享编辑页 -> 分享
     */
    public static void openShare(Activity activity){
        UMSocialService controller = getController();
        //这里需要自定义
        controller.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SMS);
        controller.openShare(activity, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
                if (mOpenShareListener != null) {
                    mOpenShareListener.onStart();
                }
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int eCode, SocializeEntity socializeEntity) {
                if (mOpenShareListener != null) {
                    mOpenShareListener.onComplete(share_media, eCode, socializeEntity);
                }
            }
        });
    }

    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
     */
    public static void directShare(final Activity activity, SHARE_MEDIA platform){
        UMSocialService controller = getController();
        controller.getConfig().closeToast();// android关掉toast
        controller.directShare(activity, platform, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
                if (mListener != null) {
                    mListener.onStart();
                }
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                if (mListener != null) {
                    mListener.onComplete(platform, eCode, entity);
                }
            }
        });
    }



    public interface DirectShareCallBackListener{
        void onStart();
        void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity);
    }

    public DirectShareCallBackListener getListener() {
        return mListener;
    }

    public void setListener(DirectShareCallBackListener listener) {
        mListener = listener;
    }

    public interface OpenShareCallBackListener{
        void onStart();
        void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity);
    }

    public static OpenShareCallBackListener getOpenShareListener() {
        return mOpenShareListener;
    }

    public static void setOpenShareListener(OpenShareCallBackListener mOpenShareListener) {
        ShareUtils.mOpenShareListener = mOpenShareListener;
    }
}
