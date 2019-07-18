package xiake.vscreenshot.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class BitmapUtil {

    private static final String TAG = "BitmapUtil";
    private static String cutImgPath = Environment.getExternalStorageDirectory() + "/XiaKe/vscreenshot/";
    public static Uri cutImgUri;
    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param dirName 文件夹名称
     * @param bitmap   图片
     *
     */
    public static String saveFile(Context c, String dirName, Bitmap bitmap) {
        return saveFile(c, "", dirName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String dirName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, dirName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String dirName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = ".jpg";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = cutImgPath + dirName + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, dateFolder + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, dateFolder + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    public static void setImg(Activity activity, ImageView friendImg, String imgPath) {

        String im = "";
        String fi = CommonUtil.defaultPath;
        InputStream open = null;
        AssetManager assets = activity.getAssets();
        if (imgPath.contains(fi)) {
            im = imgPath.substring(fi.length());
            try {
                open = assets.open("image/" + im);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
                System.out.print("friendImg" + friendImg);
                friendImg.setImageBitmap(bitmap);
                open.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Glide.with(activity).load(imgPath).asBitmap().into(friendImg);
        }

    }

    /**
     * 裁剪图片
     * @param uri
     * @return
     */
    public static Intent cutImage(Uri uri) {
        File file = new File(cutImgPath);
        if (!file.exists()){
            file.mkdir();
        }
        File cutFile = new File(file.getAbsolutePath() + "/cut.png");
        cutImgUri = Uri.fromFile(cutFile);
        Log.e("uri", "cutImage: " + "uri=======" + uri);
        if (uri == null) {
            Log.e("cutImage", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded",true);
        intent.putExtra("return-data", false);
        Log.e(TAG, "cutImage: " + "\n" +cutImgUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutImgUri);
        intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
        return intent;
    }
}
