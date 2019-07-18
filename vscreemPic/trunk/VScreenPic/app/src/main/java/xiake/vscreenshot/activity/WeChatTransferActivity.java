package xiake.vscreenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xiake.vscreenshot.R;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.ToastUtil;

import static xiake.vscreenshot.R.id.transfer_status;

/**
 * Created by Administrator on 2017/9/18 0018.
 * <p>
 * 微信转账
 */
public class WeChatTransferActivity extends BaseActivity {

    private static final String TAG = "WeChatTransferActivity";
    public String mStringExtra;
    public double mMoney;
    public static final int GET_FRIEND = 1;
    public static final int CHOICE_FRIEND = 2;
    private int type = 0;//转账类型
    private int count = 0;
    private boolean visible = false;
    private boolean transfer_Status = false;
    public int mMinusTime = -120;//转账时间比当前时间早2分钟
    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public TextView mGetTime_tx;
    public TextView mTransferTime_tx;
    public LinearLayout mGetMoneyTime;
    public TextView friendName;
    public ImageView friendImg;
    public long transferTime = 0;
    public String mFriendName = "选择角色";
    private boolean img_visible = false;

    @Override
    protected void setTitle() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.title_1);
        linearLayout.findViewById(R.id.weChat_transfer_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView title = (TextView) linearLayout.findViewById(R.id.weChat_transfer_title);
        Intent intent = getIntent();
        mStringExtra = intent.getStringExtra("title");
        if (mStringExtra != null) {
            title.setText(mStringExtra);
        }
    }

    @Override
    protected void initView() {

        final TextView getMoney_tx = (TextView) findViewById(R.id.get_money_tx);
        final TextView kiting = (TextView) findViewById(R.id.kiting);
        final RelativeLayout getMoney_target = (RelativeLayout) findViewById(R.id.get_money_target);
        friendName = ((TextView) findViewById(R.id.get_money_name));
        friendImg = ((ImageView) findViewById(R.id.get_money_img));
        getMoney_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMoney_tx.setTextColor(Color.WHITE);
                getMoney_tx.setBackgroundResource(R.drawable.shape_blue);
                kiting.setTextColor(Color.GRAY);
                kiting.setBackgroundResource(R.drawable.shape8);
                getMoney_target.setVisibility(View.GONE);
                visible = false;
                mFriendName = "";
            }
        });

        kiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMoney_tx.setTextColor(Color.GRAY);
                getMoney_tx.setBackgroundResource(R.drawable.shape8);
                kiting.setTextColor(Color.WHITE);
                kiting.setBackgroundResource(R.drawable.shape_blue);
                getMoney_target.setVisibility(View.VISIBLE);
                visible = true;
            }
        });

        getMoney_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ChoiceFriendActivity.class);
                startActivityForResult(intent,GET_FRIEND);
            }
        });
        /**
         * 到账时间
         */
        mGetMoneyTime = (LinearLayout) findViewById(R.id.get_time);
        mGetTime_tx = (TextView) findViewById(R.id.get_time_tx);
        mGetMoneyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(1, mGetTime_tx);
            }
        });
        LinearLayout transferType = (LinearLayout) findViewById(R.id.transfer_type);
        final TextView transferStatus = (TextView) findViewById(transfer_status);
        transferType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count%2 == 0){
                    transfer_Status = false;
                    transferStatus.setText("未收钱");
                    mGetMoneyTime.setVisibility(View.GONE);
                    mGetTime_tx.setText("0");
                }else {
                    transfer_Status = true;
                    transferStatus.setText("已收钱");
                    mGetMoneyTime.setVisibility(View.VISIBLE);
                    mGetTime_tx.setText(getTime(0));
                }

            }
        });
        /**
         * 转账时间
         */
        LinearLayout transferTime = (LinearLayout) findViewById(R.id.transfer_time);
        mTransferTime_tx = (TextView) findViewById(R.id.transfer_time_tx);
        transferTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(0, mTransferTime_tx);
            }
        });

        mTransferTime_tx.setText(getTime(mMinusTime));
        /**
         * 生成预览
         */
        final EditText moneyCount = (EditText) findViewById(R.id.weChat_money);
        Button button = (Button) findViewById(R.id.getPreView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = moneyCount.getText().toString().trim();
                if (money.equals("")) {
                    ToastUtil.show(activity,"请设置转账金额",1);
                }else {
                    mMoney = Double.parseDouble(money);
                    if (mMoney <= 0) {
                        ToastUtil.show(activity,"转账金额不能小于0",1);
                    } else {
                        if (visible&&!img_visible){
                            ToastUtil.show(activity,"请选择收钱的人",1);
                            return;
                        }
                        if (!img_visible&&mFriendName.equals("选择角色")){
                            mFriendName = "";
                        }
                        String moneyStr = CommonUtil.formatMoney(Double.parseDouble(money));
                        //传递给webView
                        Intent intent = new Intent(activity, TransferPreViewActivity.class);
                        intent.putExtra("title", mStringExtra);//标题
                        intent.putExtra("money", moneyStr);//转账金额
                        intent.putExtra("nickName", mFriendName);//转账对象
                        intent.putExtra("transferTime",mTransferTime_tx.getText().toString());//转账时间
                        intent.putExtra("getMoneyTime",mGetTime_tx.getText().toString());//到账时间
                        startActivity(intent);
                    }
                }

            }
        });

    }

    private void showTimePickerDialog(final int which,final TextView textView) {
        new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String date = getDate(millseconds);
                        if (which==0){
                            try {
                                int visibility = mGetMoneyTime.getVisibility();
                                if (visibility==0){
                                    long time = format.parse(mGetTime_tx.getText().toString()).getTime();
                                    if (millseconds>time){
                                        ToastUtil.show(activity,"转账时间不能大于收钱时间",1);
                                    }else {
                                        textView.setText(date);
                                    }
                                }else {
                                    transferTime = millseconds;
                                    textView.setText(date);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }else if (which==1){
                            try {
                                long time = format.parse(mTransferTime_tx.getText().toString()).getTime();
                                if (time>0&&millseconds<time){
                                    ToastUtil.show(activity,"收钱时间不能小于转账时间",1);
                                }else {
                                    textView.setText(date);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis()-tenYears)
                .setMaxMillseconds(System.currentTimeMillis()+tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build()
                .show(getSupportFragmentManager(),"all");

    }

    private String getDate(long millseconds) {
        Date date = new Date(millseconds);
        String time = this.format.format(date);
        return time;
    }


    private String getTime(int minusTime) {
        long current = System.currentTimeMillis();
        if (transferTime<=current){
            long timeMillis = current +minusTime*1000;
            Date date = new Date(timeMillis);
            String time = format.format(date);
            return time;
        }else {
            Log.e(TAG, "getTime: " + "\n" +"---------");
            Date date = new Date(transferTime + 120000);
            String time = format.format(date);
            return time;
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_we_chat_transfer;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK){
//            Log.e("onActivityResult: " + "\n" ,"------");
            if (requestCode==GET_FRIEND){
                mFriendName = data.getStringExtra("nickName");
                String imgPath = data.getStringExtra("imgPath");
                friendName.setText(mFriendName);
                friendImg.setVisibility(View.VISIBLE);
                img_visible = true;
                BitmapUtil.setImg(activity,friendImg,imgPath);
            }
        }
    }


}
