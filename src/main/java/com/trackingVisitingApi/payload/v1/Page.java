package com.trackingVisitingApi.payload.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page<T> {

    public static <T> Page<T> empty() {
        return new Page<>(List.of(), 0);
    }

    private List<T> data;
    private int count;

    public Page(org.springframework.data.domain.Page<T> page) {
        this.data = new ArrayList<>(page.getContent());
        this.count = (int)page.getTotalElements();
    }

}
