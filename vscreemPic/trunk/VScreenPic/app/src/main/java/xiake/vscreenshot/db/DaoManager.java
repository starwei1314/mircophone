package xiake.vscreenshot.db;

import android.content.Context;

import com.google.gson.Gson;

import org.greenrobot.greendao.database.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xiake.db.dao.DaoMaster;
import xiake.db.dao.DaoSession;
import xiake.db.dao.DataDao;
import xiake.db.entity.Data;
import xiake.vscreenshot.bean.DefaultData;
import xiake.vscreenshot.util.CommonUtil;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class DaoManager {
    public volatile static DaoManager instance;
    private Context mContext;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static DaoManager getInstance(){
        if (instance==null){
            synchronized (DaoManager.class){
                if (instance ==null){
                    instance = new DaoManager();
                }
            }
        }
        return instance;
    }
    //传递上下文
    public void  initManager(Context context){
        this.mContext = context;
    }
    public DaoMaster getDaoMaster() {
        if (mDaoMaster==null){
            mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext,"screenShot.db");
            Database writableDb = mDevOpenHelper.getWritableDb();
            mDaoMaster = new DaoMaster(writableDb);
        }

        return mDaoMaster;
    }
    public DaoSession getDaoSession(){
        if (mDaoSession==null){
            if (mDaoMaster==null){
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    public List<Data> CheckUpDataBase(Context context) {

        initManager(context);
        DataDao dataDao = getDaoSession().getDataDao();
        List<Data> datas = dataDao.loadAll();
        if (datas.size() == 0) {
            instance.createData();
            datas = dataDao.loadAll();
        }
        return datas;
    }

    public Database getWritableDb() {
        return mDevOpenHelper.getWritableDb();
    }

    /**
     * 创建初始数据
     */
    public void createData() {
        /**
         * 聊天界面初始提示语
         */
        String contents = "1.点击头部昵称可切换聊天对象<br />";
        contents += "2.点击右下角＋添加时间以及各种类型的消息<br />";
        contents += "3.长按消息可删除<br />";
        contents += "4.点击右上角图标编辑聊天人信息以及设置聊天背景";
        Gson gson = new Gson();
        DataDao dataDao = getDaoSession().getDataDao();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //我的信息
        int s = (int) (Math.random() * CommonUtil.randomNum);
        DefaultData.MyInfoBean myInfoBean = new DefaultData.MyInfoBean(
                CommonUtil.defaultPath+"picture"+s+".jpg", "老板",999999999, "laoban2017", "-1");
        String myInfo = gson.toJson(myInfoBean);
        //新的朋友
        List<DefaultData.NewFriendListBean> newFriendList = new ArrayList<>();

        String date = format.format(new Date(System.currentTimeMillis()));
        DefaultData.NewFriendListBean newFriendListBean = new DefaultData.NewFriendListBean(
                "恭喜发财",true,"小V",-1,0,CommonUtil.defaultPath+"picture0"+".jpg",1,"",date,false,new ArrayList<DefaultData.ChatRecordBean>());
        newFriendList.add(newFriendListBean);

        //所有朋友
        //聊天内容相关
        List<DefaultData.ChatRecordBean> chatRecords = new ArrayList<>();
        DefaultData.ChatRecordBean chatRecordBean = new DefaultData.ChatRecordBean(
                0, false, false, false, "", 999, "text", contents, date.split(" ")[1],"", "");
        chatRecords.add(chatRecordBean);
        //  聊天列表
        List<DefaultData.MailListBean> mailList = new ArrayList<>();
        DefaultData.MailListBean mailListBean = new DefaultData.MailListBean(
                "小V", 1, 0, CommonUtil.defaultPath+"picture0"+".jpg", 1, "", date, false,chatRecords );
        mailList.add(mailListBean);

        List<DefaultData.AllfriendBean> allfriends = new ArrayList<>();
        DefaultData.AllfriendBean allfriendBean = new DefaultData.AllfriendBean("X", mailList);
        allfriends.add(allfriendBean);

        Data dataEntity = new Data();
        dataEntity.setMy_info(myInfo);
        dataEntity.setNew_friend_list(gson.toJson(newFriendList));
        dataEntity.setAllfriend(gson.toJson(allfriends));
        dataDao.insertOrReplace(dataEntity);

    }


}
