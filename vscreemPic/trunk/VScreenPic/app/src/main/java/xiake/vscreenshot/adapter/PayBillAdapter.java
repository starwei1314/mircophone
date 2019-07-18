package xiake.vscreenshot.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.BillDetails;
import xiake.vscreenshot.util.BitmapUtil;

import static xiake.vscreenshot.util.Constant.VipLv;
import static xiake.vscreenshot.util.Constant.imglv;


/**
 * Created by Jione on 2017/9/21.
 */
public class PayBillAdapter extends RecyclerViewAdapter<BillDetails>{
    private static final String TAG = "PayBillAdapter";
    private final Activity activity;
    public final String mDay;
    private String preMonth = "";
    private int dayContnt = -1;
    private int yearContnt = -1;
    private int MonthContnt = -1;
    public final Gson mGson;
    SimpleDateFormat format;
    public final String mYear;
    public final String mMonth;

    public PayBillAdapter(Activity activity) {
        this.activity = activity;
        mGson = new Gson();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        String formatTime = this.format.format(date);
        String s = formatTime.split("-")[2];
        mYear = formatTime.split("-")[0];
        mMonth = formatTime.split("-")[1];
        mDay = s.split(" ")[0];
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.alipay_bill_item, null, false);
        PayBillViewHolder payBillViewHolder = new PayBillViewHolder(inflate);
        return payBillViewHolder;
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof PayBillViewHolder){

            BillDetails billDetails = mDataSource.get(position);
            PayBillViewHolder payBillViewHolder = (PayBillViewHolder) holder;
            String type = billDetails.getType();
            payBillViewHolder.buyName.setText(billDetails.getHead());
            payBillViewHolder.buyRmb.setText(type+billDetails.getMoney());
            String time = billDetails.getTime();
            setTime(time,payBillViewHolder);
            payBillViewHolder.buyStatus.setText(billDetails.getStatus());
            String imgPath = billDetails.getImg();
            Log.e(TAG, "onBindViewHolder: " + "\n" +imgPath);
            BitmapUtil.setImg(activity,payBillViewHolder.buyerImg, imgPath);
            payBillViewHolder.buyerLv.setImageResource(setLv(billDetails.getVipLv()));


            String year = time.split("-")[0];//获取年份
            String month = time.split("-")[1];//获取月份
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String localTime = format.format(date);
            Log.e(TAG, "onBindViewHolder: " + "\n" +time+ "\n" +localTime);
            String localYear = localTime.split("-")[0];//获取当前年份
            String localMonth = localTime.split("-")[1];//获取当前月份
            if (!localYear.equals(year)){
                payBillViewHolder.pay_month.setText(year+"年"+month+"月");
            }else if (localYear.equals(year)){
                if (!localMonth.equals(month)){
                    payBillViewHolder.pay_month.setText(month+"月");
                }else {
                    payBillViewHolder.pay_month.setText("本月");
                }

            }
            payBillViewHolder.Time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onClick: " + "\n" +"点击无效");
                }
            });
            if (position==0){
                payBillViewHolder.Time.setVisibility(View.VISIBLE);
            }else {
                BillDetails billPre = mDataSource.get(position-1);
                String timePre = billPre.getTime();
                String monthPre = timePre.split("-")[1];
                BillDetails billNext = mDataSource.get(position);
                String timeNext = billNext.getTime();
                String monthNext = timeNext.split("-")[1];
                if (monthPre.equals(monthNext)){
                    Log.e(TAG, "onBindViewHolder: " + "\n" +monthPre + "\n" +monthNext);
                    payBillViewHolder.Time.setVisibility(View.GONE);
                }else {
                    payBillViewHolder.Time.setVisibility(View.VISIBLE);
                }

            }

        }
        super.onBindViewHolder(holder, position);
    }

    @NonNull
    private void setTime(String time,PayBillViewHolder payBillViewHolder) {
        String substring = time.substring(5);
        String year = time.split("-")[0];
        String month = time.split("-")[1];
        String dayTime = time.split("-")[2];
        String day = dayTime.split(" ")[0];
        String hour = dayTime.split(" ")[1];
        if (mYear.equals(year)){
            if (mMonth.equals(month)){
                if (Integer.parseInt(mDay)==Integer.parseInt(day)){
                    payBillViewHolder.buyTime.setText("今天 "+hour);
                }else if (Integer.parseInt(mDay)==Integer.parseInt(day)+1){
                    payBillViewHolder.buyTime.setText("昨天 "+hour);
                }else {
                    payBillViewHolder.buyTime.setText(substring);
                }
            }else {
                payBillViewHolder.buyTime.setText(substring);
            }
        }else {
            payBillViewHolder.buyTime.setText(time);
        }

    }


    /**
     * 会员等级
     * @param vipLv
     * @return
     */
    private int setLv(String vipLv) {
        for (int i = 0; i < VipLv.length; i++) {
            Log.e(TAG, "setLv: " + "\n" +vipLv+"\n"+VipLv[i]);
            if (vipLv.equals(VipLv[i])){
                return imglv[i];
            }else {
                continue;
            }
        }
        return imglv[0];
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public class PayBillViewHolder extends ClickableViewHolder{
        private final ImageView buyerImg;//买家头像
        private final ImageView buyerLv;//买家会员等级
        private final TextView buyTime;//购买时间
        private final TextView buyRmb;//购买价格
        private final TextView buyStatus;//购买状态
        private final TextView buyName;//购买商品
        private final TextView pay_month; //日期
        private final RelativeLayout Time; //月份

        public PayBillViewHolder(View itemView) {
            super(itemView);
            buyerImg = ((ImageView) getView(R.id.shapeImageView));
            buyerLv = ((ImageView) getView(R.id.shapeImageView2));
            buyTime = ((TextView) getView(R.id.alipay_time));
            buyRmb = ((TextView) getView(R.id.alipay_rmb));
            buyStatus = ((TextView) getView(R.id.alipay_status));
            buyName = ((TextView) getView(R.id.alipay_name));
            pay_month = getView(R.id.pay_month);
            Time = getView(R.id.month_time);

        }
    }
}
