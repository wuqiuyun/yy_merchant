package com.yiyue.store.util;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by lyj on 2018/11/02.
 * 一切AppID和Key请在ShareSDK.xml中配置
 * ShareSDK分享工具类
 */

public class ShareUtils {
    //QQ好友分享
    public static void shareQQ(String title, String shareurl, String text, String imageUrl, PlatformActionListener listener){
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setText(text);
        // site是分享此内容的网站名称，仅在QQ空间使用
        sp.setSite("QQ空间标题");
        sp.setImageUrl(imageUrl);
        qq.setPlatformActionListener(listener);
        qq.share(sp);
    }
    //微信
    public static void shareWechat(String title,String shareurl,String text,String imageUrl,PlatformActionListener listener){
        Platform weixin =ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareurl);
        sp.setText(text);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }
    //微信朋友圈
    public static void shareWechatMoments(String title,String shareurl,String text,String imageUrl,PlatformActionListener listener){
        Platform weixin =ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareurl);
        sp.setText(text);
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }
}