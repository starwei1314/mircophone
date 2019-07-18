package xiake.vscreenshot.util;


import android.app.Activity;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyHelper {

    public static List list;
    private static String PATH = "/sdcard/xiake/relay/";

    public static boolean isfileExist(String fileName) {
        File file = new File(PATH + fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }

    }

    public static String getTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    //region 读写共享值
    public static String GetString(String key, String DefaultString) {
        try {
            String encoding = "UTF-8";
            File file = new File(PATH + key);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = bufferedReader.readLine();
                read.close();
                return lineTxt;
            } else {
                return DefaultString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DefaultString;
        }
    }

    public static String md5Stream(InputStream is) throws IOException {
        String md5 = "";
        try {
            byte[] bytes = new byte[4096];
            int read = 0;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            while ((read = is.read(bytes)) != -1) {
                digest.update(bytes, 0, read);
            }
            md5 = new BigInteger(1, digest.digest()).toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static void SetString(String key, String value) {
        BufferedWriter fw = null;
        try {
            createFile();
            File file = new File(PATH + key);
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
            fw.write(value);
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion

    public static void SaveOne(String key, String value) {
        FileWriter fw = null;
        try {
            createFile();
            File file = new File(PATH + key);
            fw = new FileWriter(file, true);//以追加的模式将字符写入
            BufferedWriter bw = new BufferedWriter(fw);//包裹一层缓冲流 增强IO功能
            bw.write(value);
            bw.write("\r\n");
            bw.flush();//将内容一次性写入文件
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //删除SD卡上的文件
    public static boolean deleteFile(String key) {
        File file = new File(PATH + key);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    public static List<String> readAll(String key) {
        List<String> list = new ArrayList<String>();
        File file = new File(PATH, key);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String str = "";
            while ((str = br.readLine()) != null) {
                String content = str;
                list.add(content);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> readText(String path) {
        List<String> list = new ArrayList<String>();
        File file = new File(path);

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String str = "";
            while ((str = br.readLine()) != null) {
                String content = str;
                list.add(content);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    //发送消息
    public static void SendMsgToServer(final String key, final String value) {
        new Thread() {
            @Override
            public void run() {
                File file = new File("/sdcard/share_" + UUID.randomUUID());
                BufferedWriter fw = null;
                try {
                    fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
                    fw.write(key + "\n" + StringToBase64(value));
                    fw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.start();
    }

    public static String userid = null;

    public static String obtainUserID(String value) {
        String val = value.substring(value.indexOf("v1_"), value.indexOf("@stranger"));
        return val;
    }


    /* @note 获取该activity所有view

    * */

    public List<View> getAllChildViews(Activity activity) {

        View view = activity.getWindow().getDecorView();
        return getAllChildViews(view);
    }

    private List<View> getAllChildViews(View view) {

        List<View> allchildren = new ArrayList<View>();

        if (view instanceof ListView) {
            ListView list = (ListView) view;
            /*RelativeLayout relativeLayout= (RelativeLayout) list.getAdapter().getView(1,null,null);//RelativeLayout是listview的item父布局
            TextView text= (TextView) relativeLayout.getChildAt(0);//父布局里面的子控件是第几个就填第几个getChildAt
           ;*/
        }


        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            if (vp instanceof ListView) {
                //ListView list=(ListView)vp.getChildAt(0);
                /*RelativeLayout relativeLayout= (RelativeLayout) list.getAdapter().getView(1,null,null);//RelativeLayout是listview的item父布局
                TextView text= (TextView) relativeLayout.getChildAt(0);//父布局里面的子控件是第几个就填第几个getChildAt
                ;*/
            }
            if (vp instanceof RelativeLayout) {

            }

            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                if (viewchild.toString().contains("TextView")) {
                    TextView textView = (TextView) viewchild;

                }
                if (viewchild.toString().contains("ListView")) {
                    ListView list = (ListView) viewchild;

                }
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    public static byte[] ReadFile(String filename) {
        byte[] data = new byte[0];
        File f = new File(filename);
        if (!f.exists()) {
            return data;
        }
        try {
            FileChannel channel = null;
            FileInputStream fs = null;
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            data = byteBuffer.array();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //region base64转换
    public static String StringToBase64(String str) throws UnsupportedEncodingException {
        String strBase64 = new String(Base64.encode(str.getBytes("UTF-8"), Base64.DEFAULT));
        return strBase64.replace("\r", "").replace("\n", "");
    }

    public static String Base64ToString(String strBase64) throws UnsupportedEncodingException {
        return new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT), "UTF-8");
    }
    //endregion

    //创建侠客目录
    public static void createFile() {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //创建侠客目录
    public static void createFile2(String pahts) {
        File file = new File(pahts);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    //修复微信黑屏
    public static void execCommand(String command) {
        // start the ls command running
        //String[] args =  new String[]{"sh", "-c", command};
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
            //如果有参数的话可以用另外一个被重载的exec方法
            //实际上这样执行时启动了一个子进程,它没有父进程的控制台
            //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
            InputStream inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            // read the ls output
            String line = "";
            StringBuilder sb = new StringBuilder(line);
            while ((line = bufferedreader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append('\n');
            }
            //tv.setText(sb.toString());
            //使用exec执行不会等执行成功以后才返回,它会立即返回
            //所以在某些情况下是很要命的(比如复制文件的时候)
            //使用wairFor()可以等待命令执行完成以后才返回
            try {
                if (proc.waitFor() != 0) {
                    System.err.println("exit value = " + proc.exitValue());
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        } catch (Exception e) {
        }
    }

    //在SD卡上创建文件
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(PATH , fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {

            }

        }
        return file;
    }

    public static String getFilePath() {
        return PATH;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    //删除SD卡上的文件
    public static boolean deleteSDFile(String fileName) {
        File file = new File(PATH + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    public static void inputstreamtofile(ByteArrayInputStream ins, File file) {
        try {
            FileOutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) > 0) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //写入内容到文件中
    public static void writeSDFile(String str, String fileName) throws IOException {
        FileWriter fw = new FileWriter(PATH + fileName);
        File f = new File(PATH + fileName);
        fw.write(str);
        FileOutputStream os = new FileOutputStream(f);
        DataOutputStream out = new DataOutputStream(os);
        out.writeShort(2);
        out.writeUTF("");
        fw.flush();
        fw.close();
    }

    //读取SD卡中文本文件
    public static String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File file = new File(PATH + fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }

    }
}



