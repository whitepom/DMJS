package kr.hmit.dmjs.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseAdapter extends RecyclerView.Adapter {

    protected Context mContext;
    public final String Tag = "Tag_TEXT";
    public final String Tag_Loc = "Tag_TEXT_Loc";
    protected RecyclerView.Adapter mAdapter;
    public String strAmount = "";

    /**
     * 문자열을 숫자형(double)로 바꿈
     *
     * @param str
     * @return double
     */
    public String commaWithNumber(String str) {
        if (str.contains(",")) {
            str = str.replace(",", "");
        }
        double strToDouble = Double.parseDouble(str);

        return commaWithNumber(strToDouble);
    }

    /**
     * 천단위마다 콤마 추가
     *
     * @param number
     * @return comma 포함한 숫자(문자열)
     */
    public String commaWithNumber(double number) {
        DecimalFormat myFormatter = new DecimalFormat("#,###.###");

        return myFormatter.format(number);
    }

    /**
     * 숫자에 콤마제거
     *
     * @param number
     * @return String
     */
    public String CommaDeleteNumber(String number) {
        return number.replace(",", "");
    }

    /**
     * 날짜형식 변경
     *
     * @param date
     * @return yyyyMMdd -> yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public String datePatternChange(String date) {
        if (date == null || TextUtils.isEmpty(date)) {
            return "";
        }
        if (date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return afterFormat.format(tempDate);
    }
    public String commaWithDouble(String str) {
        /**
         * 정수부
         */
        String jsb;
        /**
         * 소수부
         */
        String ssb = "";
        String strToDouble;
        DecimalFormat myFormatter = new DecimalFormat("#,###");

        if (str.contains(".")) {
            jsb = str.substring(0, str.indexOf("."));
            if (TextUtils.isEmpty(jsb)) {
                jsb = "0";
            }
            ssb = str.substring(str.indexOf("."));
            if (ssb.equals(".")) {
                ssb = ".";
            }
            strToDouble = myFormatter.format(Double.parseDouble(jsb)) + ssb;
        } else {
            jsb = str;
            strToDouble = myFormatter.format(Double.parseDouble(jsb));
        }

        return strToDouble;
    }
    /**
     * 날짜형식 변경
     *
     * @param date
     * @return YYYY-MM-DD -> yyyyMMdd
     */
    @SuppressLint("SimpleDateFormat")
    public String datePatternChange_2(String date) {
        if (date == null || TextUtils.isEmpty(date)) {
            return "";
        }
        if (date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return afterFormat.format(tempDate);
    }

    protected void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void log(String str) {
        Log.d(Tag, str);
    }

    /**
     * 정규식 날짜 구하기
     *
     * @param src
     * @return
     */
    public String getDate(String src) {

        long time = Long.parseLong(src.replaceFirst("^.*Date\\((\\d+)\\).*$", "$1"));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

        return format1.format(new Date(time));
    }

    /**
     * html 정규식 공백으로
     *
     * @param html
     * @return
     */
    public String stripHtml(String html) {
        String text = Html.fromHtml(html).toString();
        return text.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
    }

    public String getCategory_CLT(String src) {
        switch (src) {
            case "매입매출":
                return "A";
            case "매입":
                return "B";
            case "매출":
                return "C";
            case "농가":
                return "N";
            case "기타":
                return "Z";
            case "고객":
                return "G";
            case "A":
                return "매입매출";
            case "B":
                return "매입";
            case "C":
                return "매출";
            case "N":
                return "농가";
            case "Z":
                return "기타";
            case "G":
                return "고객";
        }
        return "";
    }

    /**
     * 자재구분
     *
     * @param src
     * @return
     */
    public String getCategory_Material(String src) {
        switch (src.trim()) {
            case "원재료":
                return "R";
            case "부재료":
                return "B";
            case "반제품":
                return "S";
            case "완제품":
                return "P";
            case "전체":
                return "";
            case "R":
                return "원재료";
            case "B":
                return "부재료";
            case "S":
                return "반제품";
            case "P":
                return "완제품";
            case "":
                return "전체";
        }
        return "";
    }

    /**
     * 운송구분
     *
     * @param src
     * @return
     */
    public String getCategory_Delivery(String src) {
        switch (src.trim()) {
            case "택배":
                return "1";
            case "화물":
                return "2";
            case "직배송":
                return "3";
            case "팔레트":
                return "4";
            case "내방":
                return "6";
            case "기타":
                return "5";
            case "1":
                return "택배";
            case "2":
                return "화물";
            case "3":
                return "직배송";
            case "4":
                return "팔레트";
            case "5":
                return "내방";
            case "6":
                return "기타";
            case "":
                return "";
        }
        return "";
    }

    /**
     * 입출구분
     *
     * @param src
     * @return
     */
    public String getCategory_InOut(String src) {
        switch (src.trim()) {
            case "입고":
                return "I";
            case "출고":
                return "O";
            case "반품":
                return "B";
            case "재고":
                return "K";
            case "전체":
                return "";
            case "I":
                return "입고";
            case "O":
                return "출고";
            case "B":
                return "반품";
            case "K":
                return "재고";
            case "":
                return "전체";
        }
        return "";
    }

    /**
     * 친환경구분
     *
     * @param src
     * @return
     */
    public String getCategory_Environment(String src) {
        switch (src.trim()) {
            case "1":
                return "일반";
            case "2":
                return "무농약";
            case "3":
                return "유기농";
            case "일반":
                return "1";
            case "무농약":
                return "2";
            case "유기농":
                return "3";

        }
        return "";
    }

    public String getCategory_Stock(String src) {
        switch (src) {
            case "R":
                return "[R]자재";
            case "P":
                return "[P]생산";
            case "S":
                return "[S]영업";
            case "O":
                return "[0]외주";
            case "C":
                return "[C]조정";
            case "D":
                return "[D]폐기";
            case "[R]자재":
                return "R";
            case "[P]생산":
                return "P";
            case "[S]영업":
                return "S";
            case "[0]외주":
                return "O";
            case "[C]조정":
                return "C";
            case "[D]폐기":
                return "D";
        }
        return "해당없음";
    }


}
