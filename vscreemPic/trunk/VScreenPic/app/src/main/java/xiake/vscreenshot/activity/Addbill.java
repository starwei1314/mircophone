package xiake.vscreenshot.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import xiake.db.dao.BillDao;
import xiake.db.entity.Bill;
import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.BillDetails;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.ToastUtil;

import static xiake.vscreenshot.util.Constant.VipLv;
import static xiake.vscreenshot.util.Constant.strLv;

/**
 * 添加支付宝账单
 */
public class Addbill extends BaseActivity {

    private static final String TAG = "Addbill";
    private Context mContext = Addbill.this;
    private String IncomeAndExpenses;
    private String headportrait;
    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @BindView(R.id.addbill_change)
    Button mAddbillChange;
    @BindView(R.id.addbill_delete)
    Button mAddbillDelete;
    @BindView(R.id.bill_change)
    LinearLayout mBillChange;
    @BindView(R.id.weChat_transfer_back)
    ImageView weChatTransferBack;
    @BindView(R.id.stateText)
    TextView stateText;
    @BindView(R.id.settimes)
    TextView settimes;
    @BindView(R.id.setImg)
    ImageView setImg;
    @BindView(R.id.income)
    CheckBox income;
    @BindView(R.id.expend)
    CheckBox expend;
    @BindView(R.id.Incomeandexpenditure)
    RelativeLayout Incomeandexpenditure;
    @BindView(R.id.headline)
    EditText headline;
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.portrait)
    TextView portrait;
    @BindView(R.id.imageView)
    RelativeLayout imageView;
    @BindView(R.id.Vipclass)
    RelativeLayout Vipclass;
    @BindView(R.id.transaction)
    RelativeLayout transaction;
    @BindView(R.id.Addtransactionbills)
    Button Addtransactionbills;
    @BindView(R.id.tradingTiime)
    RelativeLayout tradingTiime;

    @BindView(R.id.Viplv)
    TextView Viplv;
    public String path = "";
    public String VipLvStr = "lv0";
    public int clickCount;
    public BillDao mBillDao;
    public List<Bill> mBills;
    public List<BillDetails> mBillDetailList = new ArrayList<>();
    public BillDetails mBillDetails;
    public String[] transferStatus = {"交易成功", "等待对方付款", "等待付款"};
    private static final int CHOOSE_PICTURE = 0;
    private static final int CROP_SMALL_PICTURE = 1;

    private void initListener() {
        weChatTransferBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //选择头像
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Addbill.this)
                        .setTitle("请选择!")
                        .setItems(new String[]{"随机头像", "相册选择"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        Avatar();//随机图片
                                        break;
                                    case 1:
                                        getPhoto();//相册图片
                                        break;

                                }
                            }
                        })
                        .show();


            }
        });
        //收支类型  收入
        income.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                expend.setChecked(!b);
                IncomeAndExpenses = b ? "+" : "-";
                String status = stateText.getText().toString();
                if (!status.equals(transferStatus[0])) {
                    stateText.setText(IncomeAndExpenses.equals("+") ? transferStatus[1] : transferStatus[2]);
                }
            }
        });
        expend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                income.setChecked(!b);
            }
        });

        //会员等级
        Vipclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Addbill.this)
                        .setTitle("请选择等级")
                        .setItems(strLv, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Viplv.setText(strLv[i]);
                                VipLvStr = VipLv[i];
                            }
                        })
                        .show();
            }
        });

        //交易状态
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (IncomeAndExpenses.equals("+")) {
                    stateText.setText(clickCount % 2 == 0 ? transferStatus[0] : transferStatus[1]);
                } else {
                    stateText.setText(clickCount % 2 == 0 ? transferStatus[0] : transferStatus[2]);
                }
            }
        });

        //交易时间
        tradingTiime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimPickerDialog();
            }
        });

        //添加账单
        Addtransactionbills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBill();
            }
        });

        //修改账单
        mAddbillChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBill();
            }
        });
        //删除账单
        mAddbillDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog
                        .Builder(activity)
                        .setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                deleteBill();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });
    }

    /**
     * 修改账单
     */
    private void changeBill() {
        for (Bill bill : mBills) {
            List<BillDetails> details = mGson.fromJson(bill.getDetails(), new TypeToken<List<BillDetails>>() {
            }.getType());
            for (int i = 0; i < details.size(); i++) {
                BillDetails detail = details.get(i);
                if (mBillDetails.getId() == detail.getId()) {
                    //IncomeAndExpenses;
                    String headlines = headline.getText().toString();
                    String moneys = money.getText().toString();
                    if (moneys == null || moneys.equals("")) {
                        ToastUtil.show(activity, "请设置金额", 1);
                        return;
                    } else if (moneys.equals("0")) {
                        ToastUtil.show(activity, "金额不能为0", 1);
                        return;
                    } else if (headlines == null || headlines.equals("")) {
                        ToastUtil.show(mContext, "请输入标题", 1);
                        return;
                    }
                    String money = CommonUtil.formatMoney(Double.parseDouble(moneys));
                    //交易时间
                    String dealTime = settimes.getText().toString();
                    //交易状态
                    String stateTexts = stateText.getText().toString();
                    BillDetails billDetails = new BillDetails(mBillDetails.getId(), IncomeAndExpenses, headlines, money, path, VipLvStr, dealTime, stateTexts);
                    Log.e(TAG, "changeBill: " + "\n" +VipLvStr);
                    details.set(i, billDetails);
                    Log.e(TAG, "onClick: " + "\n" + details);
                    bill.setDetails(mGson.toJson(details));
                    mBillDao.insertOrReplace(bill);
                    ToastUtil.show(activity, "修改成功", 1);
                    finish();
                }
            }
        }
    }

    /**
     * 添加账单
     */
    private void addBill() {
        //IncomeAndExpenses;
        String headlines = headline.getText().toString();
        String moneys = money.getText().toString();
        //交易时间
        String dealTime = settimes.getText().toString();
        //交易状态
        String stateTexts = stateText.getText().toString();

        if (IncomeAndExpenses == null || IncomeAndExpenses.length() < 0 || IncomeAndExpenses.isEmpty()) {
            ToastUtil.show(mContext, "请选择收支类型", 1);
            return;
        } else if (headlines == null || headlines.length() < 0 || headlines.isEmpty()) {
            ToastUtil.show(mContext, "请输入标题", 1);
            return;
        } else if (moneys == null || moneys.length() < 0 || moneys.isEmpty()) {
            ToastUtil.show(mContext, "请设置转账金额", 1);
            return;
        } else if (headportrait == null || headportrait.length() < 0 || headportrait.isEmpty()) {
            ToastUtil.show(mContext, "请设置头像", 1);
            return;
        } else {
            //更新数据库中的账单列表
            String money = CommonUtil.formatMoney(Double.parseDouble(moneys));
            BillDetails billDetails = new BillDetails(mBillDetailList.size() + 1, IncomeAndExpenses, headlines, money, path, VipLvStr, dealTime, stateTexts);
            String time = dealTime.substring(0, dealTime.lastIndexOf("-") + 1);
            if (mBills.size() == 0) {
                List<BillDetails> newBills = new ArrayList<BillDetails>();
                newBills.add(billDetails);
                String json = mGson.toJson(newBills);
                Bill newBill = new Bill();
                newBill.setTime(dealTime);
                newBill.setDetails(json);
                mBillDao.insertOrReplace(newBill);
            } else {
                Map<String, Bill> map = new HashMap<String, Bill>();
                for (int i = 0; i < mBills.size(); i++) {
                    Bill bill = mBills.get(i);
                    String billTime = bill.getTime();
                    String subTime = billTime.substring(0, billTime.lastIndexOf("-") + 1);
                    Log.e(TAG, "onClick: --------" + "\n" + time + "\n" + subTime);
                    map.put(subTime, bill);
                }
                Set<String> subTimes = map.keySet();
                if (subTimes.contains(time)) {
                    Bill bill = map.get(time);
                    List<BillDetails> billDetailses = mGson.fromJson(bill.getDetails(), new TypeToken<List<BillDetails>>() {
                    }.getType());
                    billDetailses.add(billDetails);
                    bill.setDetails(mGson.toJson(billDetailses));
                    mBillDao.insertOrReplace(bill);
                } else {
                    Bill bill = new Bill();
                    bill.setTime(dealTime);
                    List<BillDetails> billDetailses = new ArrayList<>();
                    billDetailses.add(billDetails);
                    bill.setDetails(mGson.toJson(billDetailses));
                    mBillDao.insertOrReplace(bill);
                }
                sortDetail();
            }

//            Log.e(TAG, "onClick: " + "\n" + mGson.toJson(mBillDao.loadAll()));
            finish();
        }
    }

    private void sortDetail() {
        List<Bill> bills = mBillDao.loadAll();
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            List<BillDetails> billDetailsList = mGson.fromJson(bill.getDetails(), new TypeToken<List<BillDetails>>() {
            }.getType());
            if (billDetailsList.size() > 1) {
                for (int j = 0; j < billDetailsList.size() - 1; j++) {
                    for (int k = 0; k < billDetailsList.size() - j - 1; k++) {
                        String time1 = billDetailsList.get(k).getTime();
                        String time2 = billDetailsList.get(k + 1).getTime();
                        if (formatTime(time1) < formatTime(time2)) {
                            BillDetails player;
                            player = billDetailsList.get(k);
                            billDetailsList.set(k, billDetailsList.get(k + 1));
                            billDetailsList.set(k + 1, player);
                        }
                    }
                }
            }
            bill.setDetails(mGson.toJson(billDetailsList));
            bills.set(i, bill);
            mBillDao.insertOrReplace(bill);
        }
    }

    private long formatTime(String time) {
        long timeMillis = 0;
        try {
            timeMillis = format.parse(time).getTime();

        } catch (ParseException e) {
            Log.e(TAG, "sortList: " + "\n" + e.toString());
        }
        return timeMillis;
    }

    private void showTimPickerDialog() {
        new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

                        Date date = new Date(millseconds);
                        String time = format.format(date);
                        settimes.setText(time);

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

    /**
     * 删除账单
     */
    private void deleteBill() {
        for (Bill bill : mBills) {
            List<BillDetails> details = mGson.fromJson(bill.getDetails(), new TypeToken<List<BillDetails>>() {
            }.getType());
            for (int i = 0; i < details.size(); i++) {
                BillDetails detail = details.get(i);
                if (mBillDetails.getId() == detail.getId()) {
                    details.remove(detail);
                    Log.e(TAG, "onClick: " + "\n" + details);
                    bill.setDetails(mGson.toJson(details));
                    mBillDao.insertOrReplace(bill);
                    ToastUtil.show(activity, "删除成功", 1);
                    finish();
                }
            }
        }
    }


    /**
     * 相册图片
     */
    private void getPhoto() {
        //获取相册中的图片
        startActivityForResult(CommonUtil.getImg(), CHOOSE_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    Intent intent = BitmapUtil.cutImage(uri); // 对图片进行裁剪处理
                    startActivityForResult(intent, CROP_SMALL_PICTURE);
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    private void setImageToView(Intent data) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(BitmapUtil.cutImgUri));
            path = BitmapUtil.saveFile(activity, "crop", bitmap);
            BitmapUtil.setImg(activity,setImg,path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void setTitle() {
        mBillDao = mDaoSession.getBillDao();
        mBills = mBillDao.loadAll();
        Log.e(TAG, "setTitle: " + "\n" + mBills.size());
        if (mBills.size() != 0) {
            for (Bill bill : mBills) {
                List<BillDetails> billDetailses = mGson.fromJson(bill.getDetails(), new TypeToken<List<BillDetails>>() {
                }.getType());
                for (BillDetails billDetailse : billDetailses) {
                    mBillDetailList.add(billDetailse);
                }
            }
        }

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        initData();
        initListener();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_addbill;
    }

    /**
     * 随机头像
     */
    private void Avatar() {
        try {
            AssetManager assets = getAssets();
            int s = (int) (Math.random() * CommonUtil.randomNum);
            headportrait = "picture" + String.valueOf(s) + ".jpg";
            path = CommonUtil.defaultPath + headportrait;
            Log.e("=====", headportrait);
            InputStream open = assets.open("image/" + headportrait);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
            setImg.setImageBitmap(bitmap);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        String billDetailJson = intent.getStringExtra("billDetailJson");
        if (billDetailJson != null && billDetailJson != "") {
            mBillChange.setVisibility(View.VISIBLE);
            Addtransactionbills.setVisibility(View.GONE);
            mBillDetails = mGson.fromJson(billDetailJson, BillDetails.class);
            String vip = mBillDetails.getVipLv();
            for (int i = 0; i < VipLv.length; i++) {
                if (vip.equals(VipLv[i])) {
                    Viplv.setText(strLv[i]);
                    VipLvStr = VipLv[i];
                }
            }
            headline.setText(mBillDetails.getHead());
            money.setText(mBillDetails.getMoney());
            settimes.setText(mBillDetails.getTime());
            path = mBillDetails.getImg();
            headportrait = path.substring(path.lastIndexOf("/") + 1);
            BitmapUtil.setImg(activity, setImg, path);
            stateText.setText(mBillDetails.getStatus());
            String type = mBillDetails.getType();
            if (type.equals("+")) {
                income.setChecked(true);
                expend.setChecked(false);
                IncomeAndExpenses = "+";
            } else {
                income.setChecked(false);
                expend.setChecked(true);
                IncomeAndExpenses = "-";
            }

        } else {
            mBillChange.setVisibility(View.GONE);
            Addtransactionbills.setVisibility(View.VISIBLE);
            //设置Vip等级
            Viplv.setText(strLv[0]);
            //初始化时间
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = format.format(curDate);
            settimes.setText(str);
            Avatar();
            //初始化交易状态
            stateText.setText("交易成功");
            income.setChecked(true);
            IncomeAndExpenses = "+";
        }
    }


}
