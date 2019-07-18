package xiake.db.entity;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "CHAT_RECORD".
 */
@Entity
public class ChatRecord {
    private Boolean isMe;
    private String msg_type;
    private String text;
    private String time;
    private Integer audio_num;
    private Boolean isRead;
    private Boolean isReceive;
    private Integer money;
    private String message;
    private String time01;
    private String time02;
    private String msg_img;

    @Generated(hash = 1442974643)
    public ChatRecord() {
    }

    @Generated(hash = 1179194313)
    public ChatRecord(Boolean isMe, String msg_type, String text, String time, Integer audio_num, Boolean isRead, Boolean isReceive, Integer money, String message, String time01, String time02, String msg_img) {
        this.isMe = isMe;
        this.msg_type = msg_type;
        this.text = text;
        this.time = time;
        this.audio_num = audio_num;
        this.isRead = isRead;
        this.isReceive = isReceive;
        this.money = money;
        this.message = message;
        this.time01 = time01;
        this.time02 = time02;
        this.msg_img = msg_img;
    }

    public Boolean getIsMe() {
        return isMe;
    }

    public void setIsMe(Boolean isMe) {
        this.isMe = isMe;
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

    public Integer getAudio_num() {
        return audio_num;
    }

    public void setAudio_num(Integer audio_num) {
        this.audio_num = audio_num;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(Boolean isReceive) {
        this.isReceive = isReceive;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getMsg_img() {
        return msg_img;
    }

    public void setMsg_img(String msg_img) {
        this.msg_img = msg_img;
    }

}