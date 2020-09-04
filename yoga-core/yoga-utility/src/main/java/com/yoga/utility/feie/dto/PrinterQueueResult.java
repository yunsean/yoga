package com.yoga.utility.feie.dto;

import java.util.List;

public class PrinterQueueResult {
    private int print;
    private int waiting;

    public int getPrint() {
        return print;
    }
    public void setPrint(int print) {
        this.print = print;
    }

    public int getWaiting() {
        return waiting;
    }
    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }
}
