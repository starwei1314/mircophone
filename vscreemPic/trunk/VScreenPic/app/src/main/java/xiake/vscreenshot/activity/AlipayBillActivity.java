package xiake.vscreenshot.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import xiake.db.dao.BillDao;
import xiake.db.entity.Bill;
import xiake.vscreenshot.R;
import xiake.vscreenshot.adapter.PayBillAdapter;
import xiake.vscreenshot.adapter.RecyclerViewAdapter;
import xiake.vscreenshot.bean.BillDetails;
import xiake.vscreenshot.util.ToastUtil;

/**
 * Created by Administrator on 2017/9/18 0018.
 *
 * 支付宝账单
 */
public class AlipayBillActivity extends BaseActivity{
    private static final String TAG = "AlipayBillActivity";
    private Context mContext = AlipayBillActivity.this;
    List<Bill> bills = new ArrayList<>();
    private PayBillAdapter adapter;
    public BillDao mBillDao;
    public TextView mClear;
    private List<BillDetails> mBillDetails;

    @Override
    protected void setTitle() {
        mBillDetails = new ArrayList<>();
        mBillDao = mDaoSession.getBillDao();
        bills = mBillDao.loadAll();
        //给集合排序
        if (bills.size()>1){
            sortList();
        }
        if (bills.size()>0){
            setDetails();
        }
        Log.e(TAG, "setTitle: " + "\n" +bills.size());
    }

    private void setDetails() {
        mBillDetails.clear();
        for (Bill bill : bills) {
            List<BillDetails> billDetailses = mGson.fromJson(bill.getDetails(),new TypeToken<List<BillDetails>>(){}.getType());
            for (BillDetails billDetailse : billDetailses) {
                mBillDetails.add(billDetailse);
            }
        }
    }

    @Override
    protected void initView() {
        ((ImageView) findViewById(R.id.alipay_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //清空账单
        mClear = (TextView) findViewById(R.id.alipay_bill_clear);
        initData();
        setClear();
        initListener();
    }

    private void initListener() {
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog
                        .Builder(activity)
                        .setMessage("确定清空账单吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBillDao.deleteAll();
                                bills.clear();
                                mBillDetails.clear();
                                setClear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });
        /**
         * 添加账单
         */
        ImageView addBill = (ImageView) findViewById(R.id.alipay_add);
        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlipayBillActivity.this,Addbill.class));
            }
        });


        /**
         * 生成预览
         */
        ((Button) findViewById(R.id.alipay_bill_preview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bills.size()==0){
                    ToastUtil.show(activity,"请先添加账单",1);
                }else {
                    String billsJson = mGson.toJson(bills);
                    Intent intent = new Intent(activity,AlipayBillPreViewActivity.class);
                    intent.putExtra("billsJson",billsJson);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        RecyclerView payBill = (RecyclerView) findViewById(R.id.alipayBill);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        payBill.setLayoutManager(layoutManager);
        adapter = new PayBillAdapter(activity);

        adapter.setDataSource(mBillDetails);
        payBill.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerViewAdapter.ClickableViewHolder holder) {
                if (holder instanceof PayBillAdapter.PayBillViewHolder){
                    BillDetails billDetails = mBillDetails.get(position);
                    String billDetailJson = mGson.toJson(billDetails);
                    Log.e(TAG, "onItemClick: " + "\n" + billDetailJson);
                    Intent intent = new Intent(activity, Addbill.class);
                    intent.putExtra("billDetailJson",billDetailJson);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 冒泡排序
     */

    private void sortList() {
        for (int i = 0; i < bills.size()-1; i++) {
            for (int j = 0; j < bills.size()-i-1; j++) {
                String time1 = bills.get(j).getTime();
                String time2 = bills.get(j+1).getTime();
                if (formatTime(time1)<formatTime(time2)){
                    Bill temp;
                    temp = bills.get(j);
                    bills.set(j,bills.get(j+1));
                    bills.set(j+1,temp);
                }

            }
        }

    }
    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private long formatTime(String time) {
        long timeMillis = 0;
        try {
            timeMillis = format.parse(time).getTime();

        } catch (ParseException e) {
            Log.e(TAG, "sortList: " + "\n" +e.toString());
        }
        return timeMillis;
    }

    /**
     * 设置 "清空" 按钮状态
     */
    private void setClear() {
        if (bills.size()==0){
            mClear.setClickable(false);
            mClear.setTextColor(Color.GRAY);
        }else {
            mClear.setClickable(true);
            mClear.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_alipay_bill;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bills = mBillDao.loadAll();
        Log.e(TAG, "onResume: " + "\n" +mGson.toJson(bills));
        setClear();
        if (bills.size()>1){
            sortList();
        }
        if (bills.size()>0){
            setDetails();
        }
        adapter.setDataSource(mBillDetails);
        adapter.notifyDataSetChanged();
    }

}




