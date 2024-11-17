package com.project.common.service;

import com.project.common.response.PageRes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseService {

    @Autowired
    private ModelMapper modelMapper;

    protected <D, R> R convertSingleObject(D source, Class<R> targetClass) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, targetClass);
    }

    protected <D, R> List<R> convertObjectList(List<D> sourceList, Class<R> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(source -> modelMapper.map(source, targetClass))
                .toList();
    }

    protected <T> PageRes<T> setPageResponse(List<T> resList, int count) {
        PageRes<T> res = new PageRes<>();
        res.setData(resList);
        res.setCount(count);

        return res;
    }
}
