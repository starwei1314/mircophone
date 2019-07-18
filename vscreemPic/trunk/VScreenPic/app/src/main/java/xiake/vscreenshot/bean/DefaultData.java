package xiake.vscreenshot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class DefaultData {


    /**
     * new_friend_list : [{"request_msg":"你好","request_state":false,"name":"畅爷","is_chatlist":1,"id":1,"icon":"./img/icon.png","msg_num":999,"chat_bg":"","com_time":"","chat_record":[],"isMute":false}]
     * allfriend : [{"type":"C","mail_list":[{"name":"畅爷","is_chatlist":1,"id":1,"icon":"./img/icon.png","msg_num":999,"chat_bg":"","com_time":"","chat_record":[{"audio_num":0,"isMe":false,"isRead":false,"isReceive":false,"message":"恭喜发财","money":99999,"msg_type":"text","text":"你好","time":"12:00","time01":"2017-09-26 12:05","time02":"2017-09-26 12:08"},{"isMe":true,"msg_type":"text","text":"有用","audio_num":1,"isRead":false,"time":"15:16","isReceive":false,"money":"","message":"","time01":"2017-10-10 15:16","time02":""}],"isMute":false}]}]
     * my_info : {"tx":"./img/icon.png","my_nc":"LZF","small_change":0,"my_wxh":"liuzhenfeng2010","chat_bg":""}
     */

    private MyInfoBean my_info;
    private List<NewFriendListBean> new_friend_list;
    private List<AllfriendBean> allfriend;

    public DefaultData(MyInfoBean my_info, List<NewFriendListBean> new_friend_list, List<AllfriendBean> allfriend) {
        this.my_info = my_info;
        this.new_friend_list = new_friend_list;
        this.allfriend = allfriend;
    }

    public MyInfoBean getMy_info() {
        return my_info;
    }

    public void setMy_info(MyInfoBean my_info) {
        this.my_info = my_info;
    }

    public List<NewFriendListBean> getNew_friend_list() {
        return new_friend_list;
    }

    public void setNew_friend_list(List<NewFriendListBean> new_friend_list) {
        this.new_friend_list = new_friend_list;
    }

    public List<AllfriendBean> getAllfriend() {
        return allfriend;
    }

    public void setAllfriend(List<AllfriendBean> allfriend) {
        this.allfriend = allfriend;
    }

    @Override
    public String toString() {
        return "DefaultData{" +
                "my_info=" + my_info +
                ", new_friend_list=" + new_friend_list +
                ", allfriend=" + allfriend +
                '}';
    }

    public static class MyInfoBean {
        /**
         * tx : ./img/icon.png
         * my_nc : LZF
         * small_change : 0
         * my_wxh : liuzhenfeng2010
         * chat_bg :
         */

        private String tx;
        private String my_nc;
        private int small_change;
        private String my_wxh;
        private String chat_bg;

        public MyInfoBean(String tx, String my_nc, int small_change, String my_wxh, String chat_bg) {
            this.tx = tx;
            this.my_nc = my_nc;
            this.small_change = small_change;
            this.my_wxh = my_wxh;
            this.chat_bg = chat_bg;
        }

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

        public int getSmall_change() {
            return small_change;
        }

        public void setSmall_change(int small_change) {
            this.small_change = small_change;
        }

        public String getMy_wxh() {
            return my_wxh;
        }

        public void setMy_wxh(String my_wxh) {
            this.my_wxh = my_wxh;
        }

        public String getChat_bg() {
            return chat_bg;
        }

        public void setChat_bg(String chat_bg) {
            this.chat_bg = chat_bg;
        }

        @Override
        public String toString() {
            return "MyInfoBean{" +
                    "tx='" + tx + '\'' +
                    ", my_nc='" + my_nc + '\'' +
                    ", small_change=" + small_change +
                    ", my_wxh='" + my_wxh + '\'' +
                    ", chat_bg='" + chat_bg + '\'' +
                    '}';
        }
    }

    public static class NewFriendListBean {
        /**
         * request_msg : 你好
         * request_state : false
         * name : 畅爷
         * is_chatlist : 1
         * id : 1
         * icon : ./img/icon.png
         * msg_num : 999
         * chat_bg :
         * com_time :
         * chat_record : []
         * isMute : false
         */

        private String request_msg;
        private boolean request_state;
        private String name;
        private int is_chatlist;
        private int id;
        private String icon;
        private long msg_num;
        private String chat_bg;
        private String com_time;
        private boolean isMute;
        private List<ChatRecordBean> chat_record;

        public NewFriendListBean(String request_msg, boolean request_state, String name, int is_chatlist, int id, String icon, long msg_num, String chat_bg, String com_time, boolean isMute, List<ChatRecordBean> chat_record) {
            this.request_msg = request_msg;
            this.request_state = request_state;
            this.name = name;
            this.is_chatlist = is_chatlist;
            this.id = id;
            this.icon = icon;
            this.msg_num = msg_num;
            this.chat_bg = chat_bg;
            this.com_time = com_time;
            this.isMute = isMute;
            this.chat_record = chat_record;
        }

        public String getRequest_msg() {
            return request_msg;
        }

        public void setRequest_msg(String request_msg) {
            this.request_msg = request_msg;
        }

        public boolean getRequest_state() {
            return request_state;
        }

        public void setRequest_state(boolean request_state) {
            this.request_state = request_state;
        }

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public long getMsg_num() {
            return msg_num;
        }

        public void setMsg_num(int msg_num) {
            this.msg_num = msg_num;
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

        public boolean isIsMute() {
            return isMute;
        }

        public void setIsMute(boolean isMute) {
            this.isMute = isMute;
        }

        public List<ChatRecordBean> getChat_record() {
            return chat_record;
        }

        public void setChat_record(List<ChatRecordBean> chat_record) {
            this.chat_record = chat_record;
        }

        @Override
        public String toString() {
            return "NewFriendListBean{" +
                    "request_msg='" + request_msg + '\'' +
                    ", request_state=" + request_state +
                    ", name='" + name + '\'' +
                    ", is_chatlist=" + is_chatlist +
                    ", id=" + id +
                    ", icon='" + icon + '\'' +
                    ", msg_num=" + msg_num +
                    ", chat_bg='" + chat_bg + '\'' +
                    ", com_time='" + com_time + '\'' +
                    ", isMute=" + isMute +
                    ", chat_record=" + chat_record +
                    '}';
        }
    }

    public static class AllfriendBean {
        /**
         * type : C
         * mail_list : [{"name":"畅爷","is_chatlist":1,"id":1,"icon":"./img/icon.png","msg_num":999,"chat_bg":"","com_time":"","chat_record":[{"audio_num":0,"isMe":false,"isRead":false,"isReceive":false,"message":"恭喜发财","money":99999,"msg_type":"text","text":"你好","time":"12:00","time01":"2017-09-26 12:05","time02":"2017-09-26 12:08"},{"isMe":true,"msg_type":"text","text":"有用","audio_num":1,"isRead":false,"time":"15:16","isReceive":false,"money":"","message":"","time01":"2017-10-10 15:16","time02":""}],"isMute":false}]
         */

        private String type;
        private List<MailListBean> mail_list;

        public AllfriendBean(String type, List<MailListBean> mail_list) {
            this.type = type;
            this.mail_list = mail_list;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<MailListBean> getMail_list() {
            return mail_list;
        }

        public void setMail_list(List<MailListBean> mail_list) {
            this.mail_list = mail_list;
        }

        @Override
        public String toString() {
            return "AllfriendBean{" +
                    "type='" + type + '\'' +
                    ", mail_list=" + mail_list +
                    '}';
        }


    }
    @SuppressWarnings("serial")
    public static class MailListBean implements Serializable{
        /**
         * name : 畅爷
         * is_chatlist : 1
         * id : 1
         * icon : ./img/icon.png
         * msg_num : 999
         * chat_bg :
         * com_time :
         * chat_record : [{"audio_num":0,"isMe":false,"isRead":false,"isReceive":false,"message":"恭喜发财","money":99999,"msg_type":"text","text":"你好","time":"12:00","time01":"2017-09-26 12:05","time02":"2017-09-26 12:08"},{"isMe":true,"msg_type":"text","text":"有用","audio_num":1,"isRead":false,"time":"15:16","isReceive":false,"money":"","message":"","time01":"2017-10-10 15:16","time02":""}]
         * isMute : false
         */

        private String name;
        private int is_chatlist;
        private int id;
        private String icon;
        private long msg_num;
        private String chat_bg;
        private String com_time;
        private boolean isMute;
        private List<ChatRecordBean> chat_record;

        public MailListBean(String name, int is_chatlist, int id, String icon, long msg_num, String chat_bg, String com_time, boolean isMute, List<ChatRecordBean> chat_record) {
            this.name = name;
            this.is_chatlist = is_chatlist;
            this.id = id;
            this.icon = icon;
            this.msg_num = msg_num;
            this.chat_bg = chat_bg;
            this.com_time = com_time;
            this.isMute = isMute;
            this.chat_record = chat_record;
        }

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public long getMsg_num() {
            return msg_num;
        }

        public void setMsg_num(int msg_num) {
            this.msg_num = msg_num;
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

        public boolean isIsMute() {
            return isMute;
        }

        public void setIsMute(boolean isMute) {
            this.isMute = isMute;
        }

        public List<ChatRecordBean> getChat_record() {
            return chat_record;
        }

        public void setChat_record(List<ChatRecordBean> chat_record) {
            this.chat_record = chat_record;
        }

        @Override
        public String toString() {
            return "MailListBean{" +
                    "name='" + name + '\'' +
                    ", is_chatlist=" + is_chatlist +
                    ", id=" + id +
                    ", icon='" + icon + '\'' +
                    ", msg_num=" + msg_num +
                    ", chat_bg='" + chat_bg + '\'' +
                    ", com_time='" + com_time + '\'' +
                    ", isMute=" + isMute +
                    ", chat_record=" + chat_record +
                    '}';
        }


    }
    @SuppressWarnings("serial")
    public static class ChatRecordBean implements Serializable{
        /**
         * audio_num : 0
         * isMe : false
         * isRead : false
         * isReceive : false
         * message : 恭喜发财
         * money : 99999
         * msg_type : text
         * text : 你好
         * time : 12:00
         * time01 : 2017-09-26 12:05
         * time02 : 2017-09-26 12:08
         */

        private int audio_num;
        private boolean isMe;
        private boolean isRead;
        private boolean isReceive;
        private String message;
        private long money;
        private String msg_type;
        private String text;
        private String time;
        private String time01;
        private String time02;

        public ChatRecordBean(int audio_num, boolean isMe, boolean isRead, boolean isReceive,
                              String message, long money, String msg_type, String text, String time, String time01, String time02) {
            this.audio_num = audio_num;
            this.isMe = isMe;
            this.isRead = isRead;
            this.isReceive = isReceive;
            this.message = message;
            this.money = money;
            this.msg_type = msg_type;
            this.text = text;
            this.time = time;
            this.time01 = time01;
            this.time02 = time02;
        }

        public int getAudio_num() {
            return audio_num;
        }

        public void setAudio_num(int audio_num) {
            this.audio_num = audio_num;
        }

        public boolean isIsMe() {
            return isMe;
        }

        public void setIsMe(boolean isMe) {
            this.isMe = isMe;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public boolean isIsReceive() {
            return isReceive;
        }

        public void setIsReceive(boolean isReceive) {
            this.isReceive = isReceive;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        @Override
        public String toString() {
            return "ChatRecordBean{" +
                    "audio_num=" + audio_num +
                    ", isMe=" + isMe +
                    ", isRead=" + isRead +
                    ", isReceive=" + isReceive +
                    ", message='" + message + '\'' +
                    ", money=" + money +
                    ", msg_type='" + msg_type + '\'' +
                    ", text='" + text + '\'' +
                    ", time='" + time + '\'' +
                    ", time01='" + time01 + '\'' +
                    ", time02='" + time02 + '\'' +
                    '}';
        }
    }
}
