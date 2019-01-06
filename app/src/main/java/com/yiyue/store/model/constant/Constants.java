package com.yiyue.store.model.constant;

import com.yiyue.store.BuildConfig;
import com.yiyue.store.R;
import com.yiyue.store.util.HostUtil;

/**
 * 常量
 * <p>
 * Created by zm on 2018/9/27.
 */
public class Constants {
    // 微信appId
    public static final String WEIXIN_APPID = BuildConfig.WEIXIN_APPID;
    // 微信secret
    public static final String WEIXIN_SECRET = BuildConfig.WEIXIN_SECRET;

    // EXTRAS
    public static final String EXTRA_ORDER_TYPE = "order_type";

    //IM相关
    public static final String EXTRA_GROUP_NAME = "groupName";
    public static final String EXTRA_GROUP_ROLE = "groupRole";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
    public static final String ACTION_CONVERSION_COMING = "action_conversion_coming";

    public static final int CHATTYPE_SINGLE = 1;//单聊
    public static final int CHATTYPE_GROUP = 2;//群聊

    public static final String EXTRA_FRIEND_HEAD_PATH = "headiconpath";//好友的头像地址
    public static final String EXTRA_FRIEND_IS_FRIEND = "isfriend";//是否是好友
    public static final String EXTRA_FRIEND_IS_FROM_FRIEND = "isFromFriends";//是否是好友列表进入的好友详情
    public static final String EXTRA_FRIEND_IS_FROM_RECOMMEND_FRIEND = "isFromRecommendFriends";//是否是推荐好友进入的好友详情

    public static final String EXTRA_CHAT_IS_FROM_CONVER = "is_chat_from_conver";//是否会话列表进入的聊天 此时不刷新环信整个会话列表

    public static final String EXTRA_FRIEND_NAME = "chatName";//好友的用户昵称
    public static final String EXTRA_FRIEND_RELATION_ID = "friend_relation_id";//好友关系id
    public static final String EXTRA_CHAT_TYPE = "chatType";//聊天的类型：单聊 群聊
    public static final String EXTRA_FRIEND_ID = "friendId";//好友的用户id
    public static final String EXTRA_SERACH_USERID = "search_userid";//搜索接口中搜到的用户的userId
    public static final String EXTRA_GROUPBEAN_ID = "group_bean_id";//群列表中群本身的id
    public static final String EXTRA_GROUP = "group_bean";//群列表中群本身的bean对象
    public static final String EXTRA_USER_ID = "im_chat_id";//无论是单人还是群聊天页面需要的聊天使用的环信id
    public static final String EXTRA_GROUP_IM_ID = "imgroupId";//im对应的环信中的群组编号也就是群的imid
    public static final String EXTRA_FRIEND_USER_BEAN = "friend_user_bean";//红包 转账页面间传递bean对象
    public static final String EXTRA_RED_ITEM_BEAN = "red_item_friend_bean";//红包 转账 页面间传递 自定义的包装对象的key


    public static final String IM_SELF_BEAN = "self_bean";//im聊天自定义消息传递


    public static final String IM_CHAT_HEAD_PATH = "im_chat_head_path";//im聊天对应的头像
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String FRIEND_LIST_DB = "friend_list_db_";
    public static final String GROUP_LIST_DB = "group_list_db_";
    public static final String NOFRIEND_LIST_DB = "nofriend_list_db_";
    public static final String MESSAGE_ORDER_LIST_DB = "message_order_list_db_";
    public static final String MESSAGE_IM_LIST_DB = "message_im_list_db_";
    public static final String MESSAGE_SYS_LIST_DB = "message_sys_list_db_";

    public static final int REQUEST_CODE_MAP = 101;
    public static final int REQUEST_CODE_CAMERA = 102;
    public static final int REQUEST_CODE_FILE = 103;
    public static final int REQUEST_CODE_VOICE_CALL = 104;
    public static final int REQUEST_CODE_PIC_FROM_PHOTO = 105;

    public static final int EVENT_FRIEND_CHANGE = 1001;//好友列表数据库新增 需要刷新好友列表页面
    public static final int EVENT_GROUP_CHANGE = 1002;//群组列表数据库新增 需要刷新群组列表页面

    public static final String IM_MESSAGE_KEY = "im_message_type";//聊天类型的key
    //    public static final String IM_MESSAGE_ORDER_DETAIL_KEY = "store_order_detail_msg";//订单详情点击咨询的自定义消息类型的key
    public static final String IM_MESSAGE_ORDER_ADDMONEY_KEY = "yiyue_defined_msg";//自定义消息类型的key 例如：订单详情点击加价发送消息
    public static final int IM_MESSAGE_NORMOR_TYPE = 1;//正常聊天的类型
    public static final int IM_MESSAGE_ORDER_DETAIL_TYPE = 2;//订单详情点击咨询的自定义消息类型

//        public static int[] itemNames = {R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location, R.string.attach_recommend, R.string.attach_red_packet, R.string.attach_transfer_accounts, R.string.attach_voice_call, R.string.attach_vodeo_call};
//    public static int[] resouseIds = {R.drawable.comm_camera_nor, R.drawable.comm_album_nor, R.drawable.comm_position_nor, R.drawable.comm_recommend_nor, R.drawable.comm_redenvelopes_nor, R.drawable.comm_transferaccounts_nor, R.drawable.comm_voice_nor, R.drawable.comm_video_nor};
    public static int[] itemNames = {R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location, R.string.attach_recommend, R.string.attach_red_packet, R.string.attach_transfer_accounts};
    public static int[] resouseIds = {R.drawable.comm_camera_nor, R.drawable.comm_album_nor, R.drawable.comm_position_nor, R.drawable.comm_recommend_nor, R.drawable.comm_redenvelopes_nor, R.drawable.comm_transferaccounts_nor};


    //美发师/作品/门店 等公用fragment的来源区别参数  DO NOT USE 0
    public static final int ACTIVITY_JOIN_STYLIST_1 = 1;// 我的美发师(签约)
    public static final int ACTIVITY_JOIN_STYLIST_2 = 2;// 我的美发师(入驻)
    public static final int ACTIVITY_INVITE_STYLIST = 3;// 邀请美发师
    public static final int ACTIVITY_COLLECT = 4;// 我的收藏
    public static final int ACTIVITY_FOOTPRINT = 5;// 我的足迹
    public static final int ACTIVITY_MANY_WORKS = 6;// 这是什么鬼
    public static final int ACTIVITY_INVITE_SEARCH = 7;// 邀请美发师(搜索)

    //门店详情 来源区别
    public static final String STORE_TYPE = "storeType";
    public static final String STORE_ID = "storeId";
    public static final String STYLIST_ID = "stylistId";//美发师ID
    public static final String OPUS_ID = "opusId";
    public static final String HEAD_PORTRAIT = "headPortrait";//头像
    public static final String NICK_NAME = "nickName";// 昵称
    public static final String STYLIST_POSITION = "stylistPosition";// 美发师职级
    public static final String STYLISTId = "stylistId";//美发师ID
    public static final int STORE_MANAGE = 1;//首页门店管理
    public static final int STORE_COLLECT_FOOTPRINT = 2;//我的收藏/我的足迹
    public static final int STORE_STYLIST_DETAIL = 3;//美发师详情

    public static final String RECHARGE_MONEY = "recharge_money";//充值金额
    public static final String WITHDRAW_MONEY = "withdraw_money";//提现金额
    public static final String WECHAT_OPENID = "wechat_openid";//微信openid

    public static final int ACTIVITY_FILTER_STYLIST_1 = 1;//附近搜索
    public static final int ACTIVITY_FILTER_STYLIST_2 = 2;//综合排序
    public static final int ACTIVITY_FILTER_STYLIST_3 = 3;//筛选
    public static final int ACTIVITY_FILTER_STYLIST_4 = 4;//邀请美发师搜索
    public static final int ACTIVITY_FILTER_STYLIST_5 = 5;//我的美发师搜索

    public static String DB_NAME_FRIEND = Constants.FRIEND_LIST_DB;//好友数据库
    public static String DB_NAME_GROUP = Constants.GROUP_LIST_DB;//群组数据库
    public static String DB_NAME_NOFRIEND = Constants.NOFRIEND_LIST_DB;//非好友数据库

    public static String DB_NAME_ORDER = Constants.MESSAGE_ORDER_LIST_DB;//订单交易系统消息数据库
    public static String DB_NAME_IM = Constants.MESSAGE_IM_LIST_DB;//IM互动消息数据库
    public static String DB_NAME_SYS = Constants.MESSAGE_SYS_LIST_DB;//系统通知数据库

    // 提现账户管理
    public static final int CASHACCOUNTZFB = 0;//支付宝
    public static final int CASHACCOUNTBANK = 1;//银行卡

    //网页类
    public static final String WEB_MONEYEXOLAIN = HostUtil.getServerHost() + "/store-api/explain/wallet.html";//钱包说明
    public static final String WEB_RECOMMEND_EXOLAIN = HostUtil.getServerHost() + "/store-api/explain/recommend.html";//推荐说明
    public static final String WEB_SERVICE_DEAL = HostUtil.getServerHost() + "/store-api/explain/storeagreement.html";//门店服务协议
    public static final String WEB_GROUP_SERVICE_EXOLAIN = HostUtil.getServerHost() + "/store-api/explain/group.html";//意约群组服务声明
    public static final String WEB_REDPACKAT_EXOLAIN = HostUtil.getServerHost() + "/store-api/explain/packet.html";//意约红包说明
    public static final String WEB_ENTER_EXOLAIN = HostUtil.getServerHost() + "/store-api/explain/enter.html";//入驻门店说明

    public static final String WEB_STORES_SHOP = HostUtil.getServerHost() + "/store-api/explain/storesShop.html";//商城
    public static final String WEB_WORK_DETAILS = HostUtil.getServerHost() + "/store-api/explain/details.html";//作品详情
    public static final String WEB_HAIRDRESSER_DETAILS = HostUtil.getServerHost() + "/store-api/explain/hairdresser.html";//美发师详情
    public static final String WEB_STORE_DETAILS = HostUtil.getServerHost() + "/store-api/explain/stores.html";//门店详情
    public static final String WEB_REGISTER = HostUtil.getServerHost() + "/store-api/explain/register.html";//注册页面

    //分享附加参数
    public static final String WEB_CODE = "inviteCode=";
    public static final String WEB_OPUS_ID = "opusId=";
    public static final String WEB_STYLIST_ID = "stylistId=";
    public static final String WEB_STORE_ID = "storeId=";
    public static final String WEB_NICKNAME = "nickName=";

    //评论
    public static final String EVALUATION_TYPE = "Evaluation_type";

    public static final int RESULT_REFRESH_CODE = 9000;//返回刷新标识

    // 订单统计
    public static final int ORDER_TODAY = 0; // 今日
    public static final int ORDER_YESTERDAY = 1;//昨日
    public static final int ORDER_RECENT7 = 2;//近7日
    public static final int ORDER_RECENT30 = 3;//近30日

    //转赠
    public static final String GIFT_BACK = "giftback";// 转赠
    public static final int GIFT_BACK_CODE = 1101;// 转赠code

    //通知栏处理
    public static final String NOTIFICATION_DATA = "notification_data";
    public static final String NOTIFICATION_MSG_TYPE = "notification_msgtype";

    //提现
    public static final int BANK_BACK_CODE = 1105;// 提现code

}
