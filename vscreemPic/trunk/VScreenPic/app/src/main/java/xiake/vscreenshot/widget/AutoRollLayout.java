package xiake.vscreenshot.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.RollItem;

/**
 * Created by Administrator on 2016/11/4.
 *    1、加载布局文件到AutoRollLayout身上
 *    2、设计方法，让外界把数据传递进来  list<RollItem>
 *          public void setRollItems(List<RollItem> rollItems)
 *    3、设计方法，让外界决定是否自动轮播
 *          public void setAutoRoll(boolean isAuto)
 *    4、使用接口回调：
 *          ①、定义接口
 *          ②、对外提供方法把接口的实现类传进来
 *          ③、在合适的地方执行接口回调  一定要判断非空
 *     5、onClick单击事件的执行流程
 *          View的OnTouchEvent===》performClick()===》onClick(View v)
 *     6、手势识别器
 *          1、创建手势识别器
 *          2、把触摸事件交给手势识别器分析
 */
public class AutoRollLayout extends FrameLayout {
    private ViewPager mViewPager;
    private TextView mTvTitle;
    private LinearLayout mLL_Points;
    private int mDpToPx10;
    private GestureDetector mGestureDetector;

    public AutoRollLayout(Context context) {
        this(context, null);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public AutoRollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public AutoRollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        //1、创建手势识别器
        mGestureDetector = new GestureDetector(context,mOnGestureListener);
    }
    private GestureDetector.OnGestureListener mOnGestureListener=new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {//按下
            Log.i("test","onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {//短按
            Log.i("test","onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {//单击
            Log.i("test","onSingleTapUp");
            //执行接口回调
            // ③、在合适的地方执行接口回调
            if (onItemsChoseListener!=null){
                onItemsChoseListener.chooseItems(mViewPager.getCurrentItem());
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {//滚动
            Log.i("test","onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {//长按
            Log.i("test","onLongPress");

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {//惯性滑动
            Log.i("test","onFling");
            return false;
        }
    };
    //①、定义接口
    public interface OnItemsChoseListener{
        public void chooseItems(int position);
    }
    //②、对外提供方法把接口的实现类传进来
    private OnItemsChoseListener onItemsChoseListener;
    public void setOnItemsChoseListener(OnItemsChoseListener onItemsChoseListener) {
        this.onItemsChoseListener = onItemsChoseListener;
    }

    private void init(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        View.inflate(context, R.layout.arl_layout, this);
        //找控件
        mViewPager = ((ViewPager) findViewById(R.id.arl_ViewPager));
//        mTvTitle = ((TextView) findViewById(R.id.arl_tv_title));
        mLL_Points = ((LinearLayout) findViewById(R.id.arl_ll_points));
        mDpToPx10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        //给ViewPager设置触摸事件的监听
        mViewPager.setOnTouchListener(mOnTouchListener);
        //给ViewPager设置单击事件
       /* mViewPager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ③、在合适的地方执行接口回调
                if (onItemsChoseListener!=null){
                    onItemsChoseListener.chooseItems(mViewPager.getCurrentItem());
                }
            }
        });*/
    }
    private OnTouchListener mOnTouchListener=new OnTouchListener() {
        /**
         * @param v  指的是ViewPager
         * @param event    触摸事件
         * @return      是否处理本次事件   指的是：当返回false的时候，onTouchEvent方法才会被调用
         *              决定onTouchEvent方法能不能被调用
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            v.getParent().requestDisallowInterceptTouchEvent(true);
            //2、把触摸事件交给手势识别器分析
            mGestureDetector.onTouchEvent(event);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
//                    Log.i("test", "onTouch==ACTION_DOWN");
                    mHandler.removeCallbacks(goToNextRunnable);
                    break;
                case MotionEvent.ACTION_MOVE:
//                    Log.i("test", "onTouch==ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_CANCEL:
//                    Log.i("test","onTouch==ACTION_CANCEL");
                case MotionEvent.ACTION_UP:
//                    Log.i("test","onTouch==ACTION_UP");
                   if (isAuto){
                       mHandler.postDelayed(goToNextRunnable,interval);
                   }
                    break;
            }
            return false;
        }
    };
    //AutoRollLayout拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.i("test", "onInterceptTouchEvent==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("test","onInterceptTouchEvent==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
//                Log.i("test","onInterceptTouchEvent==ACTION_UP");
                break;
        }
        return false;
    }
    //处理事件
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("test","onTouchEvent==ACTION_DOWN");
                //停止轮播
                mHandler.removeCallbacks(goToNextRunnable);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("test","onTouchEvent==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("test","onTouchEvent==ACTION_UP");
                mHandler.postDelayed(goToNextRunnable,2000);
                break;
        }
        return true;
    }*/

    //3、设计方法，让外界决定是否自动轮播
    private boolean isAuto;
    private int interval = 5000;
    /**
     *
     * @param isAuto    是否自动轮播
     * @param interval  自动轮播间隔时间（毫秒）
     */
    public void setAutoRoll(boolean isAuto,int interval){
        this.isAuto=isAuto;
        this.interval = interval;
        if (isAuto){
            //开始轮播
            mHandler.removeCallbacks(goToNextRunnable);
            mHandler.postDelayed(goToNextRunnable,interval);
        }else{
            //停止轮播
            mHandler.removeCallbacks(goToNextRunnable);
        }
    }
    private Handler mHandler=new Handler();
    private Runnable goToNextRunnable=new Runnable() {
        private boolean isLeftToRight=true;
        @Override
        public void run() {
            //决定轮播的方向
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem==0){
                isLeftToRight=true;//左==》右
            }else if (currentItem==mPagerAdapter.getCount()-1){
                isLeftToRight=false;//右==》左
            }
            //跳转到下一个页面（开始轮播）
            int target=0;
            if (isLeftToRight){
                target=currentItem+1;
                mViewPager.setCurrentItem(target);
            }else{
                target=currentItem-1;
                mViewPager.setCurrentItem(target);
            }
//            Log.i("test",rollItems.get(target).getTitle());
            //递归调用
            mHandler.postDelayed(this,interval);
        }
    };
    //2、设计方法，让外界把数据传递进来  list<RollItem>
    private List<RollItem> rollItems;
    public void setRollItems(List<RollItem> rollItems){
        //非空判断
        if (rollItems==null){
            return;
        }
        this.rollItems=rollItems;
        //1、给ViewPager设置适配器
        mViewPager.setAdapter(mPagerAdapter);
        //2、设置每张图片的标题  (要给ViewPager设置滑动监听)
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        //3、初始化点
//        initPoints();
        mOnPageChangeListener.onPageSelected(0);
    }

    private void initPoints() {
        /*<View
                android:layout_width="10dp"
                android:layout_height="10dp"
                android:background="@drawable/arl_point_red"
                android:layout_marginRight="10dp"/>*/
        //清空之前的所有点
        mLL_Points.removeAllViews();

        //变量集合
        for (int i = 0; i < rollItems.size(); i++) {
            //创建点
            View view=new View(getContext());
            //设置布局参数
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(mDpToPx10,mDpToPx10);
            params.rightMargin=mDpToPx10;
            view.setLayoutParams(params);
            //设置背景
//            view.setBackgroundResource(R.drawable.arl_point_selector);
            //添加到线性布局身上
//            mLL_Points.addView(view,params);
            //给点提交点击事件
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = mLL_Points.indexOfChild(v);
                    mViewPager.setCurrentItem(index);
                }
            });
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener=new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            //获取当前也的数据
            RollItem rollItem = rollItems.get(position);
            //设置标题
//            mTvTitle.setText(rollItem.getTitle());
            //让当前的点不可用（红色），其他的点可用（白色）
//            int childCount = mLL_Points.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//               /* if (i==position){
//                    mLL_Points.getChildAt(i).setEnabled(false);//红色
//                }else{
//                    mLL_Points.getChildAt(i).setEnabled(true);//红色
//                }*/
//                mLL_Points.getChildAt(i).setEnabled(i!=position);
//            }
        }
    };
    private PagerAdapter mPagerAdapter=new PagerAdapter() {
        //缓存ImageView
        private List<ImageView> cacheImage=new ArrayList<ImageView>();
        @Override
        public int getCount() {
            return rollItems==null?0:rollItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            Log.i("test","position="+position);
            if (cacheImage.size()==0){
                ImageView imageView=new ImageView(container.getContext());
                cacheImage.add(imageView);
            }
            ImageView imageView = cacheImage.remove(0);
            //跟据url显示图片
            RollItem rollItem = rollItems.get(position);
            //优化：加上fit（）手动解决大图片问题
            Glide.with(container.getContext()).load(rollItem.getImageUrl()).asBitmap().centerCrop().into(imageView);
            //添加到容器
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
           //缓存起来
            cacheImage.add((ImageView) object);
        }
    };
}
