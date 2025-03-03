package com.trackingVisitingApi.util;

import java.util.Arrays;
import java.util.List;

public final class StringUtil {

    private StringUtil() {}

    public static List<Long> splitIds(String ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
    }



}
