package xiake.db.entity;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "LAST_CHAT".
 */
@Entity
public class LastChat {

    @Id(autoincrement = true)
    private Long id;
    private String lastChatTarget;
    private String lastChatImgPath;
    private String lastChatTime;
    private String lastChatContent;

    @Generated(hash = 1083215563)
    public LastChat() {
    }

    public LastChat(Long id) {
        this.id = id;
    }

    @Generated(hash = 910598696)
    public LastChat(Long id, String lastChatTarget, String lastChatImgPath, String lastChatTime, String lastChatContent) {
        this.id = id;
        this.lastChatTarget = lastChatTarget;
        this.lastChatImgPath = lastChatImgPath;
        this.lastChatTime = lastChatTime;
        this.lastChatContent = lastChatContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastChatTarget() {
        return lastChatTarget;
    }

    public void setLastChatTarget(String lastChatTarget) {
        this.lastChatTarget = lastChatTarget;
    }

    public String getLastChatImgPath() {
        return lastChatImgPath;
    }

    public void setLastChatImgPath(String lastChatImgPath) {
        this.lastChatImgPath = lastChatImgPath;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public String getLastChatContent() {
        return lastChatContent;
    }

    public void setLastChatContent(String lastChatContent) {
        this.lastChatContent = lastChatContent;
    }

}