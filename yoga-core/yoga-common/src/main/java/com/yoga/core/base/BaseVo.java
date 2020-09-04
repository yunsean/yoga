package com.yoga.core.base;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseVo {
    public interface Converter<PO, VO> {
        void convert(PO from, VO to);
    }
    public interface Converter2<PO, VO> {
        VO convert(PO from);
    }
    public static <PO, VO> VO copy(PO from, Class<VO> clazz, Converter<PO, VO> converter) {
        if (from == null) return null;
        try {
            VO result = clazz.newInstance();
            BeanUtils.copyProperties(from, result);
            if (converter != null) converter.convert(from, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <PO, VO> VO copy(PO from, Class<VO> clazz, Converter2<PO, VO> converter) {
        if (from == null) return null;
        try {
            VO result = converter.convert(from);
            if (result != null) return result;
            result = clazz.newInstance();
            BeanUtils.copyProperties(from, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <PO, VO> List<VO> copys(Collection<PO> fromList, Class<VO> clazz, Converter<PO, VO> converter) {
        if (CollectionUtils.isEmpty(fromList)) return new ArrayList<>();
        List<VO> result = new ArrayList<VO>();
        for (PO from : fromList) {
            result.add(copy(from, clazz, converter));
        }
        return result;
    }
    public static <PO, VO> List<VO> copys(Collection<PO> fromList, Class<VO> clazz, Converter2<PO, VO> converter) {
        if (CollectionUtils.isEmpty(fromList)) return new ArrayList<>();
        List<VO> result = new ArrayList<VO>();
        for (PO from : fromList) {
            result.add(copy(from, clazz, converter));
        }
        return result;
    }
    public static <PO, VO> VO copy(PO from, Class<VO> clazz) {
        if (from == null) return null;
        try {
            VO result = clazz.newInstance();
            BeanUtils.copyProperties(from, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <PO, VO> List<VO> copys(Collection<PO> fromList, Class<VO> clazz) {
        if (CollectionUtils.isEmpty(fromList)) return new ArrayList<>();
        List<VO> result = new ArrayList<VO>();
        for (PO from : fromList) {
            result.add(copy(from, clazz));
        }
        return result;
    }
}
