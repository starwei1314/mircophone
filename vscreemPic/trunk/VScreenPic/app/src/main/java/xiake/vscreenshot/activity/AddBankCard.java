package xiake.vscreenshot.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import xiake.vscreenshot.R;
import xiake.vscreenshot.util.pinyin.RandomUtil;

public class AddBankCard extends BaseActivity {
    private static final String TAG = "AddBankCard";
    private RelativeLayout addBankcard;
    private EditText bankNumber;
    private TextView bankName;
    private Button randomNumber;
    private Button saveBank;
    private ImageView addBankcard_transfer_back;
    public Intent mIntent;

    @Override
    protected void setTitle() {

    }

    @Override
    protected void initView() {
        mIntent = getIntent();
        init();
        final String[] bankNames = initData();
        clickOnTheEvent(bankNames);
    }

    private void clickOnTheEvent(final String[] bankNames) {
        addBankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AddBankCard.this).setTitle("选择银行")
                        .setItems(bankNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bankName.setText(bankNames[i]);
                            }
                        })
                .show();



            }
        });


        randomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] ints = RandomUtil.randomCommon(1000, 9999, 1);

                String Bank4Num = String.valueOf(ints[0]);

                bankNumber.setText(Bank4Num);
                bankNumber.setSelection(Bank4Num.length());

            }
        });


        saveBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bankNumbers = bankNumber.getText().toString().trim();
                String Name = bankName.getText().toString().trim();
                Log.e(TAG,"银行名称:"+Name);
                Log.e(TAG,"卡号后四位:"+bankNumbers);
                String bankCard = Name + "(" + bankNumbers + ")";

                saveBankList(bankCard);
                mIntent.putExtra("bankCard",bankCard);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });


        addBankcard_transfer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 保存银行列表
     * @param bankCard
     */
    private void saveBankList(String bankCard) {
        String bankList = getSharedPreferences("config", MODE_PRIVATE).getString("BankList","");
        if (bankList.equals("")){
            List<String> banklistDef = new ArrayList<>();
            banklistDef.add("余额");
            banklistDef.add("余额宝");
            Gson gson = new Gson();
            bankList = gson.toJson(banklistDef);
        }
        List<String> banklist = mGson.fromJson(bankList,new TypeToken<List<String>>(){}.getType());
        banklist.add(bankCard);
        String banklistJson = mGson.toJson(banklist);
        getSharedPreferences("config",MODE_PRIVATE).edit().putString("BankList",banklistJson).commit();
    }

    @NonNull
    private String[] initData() {
        return new String[]{"招商银行","工商银行","农业银行","中国银行","建设银行","邮政储蓄","交通银行","浦发银行","兴业银行","华夏银行","广发银行","民生银行" +
                    "中信银行","光大银行","恒丰银行","浙商银行","渤海银行","平安银行"};
    }

    private void init() {
        addBankcard = (RelativeLayout) findViewById(R.id.addBankcard);
        bankNumber = (EditText) findViewById(R.id.bankNumber);
        bankName = (TextView) findViewById(R.id.bankName);
        randomNumber = (Button) findViewById(R.id.randomNumber);
        saveBank = (Button) findViewById(R.id.saveBank);
        addBankcard_transfer_back = (ImageView) findViewById(R.id.addBankcard_transfer_back);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_bank_card;
    }

}
