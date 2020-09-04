package com.yoga.excelkit.convert;

import com.yoga.excelkit.exception.ExcelKitWriteConverterException;

public class ExcelBooleanConvertor implements WriteConverter {

    @Override
    public String convert(Object value) throws ExcelKitWriteConverterException {
        if (value == null) return null;
        String svalue = value.toString();
        if (svalue.equalsIgnoreCase("true")) return "√";
        else if (svalue.equalsIgnoreCase("false")) return "×";
        return svalue;
    }
}
