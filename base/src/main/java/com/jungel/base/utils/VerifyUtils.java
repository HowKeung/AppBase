package com.jungel.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by lion on 2017/3/24.
 */

public class VerifyUtils {

    /**
     * account type
     */
    public static final int ACCOUNT_TYPE_PHONE = 1;
    public static final int ACCOUNT_TYPE_EMAIL = 2;

    /**
     * 判断电话号码是否合法
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobilePhoneNnumber(String mobiles) {
        Pattern p = Pattern.compile("^((1[1-9][0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static int getAccountType(String account) {
        int accountType = -1;
        if (isMobilePhoneNnumber(account)) {
            accountType = ACCOUNT_TYPE_PHONE;
        }

        if (isEmail(account)) {
            accountType = ACCOUNT_TYPE_EMAIL;
        }

        return accountType;
    }

    public static boolean isRightAccount(String email) {
        boolean isPhone = isMobilePhoneNnumber(email);
        boolean isEmail = isEmail(email);

        return isPhone || isEmail;
    }

    /**
     * is password correct
     *
     * @param password password string
     * @return
     */
    public static boolean isPasswordAvailable(String password) {
        String passwordReg = "^(?![0-9]+$)(?![a-zA-Z_]+$)[\\S]{6,}$";//必须包含字母和数字
        if (password.matches(passwordReg)) {
            //        if(password.matches("(?![0-9]+$)(?![a-zA-Z_]+$)[\\S]{6,}$")){
            return true;
        }
        return false;
    }

    /**
     * is the right id card number
     *
     * @param idNo
     * @return
     */
    public static boolean isRightIdNo(String idNo) {
        if (TextUtils.isEmpty(idNo)) {
            return false;
        }
        String reg = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        return idNo.matches(reg);
    }

    public static String getBirthFromIdNo(String idNo) {
        boolean right = isRightIdNo(idNo);

        if (right) {
            try {
                String year = idNo.substring(6, 10);
                String month = idNo.substring(10, 12);
                String day = idNo.substring(12, 14);

                return year + "-" + month + "-" + day;
            } catch (Exception e) {
                LogUtils.exception(e);
            }
        }

        return "";
    }

    /**
     * 判断字符串全是数字
     *
     * @param str 待测字符串
     * @return true or false
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isEditTextEmpty(EditText editText) {
        return editText == null || TextUtils.isEmpty(editText.getText());
    }

    public static int editTextLength(EditText editText) {
        if (editText == null) {
            return 0;
        }

        return editText.getText().toString().length();
    }

    /**
     * 获取bitmap的size
     *
     * @param bitmap
     * @return
     */
    public static long getBitmapsize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static Bitmap compress(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return bitmap;//压缩好比例大小后再进行质量压缩
        //return bitmap;
    }

    public static boolean isNickName(String str) throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim().equals(str);
    }


    public static void main(String[] args) {
        System.out.println(isNickName("123中文skdjfkjasdk™ ๑·ิ.·ั๑ ♧ ♡"));
        System.out.println(isNickName("123中文skdjfkjasdk"));
        System.out.println(isNickName("Toolang"));
        System.out.println(isPasswordAvailable("123abcd"));
        System.out.println(isPasswordAvailable("123456"));
    }
}
