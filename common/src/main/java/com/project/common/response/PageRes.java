package com.project.common.response;

import lombok.Data;

import java.util.List;

@Data
public class PageRes<T> {

    private List<T> data;

    private int count;
}

