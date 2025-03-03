package com.trackingVisitingApi.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class DateTimeUtilTest {

    static final TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");

    @Test
    void convertWithAppendOffset() {
        LocalDateTime localDateTime = LocalDateTime.parse("2025-01-01T11:00");
        OffsetDateTime expected = localDateTime.atZone(timeZone.toZoneId()).toOffsetDateTime();
        expected = expected.plusSeconds(expected.getOffset().getTotalSeconds());

        OffsetDateTime actual = DateTimeUtil.convertWithAppendOffset(localDateTime, timeZone);

        assertEquals(expected, actual);
    }

    @Test
    void convertWithRemoveOffset() {
        OffsetDateTime offsetDateTime = LocalDateTime.parse("2025-01-01T13:00")
                .atZone(timeZone.toZoneId()).toOffsetDateTime();

        LocalDateTime expected = offsetDateTime
                .minusSeconds(offsetDateTime.getOffset().getTotalSeconds())
                .toLocalDateTime();

        LocalDateTime actual = DateTimeUtil.convertWithRemoveOffset(offsetDateTime);

        assertEquals(expected, actual);
    }

    @Test
    void appendOffsetWithOffsetDateTime() {
        OffsetDateTime offsetDateTime = LocalDateTime.parse("2025-01-01T13:00")
                .atZone(timeZone.toZoneId()).toOffsetDateTime();

        OffsetDateTime expected = offsetDateTime.plusSeconds(offsetDateTime.getOffset().getTotalSeconds());

        OffsetDateTime actual = DateTimeUtil.appendOffset(offsetDateTime);

        assertEquals(expected, actual);
    }

    @Test
    void appendOffsetWithLocalDateTime() {
        LocalDateTime localDateTime =LocalDateTime.parse("2025-01-01T13:00");
        OffsetDateTime offsetDateTime = localDateTime
                .atZone(timeZone.toZoneId())
                .toOffsetDateTime();

        LocalDateTime expected = offsetDateTime.plusSeconds(offsetDateTime.getOffset().getTotalSeconds())
                .toLocalDateTime();

        LocalDateTime actual = DateTimeUtil.appendOffset(localDateTime, timeZone);

        assertEquals(expected, actual);
    }

    @Test
    void removeOffsetWithOffsetDateTime() {
        OffsetDateTime expected = LocalDateTime.parse("2025-01-01T13:00")
                .atZone(timeZone.toZoneId()).toOffsetDateTime();

        OffsetDateTime offsetDateTime = expected.minusSeconds(expected.getOffset().getTotalSeconds());

        OffsetDateTime actual = DateTimeUtil.appendOffset(offsetDateTime);

        assertEquals(expected, actual);
    }

    @Test
    void removeOffsetWithLocalDateTime() {
        LocalDateTime expected = LocalDateTime.parse("2025-01-01T13:00");
        OffsetDateTime offsetDateTime = expected
                .atZone(timeZone.toZoneId())
                .toOffsetDateTime();

        LocalDateTime localDateTime = offsetDateTime.minusSeconds(offsetDateTime.getOffset().getTotalSeconds())
                .toLocalDateTime();

        LocalDateTime actual = DateTimeUtil.appendOffset(localDateTime, timeZone);

        assertEquals(expected, actual);
    }

}