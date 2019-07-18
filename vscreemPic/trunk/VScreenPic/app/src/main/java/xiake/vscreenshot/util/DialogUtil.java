package xiake.vscreenshot.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class DialogUtil {

    public static Dialog createButtomDialog(Context context, @StyleRes int styleRes){
        Dialog dialog = new Dialog(context,styleRes);
        return dialog;
    }

}
