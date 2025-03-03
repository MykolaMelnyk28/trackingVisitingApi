package com.trackingVisitingApi.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class StringUtilTest {

    @Test
    void testSplitIds() {
        String strIds = "1,2,3,4,5";
        List<Long> expected = List.of(1L, 2L, 3L, 4L, 5L);
        List<Long> ids = StringUtil.splitIds(strIds);
        assertIterableEquals(expected, ids);
    }

    @Test
    void testSplitIdsWithEmpty() {
        String strIds = "";
        List<Long> expected = List.of();
        List<Long> ids = StringUtil.splitIds(strIds);
        assertIterableEquals(expected, ids);
    }

}