package xiake.vscreenshot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/14 0014.
 */

public class BillHtml {
    public String time;
    public List<BillDetails> billDetails;

    @Override
    public String toString() {
        return "BillHtml{" +
                "time='" + time + '\'' +
                ", billDetails=" + billDetails +
                '}';
    }

    public BillHtml(String time, List<BillDetails> billDetails) {
        this.time = time;
        this.billDetails = billDetails;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<BillDetails> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetails> billDetails) {
        this.billDetails = billDetails;
    }
}
