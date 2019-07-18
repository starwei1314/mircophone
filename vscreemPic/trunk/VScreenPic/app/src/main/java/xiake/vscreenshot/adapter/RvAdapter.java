package xiake.vscreenshot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xiake.vscreenshot.R;
import xiake.vscreenshot.activity.MainActivity;

/**
 * Created by Administrator on 2017/9/18 0018.
 */
public class RvAdapter extends RecyclerViewAdapter {
    private final Integer[] imageRes;
    private final String[] textRes;

    public RvAdapter(MainActivity mainActivity, String[] textRes, Integer[] imageRes) {
        this.context = mainActivity;
        this.textRes = textRes;
        this.imageRes = imageRes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        View inflate = LayoutInflater.from(context).inflate(R.layout.screen_shot_item, null);
        ItemViewHolder itemViewHolder = new ItemViewHolder(inflate);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mText.setText(textRes[position]);
            itemViewHolder.mImage.setImageResource(imageRes[position]);
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return textRes.length;
    }

    private class ItemViewHolder extends RecyclerViewAdapter.ClickableViewHolder{

        public final ImageView mImage;
        public final TextView mText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImage = getView(R.id.item_image);
            mText = getView(R.id.item_text);
        }
    }
}
