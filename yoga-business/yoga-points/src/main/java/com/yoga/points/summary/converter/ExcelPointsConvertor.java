package com.yoga.points.summary.converter;

import com.yoga.excelkit.convert.WriteConverter;
import com.yoga.excelkit.exception.ExcelKitReadConverterException;

public class ExcelPointsConvertor implements WriteConverter {
    @Override
    public String convert(Object value) throws ExcelKitReadConverterException {
        if (value == null) return null;
        try {
            return String.format("%.1f", Integer.parseInt(value.toString()) / 100F);
        } catch (Exception ex) {
            return value.toString();
        }
    }
}
