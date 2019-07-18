package xiake.vscreenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xiake.vscreenshot.R;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.ToastUtil;

/**
 * Created by Administrator on 2017/9/18 0018.
 *
 * 微信红包
 */
public class WeChatGiftActivity extends BaseActivity{

    private int CHOICE_FRIEND = 0;
    private String imagePath;
    public TextView mSendGiftName;
    public ImageView mSendGiftImg;

    @Override
    protected void setTitle() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.we_chat_gift_title);
        TextView title = (TextView) layout.findViewById(R.id.weChat_transfer_title);
        ImageView back = (ImageView) findViewById(R.id.weChat_transfer_back);
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
    Intent intent;
    @Override
    protected void initView() {

        RelativeLayout sendGift = (RelativeLayout) findViewById(R.id.send_gift);
        sendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(activity,ChoiceFriendActivity.class);
                startActivityForResult(intent,CHOICE_FRIEND);
            }
        });
        mSendGiftName = (TextView) findViewById(R.id.send_gift_name);
        mSendGiftImg = (ImageView) findViewById(R.id.send_gift_image);
        final EditText giftMoney = (EditText) findViewById(R.id.gift_money);
        final EditText giftWish = (EditText) findViewById(R.id.gift_wish);
        Button giftPreView = (Button) findViewById(R.id.gift_preview);
        giftPreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mSendGiftName.getText().toString();
                int visibility = mSendGiftImg.getVisibility();
                String wish = giftWish.getText().toString().trim();
                String money = giftMoney.getText().toString().trim();
                if (money.equals("")){
                    money = "0";
                }
                double moneyCount = Double.parseDouble(money);
                if (visibility!=0){
                    ToastUtil.show(activity,"请选择发红包的人",1);
                }else if (moneyCount<=0.00){
                    ToastUtil.show(activity,"请填写红包金额",1);
                }else if (moneyCount>200.00){
                    ToastUtil.show(activity,"红包金额不能超过200",1);
                }else {
                    if (wish.equals("")){
                        wish = "恭喜发财，大吉大利";
                    }
                    String s = CommonUtil.formatMoney(moneyCount);
                    Log.e("onClick: " , "\n" +wish);
                    intent = new Intent(activity,GiftPreViewActivity.class);
                    intent.putExtra("sendGiftName",name);
                    intent.putExtra("sendGiftImg",imagePath);
                    intent.putExtra("wish",wish);
                    intent.putExtra("giftMoney",s);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_wechat_gift;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==CHOICE_FRIEND){
                String nickName = data.getStringExtra("nickName");
                imagePath = data.getStringExtra("imgPath");
                mSendGiftName.setText(nickName);
                mSendGiftImg.setVisibility(View.VISIBLE);
                BitmapUtil.setImg(activity,mSendGiftImg,imagePath);
            }
        }
    }
}
