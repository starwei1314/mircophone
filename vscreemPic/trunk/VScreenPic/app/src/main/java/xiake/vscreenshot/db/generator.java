package xiake.vscreenshot.db;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class generator {
    public static void main(String[] args) {
        int version = 1;
        String defaultPackage = "xiake.db.entity";
        Schema schema = new Schema(version, defaultPackage);
        schema.setDefaultJavaPackageDao("xiake.db.dao");
        //微信
        addUser(schema);//用户
        addSmallMoney(schema);//零钱
        addFriendAbout(schema);//好友相关
        addNewFriend(schema);//新的朋友
        addAllFriend(schema);
        addMailList(schema);
        addChatRecord(schema);
        addData(schema);//所有数据
        //支付宝
        addBill(schema);//支付宝账单
        addAliFriend(schema);//支付宝好友
        String outDir = "./app/src/main/java-gen";

        try {
            new DaoGenerator().generateAll(schema,outDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addAliFriend(Schema schema) {
        Entity aliFriend = schema.addEntity("AliFriend");
        aliFriend.addIdProperty().autoincrement();
        aliFriend.addStringProperty("img");
        aliFriend.addStringProperty("nick");
        aliFriend.addBooleanProperty("isFriend");
        aliFriend.addStringProperty("VipLv");
        aliFriend.addStringProperty("num");
        aliFriend.addStringProperty("mark");
    }

    private static void addBill(Schema schema) {
        Entity bill = schema.addEntity("Bill");
        bill.addIdProperty().autoincrement();
        bill.addStringProperty("time");//交易时间
        bill.addStringProperty("details");//交易详情
    }


    private static void addChatRecord(Schema schema) {
        Entity chatRecord = schema.addEntity("ChatRecord");
        chatRecord.addBooleanProperty("isMe");
        chatRecord.addStringProperty("msg_type");
        chatRecord.addStringProperty("text");
        chatRecord.addStringProperty("time");
        chatRecord.addIntProperty("audio_num");
        chatRecord.addBooleanProperty("isRead");
        chatRecord.addBooleanProperty("isReceive");
        chatRecord.addIntProperty("money");
        chatRecord.addStringProperty("message");
        chatRecord.addStringProperty("time01");
        chatRecord.addStringProperty("time02");
        chatRecord.addStringProperty("msg_img");
    }

    private static void addMailList(Schema schema) {
        Entity mailList = schema.addEntity("MailList");
        mailList.addStringProperty("name");
        mailList.addIntProperty("is_chatlist");
        mailList.addStringProperty("id");
        mailList.addStringProperty("icon");
        mailList.addIntProperty("msg_num");
        mailList.addStringProperty("chat_bg");
        mailList.addStringProperty("com_time");
        mailList.addStringProperty("recent_new");
        mailList.addBooleanProperty("isMute");
        mailList.addStringProperty("chat_record");

    }

    private static void addAllFriend(Schema schema) {
        Entity allFriend = schema.addEntity("AllFriend");
        allFriend.addStringProperty("type");
        allFriend.addStringProperty("mail_list");
    }

    /**
     * 新的朋友
     * @param schema
     */
    private static void addNewFriend(Schema schema) {
        Entity newFriend = schema.addEntity("NewFriend");
        newFriend.addStringProperty("name");
        newFriend.addIntProperty("is_chatlist");
        newFriend.addLongProperty("id");
        newFriend.addStringProperty("icon");
        newFriend.addIntProperty("msg_num");
        newFriend.addStringProperty("chat_bg");
        newFriend.addStringProperty("com_time");
        newFriend.addStringProperty("recent_new");
        newFriend.addBooleanProperty("isMute");
        newFriend.addStringProperty("chat_record");
        newFriend.addStringProperty("request_msg");//
        newFriend.addBooleanProperty("request_state");//
    }

    /**
     * 朋友相关属性
     * @param schema
     */
    private static void addFriendAbout(Schema schema) {
        Entity friendAbout = schema.addEntity("FriendAbout");
        friendAbout.addIdProperty().autoincrement();
        friendAbout.addStringProperty("imgPath");//头像
        friendAbout.addStringProperty("nickName");//昵称
        friendAbout.addBooleanProperty("no_disturb");//免打扰
        friendAbout.addBooleanProperty("star_target");//星标朋友
        friendAbout.addStringProperty("background");//聊天背景图片
        friendAbout.addStringProperty("remark");//备注
        friendAbout.addBooleanProperty("stick");//置顶聊天
        friendAbout.addStringProperty("chatContent");//聊天内容
    }

    /**
     * 零钱
     * @param schema
     */
    private static void addSmallMoney(Schema schema) {
        Entity smallMoney = schema.addEntity("SmallMoney");
        smallMoney.addIdProperty().autoincrement();
        smallMoney.addStringProperty("pay");//充值
        smallMoney.addStringProperty("kiting");//提现
        smallMoney.addStringProperty("payTime");//充值时间
        smallMoney.addStringProperty("kitingTime");//提现时间
        smallMoney.addStringProperty("getkitingTime");//提现到账时间
        smallMoney.addStringProperty("payMoney");//充值金额
        smallMoney.addStringProperty("kitingMoney");//提现金额
        smallMoney.addStringProperty("payBank");//充值银行
        smallMoney.addStringProperty("kitingBank");//提现银行
    }



    /**
     * 用户信息
     * @param schema
     */
    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement();
        user.addStringProperty("chat_bg");//通用聊天背景
        user.addStringProperty("my_nc");//用户名、昵称
        user.addStringProperty("tx");//头像地址
        user.addStringProperty("my_wxh");//微信号
        user.addStringProperty("money");//零钱


    }




    private static void addData(Schema schema){
        Entity data = schema.addEntity("Data");
        data.addStringProperty("my_info");
        data.addStringProperty("new_friend_list");
        data.addStringProperty("allfriend");
    }
}
