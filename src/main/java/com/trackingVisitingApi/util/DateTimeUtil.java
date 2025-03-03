package com.trackingVisitingApi.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

public final class DateTimeUtil {

    private DateTimeUtil() {}

    public static OffsetDateTime convertWithAppendOffset(LocalDateTime localDateTime, TimeZone timeZone) {
        OffsetDateTime offsetDateTime = localDateTime.atZone(timeZone.toZoneId()).toOffsetDateTime();
        return appendOffset(offsetDateTime);
    }

    public static LocalDateTime convertWithRemoveOffset(OffsetDateTime offsetDateTime) {
        return removeOffset(offsetDateTime).toLocalDateTime();
    }

    public static LocalDateTime appendOffset(LocalDateTime localDateTime, TimeZone timeZone) {
        OffsetDateTime offsetDateTime = localDateTime.atZone(timeZone.toZoneId()).toOffsetDateTime();
        return appendOffset(offsetDateTime).toLocalDateTime();
    }

    public static LocalDateTime removeOffset(LocalDateTime localDateTime, TimeZone timeZone) {
        OffsetDateTime offsetDateTime = localDateTime.atZone(timeZone.toZoneId()).toOffsetDateTime();
        return removeOffset(offsetDateTime).toLocalDateTime();
    }

    public static OffsetDateTime appendOffset(OffsetDateTime offsetDateTime) {
        return offsetDateTime.plusSeconds(offsetDateTime.getOffset().getTotalSeconds());
    }

    public static OffsetDateTime removeOffset(OffsetDateTime offsetDateTime) {
        return offsetDateTime.minusSeconds(offsetDateTime.getOffset().getTotalSeconds());
    }

}
