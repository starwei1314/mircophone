package xiake.vscreenshot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.List;

import xiake.db.dao.DaoSession;
import xiake.db.dao.DataDao;
import xiake.db.entity.Data;
import xiake.vscreenshot.db.DaoManager;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public BaseActivity activity;
    public Gson mGson;
    public DataDao mDataDao;
    public List<Data> datas;
    public DaoSession mDaoSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getLayoutRes());

        activity = this;

        initData();

        setTitle();
        initView();


    }



    private void initData() {
//        Log.e(TAG, "initData: " + "\n" +"--base--");
        mGson = new Gson();
        DaoManager instance = DaoManager.getInstance();
        instance.initManager(this);
        instance.getDaoMaster().createAllTables(instance.getWritableDb(),true);
        mDaoSession = instance.getDaoSession();
        mDataDao = mDaoSession.getDataDao();
        datas = DaoManager.getInstance().CheckUpDataBase(this);
    }

    protected abstract void setTitle();

    protected abstract void initView() ;

    public abstract int getLayoutRes();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDaoSession.clear();
    }
}
