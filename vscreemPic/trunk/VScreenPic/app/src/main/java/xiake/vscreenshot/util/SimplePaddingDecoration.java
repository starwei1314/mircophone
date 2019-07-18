package xiake.vscreenshot.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import xiake.vscreenshot.R;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class SimplePaddingDecoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;

    public SimplePaddingDecoration(Context context) {
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.margin1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;//类似加了一个bottom padding
    }
}
