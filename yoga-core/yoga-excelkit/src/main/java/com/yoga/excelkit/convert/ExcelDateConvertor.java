package com.yoga.excelkit.convert;

import com.yoga.core.utils.DateUtil;
import com.yoga.excelkit.exception.ExcelKitReadConverterException;

import java.util.Date;

public class ExcelDateConvertor implements WriteConverter {
    @Override
    public String convert(Object value) throws ExcelKitReadConverterException {
        if (value == null) return null;
        if (value instanceof Integer) return DateUtil.format(new Date((Integer)value * 1000L), "yyyy-MM-dd");
        try {
            Long l = Long.parseLong(value.toString());
            if (l == 0) return null;
            return DateUtil.format(new Date(l * 1000L), "yyyy-MM-dd");
        } catch (Exception ex) {
            return value.toString();
        }
    }
}
