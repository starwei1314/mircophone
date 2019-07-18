package xiake.vscreenshot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class test {


    /**
     * mail_list : [{"chat_bg":"file:///android_asset/image/picture88.jpg","chat_record":[{"audio_num":0,"isMe":false,"isRead":false,"isReceive":false,"message":"恭喜发财","money":99999,"msg_type":"text","text":"你好","time":"12:00","time01":"2017-09-26 12:05","time02":"2017-09-26 12:08"},{"isMe":true,"msg_type":"text","text":"文本。ggh","audio_num":1,"isRead":false,"time":"14:35","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:35","time02":""},{"isMe":true,"msg_type":"text","text":"Bnn","audio_num":1,"isRead":false,"time":"14:44","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:44","time02":""},{"isMe":true,"msg_type":"text","text":"Bnnj","audio_num":1,"isRead":false,"time":"14:45","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:45","time02":""},{"isMe":true,"msg_type":"text","text":"按摩","audio_num":1,"isRead":false,"time":"14:48","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:48","time02":""}],"com_time":"2017-10-10T06:48:42.126Z","icon":"file:///android_asset/image/picture0.jpg","id":"0","isMute":false,"is_chatlist":1,"msg_num":0,"name":"小V","recent_new":"恭喜发财","time":"12:00"}]
     * type : X
     */

    private String type;
    private List<MailListBean> mail_list;

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

    public static class MailListBean {
        /**
         * chat_bg : file:///android_asset/image/picture88.jpg
         * chat_record : [{"audio_num":0,"isMe":false,"isRead":false,"isReceive":false,"message":"恭喜发财","money":99999,"msg_type":"text","text":"你好","time":"12:00","time01":"2017-09-26 12:05","time02":"2017-09-26 12:08"},{"isMe":true,"msg_type":"text","text":"文本。ggh","audio_num":1,"isRead":false,"time":"14:35","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:35","time02":""},{"isMe":true,"msg_type":"text","text":"Bnn","audio_num":1,"isRead":false,"time":"14:44","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:44","time02":""},{"isMe":true,"msg_type":"text","text":"Bnnj","audio_num":1,"isRead":false,"time":"14:45","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:45","time02":""},{"isMe":true,"msg_type":"text","text":"按摩","audio_num":1,"isRead":false,"time":"14:48","isReceive":false,"money":"","message":"","time01":"2017-10-10 14:48","time02":""}]
         * com_time : 2017-10-10T06:48:42.126Z
         * icon : file:///android_asset/image/picture0.jpg
         * id : 0
         * isMute : false
         * is_chatlist : 1
         * msg_num : 0
         * name : 小V
         * recent_new : 恭喜发财
         * time : 12:00
         */

        private String chat_bg;
        private String com_time;
        private String icon;
        private String id;
        private boolean isMute;
        private int is_chatlist;
        private int msg_num;
        private String name;
        private String recent_new;
        private String time;
        private List<ChatRecordBean> chat_record;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsMute() {
            return isMute;
        }

        public void setIsMute(boolean isMute) {
            this.isMute = isMute;
        }

        public int getIs_chatlist() {
            return is_chatlist;
        }

        public void setIs_chatlist(int is_chatlist) {
            this.is_chatlist = is_chatlist;
        }

        public int getMsg_num() {
            return msg_num;
        }

        public void setMsg_num(int msg_num) {
            this.msg_num = msg_num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRecent_new() {
            return recent_new;
        }

        public void setRecent_new(String recent_new) {
            this.recent_new = recent_new;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ChatRecordBean> getChat_record() {
            return chat_record;
        }

        public void setChat_record(List<ChatRecordBean> chat_record) {
            this.chat_record = chat_record;
        }

        public static class ChatRecordBean {
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
            private int money;
            private String msg_type;
            private String text;
            private String time;
            private String time01;
            private String time02;

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

            public int getMoney() {
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
        }
    }
}
