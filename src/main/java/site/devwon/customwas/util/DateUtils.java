package site.devwon.customwas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * date, format을 받아와서 date를 format 형식으로 반환 한다.
     *
     * @param date, format
     * @return String: formatted date
     */

    public static String getDateTimeFormat(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
