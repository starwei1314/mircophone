package xiake.db.entity;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "USER".
 */
@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;
    private String chat_bg;
    private String my_nc;
    private String tx;
    private String my_wxh;
    private String money;

    @Generated(hash = 586692638)
    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    @Generated(hash = 340621780)
    public User(Long id, String chat_bg, String my_nc, String tx, String my_wxh, String money) {
        this.id = id;
        this.chat_bg = chat_bg;
        this.my_nc = my_nc;
        this.tx = tx;
        this.my_wxh = my_wxh;
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChat_bg() {
        return chat_bg;
    }

    public void setChat_bg(String chat_bg) {
        this.chat_bg = chat_bg;
    }

    public String getMy_nc() {
        return my_nc;
    }

    public void setMy_nc(String my_nc) {
        this.my_nc = my_nc;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getMy_wxh() {
        return my_wxh;
    }

    public void setMy_wxh(String my_wxh) {
        this.my_wxh = my_wxh;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

}
