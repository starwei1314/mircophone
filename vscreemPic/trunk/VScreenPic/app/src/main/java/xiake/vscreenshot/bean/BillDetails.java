package xiake.vscreenshot.bean;

/**
 * Created by Administrator on 2017/10/13 0013.
 */

public class BillDetails {
    public int id;
    public String type;
    public String head;
    public String money;
    public String img;
    public String vipLv;
    public String time;
    public String status;

    public BillDetails(int id,String type, String head, String money, String img, String vipLv, String time, String status) {
        this.id = id;
        this.type = type;
        this.head = head;
        this.money = money;
        this.img = img;
        this.vipLv = vipLv;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVipLv() {
        return vipLv;
    }

    public void setVipLv(String vipLv) {
        this.vipLv = vipLv;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BillDetails{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", head='" + head + '\'' +
                ", money='" + money + '\'' +
                ", img='" + img + '\'' +
                ", vipLv='" + vipLv + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
