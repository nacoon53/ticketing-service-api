package kr.hhplus.be.server.module.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        // 원하는 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 포맷 적용
        return dateTime.format(formatter);
    }
}
