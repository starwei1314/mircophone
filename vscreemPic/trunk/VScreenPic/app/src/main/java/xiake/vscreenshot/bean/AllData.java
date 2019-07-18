package xiake.vscreenshot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/26 0026.
 */
public class AllData {
    private List<NewFriend> new_friend_list;//新的朋友
    private List<AllFriend> allfriend;//所有朋友
    private MyInfo my_info;//我的信息

    public List<NewFriend> getNew_friend_list() {
        return new_friend_list;
    }

    public void setNew_friend_list(List<NewFriend> new_friend_list) {
        this.new_friend_list = new_friend_list;
    }

    public List<AllFriend> getAllfriend() {
        return allfriend;
    }

    public void setAllfriend(List<AllFriend> allfriend) {
        this.allfriend = allfriend;
    }

    public MyInfo getMy_info() {
        return my_info;
    }

    public void setMy_info(MyInfo my_info) {
        this.my_info = my_info;
    }

    public class MyInfo {
        private String tx;//头像
        private String my_nc;//昵称
        private String my_wxh;//微信号

        public String getTx() {
            return tx;
        }

        public void setTx(String tx) {
            this.tx = tx;
        }

        public String getMy_nc() {
            return my_nc;
        }

        public void setMy_nc(String my_nc) {
            this.my_nc = my_nc;
        }

        public String getMy_wxh() {
            return my_wxh;
        }

        public void setMy_wxh(String my_wxh) {
            this.my_wxh = my_wxh;
        }
    }

    public class NewFriend {
        private String name;//新朋友名称
        private int is_chatlist;//是否在聊天列表中
        private int id;
        private String request_msg;//请求添加好友时发过来的消息
        private String icon;//头像
        private boolean request_state;//是否已接受

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_chatlist() {
            return is_chatlist;
        }

        public void setIs_chatlist(int is_chatlist) {
            this.is_chatlist = is_chatlist;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRequest_msg() {
            return request_msg;
        }

        public void setRequest_msg(String request_msg) {
            this.request_msg = request_msg;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public boolean isRequest_state() {
            return request_state;
        }

        public void setRequest_state(boolean request_state) {
            this.request_state = request_state;
        }
    }

    public class AllFriend {
        private String type;//朋友名称的首字母
        private List<MailList> mail_list;//同一首字母的朋友列表

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<MailList> getMail_list() {
            return mail_list;
        }

        public void setMail_list(List<MailList> mail_list) {
            this.mail_list = mail_list;
        }
    }

    public class MailList {
        private String name;//名称
        private int is_chatlist;//是否在聊天列表中
        private String id;
        private int icon;//头像
        private String chat_bg;//聊天背景
        private String com_time;//聊天日期
        private String time;//聊天时间
        private String recent_new;//最近聊天记录
        private List<RecentList> chat_record;//聊天列表

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_chatlist() {
            return is_chatlist;
        }

        public void setIs_chatlist(int is_chatlist) {
            this.is_chatlist = is_chatlist;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getChat_bg() {
            return chat_bg;
        }

        public void setChat_bg(String chat_bg) {
            this.chat_bg = chat_bg;
        }

        public String getCom_time() {
            return com_time;
        }

        public void setCom_time(String com_time) {
            this.com_time = com_time;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRecent_new() {
            return recent_new;
        }

        public void setRecent_new(String recent_new) {
            this.recent_new = recent_new;
        }

        public List<RecentList> getChat_record() {
            return chat_record;
        }

        public void setChat_record(List<RecentList> chat_record) {
            this.chat_record = chat_record;
        }
    }

    public class RecentList {
        private boolean isMe;//是否是我发的信息
        private String msg_type;//聊天内容类型
        private int num;//未读消息数量
        private String text;//聊天文字
        private String icon;//聊天图片
        private boolean isRead;//是否已读
        private String time;//聊天时间
        private boolean isReceive;//是否接收
        private int money;//转账数额
        private String time01;//转账时间
        private String time02;//收取时间
        private String message;//转账说明

        public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean me) {
            isMe = me;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isReceive() {
            return isReceive;
        }

        public void setReceive(boolean receive) {
            isReceive = receive;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getTime01() {
            return time01;
        }

        public void setTime01(String time01) {
            this.time01 = time01;
        }

        public String getTime02() {
            return time02;
        }

        public void setTime02(String time02) {
            this.time02 = time02;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}






