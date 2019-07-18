package xiake.vscreenshot.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import xiake.vscreenshot.R;
import xiake.vscreenshot.util.CommonUtil;

/**
 * Created by Administrator on 2017/9/18 0018.
 * <p>
 * 支付宝余额
 */
public class AlipayMoneyActivity extends BaseActivity {

    @Override
    protected void setTitle() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.we_chat_small_money_title);
        TextView title = (TextView) layout.findViewById(R.id.weChat_transfer_title);
        ImageView back = (ImageView) layout.findViewById(R.id.weChat_transfer_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("title");
        title.setText(stringExtra);
    }

    @Override
    protected void initView() {
        final EditText smallMoney = (EditText) findViewById(R.id.small_money_money);
        Button smallMoneyPreview = (Button) findViewById(R.id.small_money_preview);
        smallMoneyPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String smallmoney = smallMoney.getText().toString().trim();
                if (smallmoney.equals("")) {
                    smallmoney = "0";
                }
                double d_money = Double.parseDouble(smallmoney);
                String s_money = CommonUtil.formatMoney(d_money);
                Intent intent = new Intent(activity, AliSmallMoneyPreviewActivity.class);
                intent.putExtra("money", s_money);
                startActivity(intent);

            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_alipay_money;
    }
}
