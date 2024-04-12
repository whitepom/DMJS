package kr.hmit.base.base_alret;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;

import kr.hmit.base.R;

//--------------------------------------------------
// 알람창을 띄운다.
//--------------------------------------------------
public class BaseAlert {

    /**
     * 기본 알림창
     *
     * @param message 메시지
     */
    public static void show(Context context, String message) {
        show(context, "", message, null);
    }

    /**
     * 제목있는 알림창
     *
     * @param title   제목
     * @param message 메시지
     */
    public static void show(Context context, String title, String message) {
        show(context, title, message, null);
    }

    /**
     * 제목없는 알림창 확인 버튼 클릭 시 이벤트를 발생시킨다.
     *
     * @param message       메시지
     * @param eventPositive 확인 이벤트
     */
    public static void show(Context context, String message, DialogInterface.OnClickListener eventPositive) {
        show(context, "", message, eventPositive);
    }

    /**
     * 제목있는 알림창 확인 버튼 클릭 시 이벤트를 발생시킨다.
     *
     * @param title
     * @param message
     * @param eventPositive
     */
    public static void show(Context context, String title, String message, DialogInterface.OnClickListener eventPositive) {
        String alertMessage = message;
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(alertMessage)
                .setTitle(title)
                .setPositiveButton(R.string.common_confirm, eventPositive)
                .create();
        dialog.show();
    }


    public static void show(Context context, String message, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative) {
        show(context, "", message, eventPositive, eventNegative);
    }


    /**
     * 알람창 기본형 확인과 취소버튼 클릭 시 이벤트를 발생시킨다.
     *
     * @param title         제목
     * @param message       메시지
     * @param eventPositive 확인 클릭 이벤트
     * @param eventNegative 취소 클릭 이벤트
     */
    public static void show(Context context, String title, String message, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative) {
        String alertMessage = message;
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(alertMessage)
                .setTitle(title)
                .setNegativeButton(R.string.common_cancel, eventNegative)
                .setPositiveButton(R.string.common_confirm, eventPositive)
                .create();
        dialog.show();
    }

    public static void show2(Context context, String title, String message, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative) {
        String alertMessage = message;
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(alertMessage)
                .setTitle(title)
                .setPositiveButton(R.string.common_confirm, eventPositive)
                .create();
        dialog.show();
    }

    /**
     * * 알람창 기본형 확인과 취소버튼 클릭 시 이벤트를 발생시킨다.
     *
     * @param message       메시지
     * @param eventPositive 확인 클릭 이벤트
     * @param eventNegative 취소 클릭 이벤트
     * @param textPositive  ex) 예
     * @param textNegative  ex) 아니오
     */
    public static void show(Context context, String message, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative, String textPositive, String textNegative) {
        String alertMessage = message;
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(alertMessage)
                .setNegativeButton(textNegative, eventNegative)
                .setPositiveButton(textPositive, eventPositive)
                .create();
        dialog.show();
    }

    /**
     * * 알람창 기본형 확인과 취소버튼 클릭 시 이벤트를 발생시킨다.
     *
     * @param title         제목
     * @param message       메시지
     * @param eventPositive 확인 클릭 이벤트
     * @param eventNegative 취소 클릭 이벤트
     * @param textPositive  ex) 예
     * @param textNegative  ex) 아니오
     */
    public static void show(Context context, String title, String message, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative, String textPositive, String textNegative) {
        String alertMessage = message;
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(alertMessage)
                .setTitle(title)
                .setNegativeButton(textNegative, eventNegative)
                .setPositiveButton(textPositive, eventPositive)
                .create();
        dialog.show();
    }

    /**
     * 기본 메시지창
     *
     * @param context
     * @param messageId
     */
    public static void show(Context context, @StringRes int messageId) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(messageId)
                .setPositiveButton(R.string.common_confirm, null)
                .create();
        dialog.show();
    }

    /**
     * 제목 있는 알림창
     *
     * @param context   Context
     * @param titleId   제목 string id
     * @param messageId 메시지 string id
     */
    public static void show(Context context, @StringRes int titleId, @StringRes int messageId) {
        show(context, titleId, messageId, null);
    }

    public static void show(Context context, @StringRes int titleId, @StringRes int messageId, DialogInterface.OnClickListener eventPositive) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(messageId)
                .setTitle(titleId)
                .setPositiveButton(R.string.common_confirm, eventPositive)
                .create();
        dialog.show();
    }

    public static void show(Context context, @StringRes int messageId, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(messageId)
                .setNegativeButton(R.string.common_cancel, eventNegative)
                .setPositiveButton(R.string.common_confirm, eventPositive)
                .create();
        dialog.show();
    }

    public static void show(Context context, @StringRes int titleId, @StringRes int messageId, DialogInterface.OnClickListener eventPositive, DialogInterface.OnClickListener eventNegative, int textPositiveId, int textNegativeId) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = customBuilder.setCancelable(false)
                .setMessage(messageId)
                .setTitle(titleId)
                .setNegativeButton(textNegativeId, eventNegative)
                .setPositiveButton(textPositiveId, eventPositive)
                .create();
        dialog.show();
    }
}
