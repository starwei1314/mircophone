package xiake.db.entity;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "ALL_FRIEND".
 */
@Entity
public class AllFriend {
    private String type;
    private String mail_list;

    @Generated(hash = 59480190)
    public AllFriend() {
    }

    @Generated(hash = 507839113)
    public AllFriend(String type, String mail_list) {
        this.type = type;
        this.mail_list = mail_list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMail_list() {
        return mail_list;
    }

    public void setMail_list(String mail_list) {
        this.mail_list = mail_list;
    }

}