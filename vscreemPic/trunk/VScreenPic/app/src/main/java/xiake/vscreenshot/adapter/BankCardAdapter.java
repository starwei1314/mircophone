package xiake.vscreenshot.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import xiake.vscreenshot.R;


/**
 * Created by Administrator on 2017/10/16 0016.
 */
public class BankCardAdapter extends BaseAdapter {
    private final List<String> list;
    private final Activity activity;
    private OnItemClickListener listener;

    public BankCardAdapter(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.bank_name_list,null);
            viewHolder.bankName = (TextView) convertView.findViewById(R.id.bank_name_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }
        viewHolder.bankName.setText(list.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemclick(position, viewHolder);
            }
        });
        return convertView;
    }
   public static class ViewHolder{
        TextView bankName;
    }

    public interface OnItemClickListener{
        void onItemclick(int position,ViewHolder holder);
    }
}
