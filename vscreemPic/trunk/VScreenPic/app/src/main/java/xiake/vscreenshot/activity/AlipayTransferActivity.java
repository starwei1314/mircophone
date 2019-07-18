package xiake.vscreenshot.activity;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xiake.vscreenshot.R;
import xiake.vscreenshot.adapter.BankCardAdapter;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.Constant;
import xiake.vscreenshot.util.ToastUtil;
import xiake.vscreenshot.util.pinyin.RandomUtil;

import static xiake.vscreenshot.R.id.alipay_transfer_income;
import static xiake.vscreenshot.util.Constant.strLv;

/**
 * Created by Administrator on 2017/9/18 0018.
 * <p>
 * 支付宝转账
 */
public class AlipayTransferActivity extends BaseActivity implements View.OnClickListener {

    private static final int CHOICE_ALI_FRIEND = 1;
    private static final int ADD_BANK_CARD = 2;
    private static final String TAG = "AlipayTransferActivity";
    @BindView(R.id.alipay_transfer_title_tx)
    TextView mAlipayTransferTitleTx;
    @BindView(R.id.alipay_transfer_back)
    ImageView mAlipayTransferBack;
    @BindView(R.id.alipay_transfer_title)
    TextView mAlipayTransferTitle;
    @BindView(R.id.transfer_choice)
    TextView mTransferChoice;
    @BindView(R.id.transfer_number)
    TextView mTransferNumber;
    @BindView(R.id.transfer_name)
    TextView mTransferName;
    @BindView(R.id.transfer_select)
    RelativeLayout mTransferSelect;
    @BindView(R.id.transfer_choice_rl)
    RelativeLayout mTransferChoiceRl;
    @BindView(R.id.alipay_transfer_expend)
    CheckBox mAlipayTransferExpend;
    @BindView(alipay_transfer_income)
    CheckBox mAlipayTransferIncome;
    @BindView(R.id.alipay_transfer_money)
    EditText mAlipayTransferMoney;
    @BindView(R.id.alipay_transfer_declare)
    EditText mAlipayTransferDeclare;
    @BindView(R.id.alipay_transfer_time)
    TextView mAlipayTransferTime;
    @BindView(R.id.alipay_set_time)
    RelativeLayout mAlipaySetTime;
    @BindView(R.id.alipay_transfer_status)
    TextView mAlipayTransferStatus;
    @BindView(R.id.transfer_order_number)
    TextView mTransferOrderNumber;
    @BindView(R.id.alipay_transfer_order_rl)
    RelativeLayout mAlipayTransferOrderRl;
    @BindView(R.id.alipay_transfer_shop_number)
    Switch mAlipayTransferShopNumber;
    @BindView(R.id.alipay_transfer_record)
    Switch mAlipayTransferRecord;
    @BindView(R.id.alipay_transfer_preview)
    TextView mAlipayTransferPreview;

    @BindView(R.id.alipay_transfer_title_ll)
    LinearLayout mAlipayTransferTitleLl;
    @BindView(R.id.imageView2)
    ImageView mImageView2;
    @BindView(R.id.textView6)
    TextView mTextView6;
    @BindView(R.id.textView8)
    TextView mTextView8;
    @BindView(R.id.alipay_pay_type_rl)
    RelativeLayout mAlipayPayTypeRl;
    @BindView(R.id.pay_bank_card)
    TextView mPayBankCard;
    @BindView(R.id.transfer_lv)
    ImageView mTransferLv;

    public ArrayList<String> mList;
    public boolean isShowNumber;
    public boolean isShowRecord;
    public String payType = "+";//支付类型  默认为收入
    public String payMode = "余额";//支付类型  默认为余额
    List<String> friend = new ArrayList<>();
    public String[] PAY_STATUS = {"交易成功", "等待对方付款", "等待付款"};
    public int clickNum = 0;
    public String payStatus = "交易成功";
    public String orderNum = "";
    public TextView addBankCard;
    public List<String> mBanks;

    @Override
    protected void setTitle() {

    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        orderNum = getOrderNumber();
        ButterKnife.bind(this);
        initData();
        initListener();

    }

    private void initListener() {
        //返回
        mAlipayTransferBack.setOnClickListener(this);
        //选择角色
        mTransferChoiceRl.setOnClickListener(this);
        //收支类型
        //收入
        mAlipayTransferIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                mAlipayTransferExpend.setChecked(!b);
                payType = b ? "+" : "-";
                payStatus = mAlipayTransferStatus.getText().toString();
                if (!payStatus.equals(PAY_STATUS[0])) {
                    mAlipayTransferStatus.setText(b ? PAY_STATUS[1] : PAY_STATUS[2]);
                }
                if (payType.equals("-")) {
                    mAlipayPayTypeRl.setVisibility(View.VISIBLE);
                } else {
                    mAlipayPayTypeRl.setVisibility(View.GONE);
                }
            }
        });
        mAlipayPayTypeRl.setOnClickListener(this);
        //支出
        mAlipayTransferExpend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                mAlipayTransferIncome.setChecked(!b);
            }
        });
        //创建时间
        mAlipaySetTime.setOnClickListener(this);
        //交易状态
        mAlipayTransferStatus.setOnClickListener(this);
        //商户账单号
        mAlipayTransferOrderRl.setOnClickListener(this);
        mAlipayTransferShopNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                isShowNumber = b;
            }
        });
        //来往记录
        mAlipayTransferRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                isShowRecord = b;
            }
        });
        //生成预览
        mAlipayTransferPreview.setOnClickListener(this);
    }

    private void initData() {
        mAlipayTransferIncome.setChecked(true);
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(timeMillis);
        String formatDate = format.format(date);
        mAlipayTransferTime.setText(formatDate);
        mTransferOrderNumber.setText(orderNum);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_alipay_transfer;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.alipay_transfer_preview://生成预览
                preView();
                break;
            case R.id.alipay_transfer_back://返回
                finish();
                break;
            case R.id.transfer_choice_rl://选择角色
                Intent intent1 = new Intent(activity, AliFriends.class);
                startActivityForResult(intent1, CHOICE_ALI_FRIEND);
                break;
            case R.id.alipay_set_time://交易时间
                showTimPickerDialog();
                break;
            case R.id.alipay_transfer_order_rl://订单号
                mTransferOrderNumber.setText(getOrderNumber());
                break;
            case R.id.alipay_transfer_status://交易状态
                setPayStatus();
                break;
            case R.id.alipay_pay_type_rl://付款方式
                setPayType();

                break;
        }

    }

    /**
     * 预览
     */
    private void preView() {
        mList.clear();
        String moneyStr = mAlipayTransferMoney.getText().toString();
        String declareStr = mAlipayTransferDeclare.getText().toString();
        String transferTime = mAlipayTransferTime.getText().toString();
        if (friend.size()==0){
            ToastUtil.show(activity, "请选择角色）", 1);
            return;
        }

        if (moneyStr.equals("") || moneyStr.equals("0")) {
            ToastUtil.show(activity, "请设置转账金额（不能为0）", 1);
            return;
        }
        if (declareStr.equals("")) {
            declareStr = "收款";
        }
        String formatMoney = CommonUtil.formatMoney(Double.parseDouble(moneyStr));
        mList.add(mGson.toJson(friend));
        mList.add(payType);
        mList.add(formatMoney);
        mList.add(declareStr);
        mList.add(transferTime);
        mList.add(payStatus);
        mList.add(orderNum);
        mList.add(payMode);
        mList.add(isShowRecord?"是":"否");
        Log.e(TAG, "preView: " + "\n" + payMode + "\n" + payStatus);
        Intent intent = new Intent(activity, AlipayTransferPreView.class);
        intent.putStringArrayListExtra("list", mList);
        startActivity(intent);
    }

    /**
     * 设置付款状态
     */
    private void setPayStatus() {
        clickNum++;
        if (payType.equals("+")) {
            mAlipayTransferStatus.setText(clickNum % 2 == 0 ? PAY_STATUS[0] : PAY_STATUS[1]);
        } else {
            mAlipayTransferStatus.setText(clickNum % 2 == 0 ? PAY_STATUS[0] : PAY_STATUS[2]);
        }
        payStatus = mAlipayTransferStatus.getText().toString();
        Log.e(TAG, "setPayStatus: " + "\n" +payStatus);
    }

    /**
     * 设置付款方式
     */
    private void setPayType() {
        String bankStr = getSharedPreferences("config", MODE_PRIVATE).getString("BankList", "");
        if (bankStr.equals("")) {
            List<String> banklist = new ArrayList<>();
            banklist.add("余额");
            banklist.add("余额宝");
            mGson = new Gson();
            bankStr = mGson.toJson(banklist);
            getSharedPreferences("config", MODE_PRIVATE).edit().putString("BankList", bankStr).commit();
        }
        final List<String> banklist = mGson.fromJson(bankStr, new TypeToken<List<String>>() {
        }.getType());
        View inflate = LayoutInflater.from(activity).inflate(R.layout.pay_type, null, false);
        addBankCard = ((TextView) inflate.findViewById(R.id.pay_type_add));
        ListView bankCardList = ((ListView) inflate.findViewById(R.id.pay_type_list));
        final PopupWindow popupWindow = new PopupWindow(inflate);
        popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0);
        BankCardAdapter adapter = new BankCardAdapter(activity, banklist);
        bankCardList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BankCardAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position, BankCardAdapter.ViewHolder holder) {
                payMode = banklist.get(position);
                mPayBankCard.setText(payMode);
                popupWindow.dismiss();
            }
        });
        addBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Intent intent2 = new Intent(activity, AddBankCard.class);
                startActivityForResult(intent2, ADD_BANK_CARD);
            }
        });
    }

    /**
     * 获取订单号
     * 前8位为日期，中间12位固定：比如转账是 200040011100
     * 接着4位是角色码（两位数） +00
     * 最后8位不同
     *
     * @return
     */
    private String getOrderNumber() {
        String midNum = "200040011100";
        long timeMillis = System.currentTimeMillis();
        Log.e(TAG, "getOrderNumber: " + "\n" + timeMillis);
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmm");
        String formatTime = format.format(date);
        //获取随机角色码
        int[] ints1 = RandomUtil.randomCommon(0, 99, 1);
        String s1 = "" + ints1[0];

        if (s1.length() == 1) {
            s1 = "0" + s1;
        }
        int[] ints2 = RandomUtil.randomCommon(1000000, 99999999, 1);
        String s2 = "" + ints2[0];
        if (s2.length() == 7) {
            s2 = "0" + s2;
        }
        orderNum = formatTime.split(" ")[0] + midNum + s1 + "00" + s2;
        return orderNum;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CHOICE_ALI_FRIEND) {
                friend.clear();
                String nickName = data.getStringExtra("nickName");
                String imgPath = data.getStringExtra("imgPath");
                String vipLv = data.getStringExtra("vipLv");
                Log.e(TAG, "onActivityResult: " + "\n" +vipLv);
                String num = data.getStringExtra("num");
                friend.add(nickName);
                friend.add(imgPath);
                friend.add(num);
                mTransferSelect.setVisibility(View.VISIBLE);
                mTransferName.setText(nickName);
                BitmapUtil.setImg(activity,mImageView2,imgPath);
                mTransferNumber.setText(num);
                for (int i = 0; i < strLv.length; i++) {
                    if (vipLv.equals(Constant.strLv[i])){
                        mTransferLv.setImageResource(Constant.imglv[i]);
                        friend.add(Constant.VipLv[i]);
                    }
                }

            } else if (requestCode == ADD_BANK_CARD) {
                payMode = data.getStringExtra("bankCard");
                mPayBankCard.setText(payMode);
            }
        }

    }

    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private void showTimPickerDialog() {
        new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

                        Date date = new Date(millseconds);
                        String time = format.format(date);
                        mAlipayTransferTime.setText(time);

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
                .setMinMillseconds(System.currentTimeMillis() - tenYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build()
                .show(getSupportFragmentManager(), "all");
    }

}
