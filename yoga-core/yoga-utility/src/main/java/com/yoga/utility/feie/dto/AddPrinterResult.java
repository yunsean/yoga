package com.yoga.utility.feie.dto;

import java.util.List;

public class AddPrinterResult {
    private List<String> ok;
    private List<String> no;

    public List<String> getOk() {
        return ok;
    }

    public void setOk(List<String> ok) {
        this.ok = ok;
    }

    public List<String> getNo() {
        return no;
    }

    public void setNo(List<String> no) {
        this.no = no;
    }
}
