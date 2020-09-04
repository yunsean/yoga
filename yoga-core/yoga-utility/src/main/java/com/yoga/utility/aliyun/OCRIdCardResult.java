package com.yoga.utility.aliyun;

public class OCRIdCardResult {
    private String number;
    private String sex;
    private String birthday;
    private String nation;
    private String name;
    private String address;

    public OCRIdCardResult(String number, String sex, String birthday, String nation, String name, String address) {
        this.number = number;
        this.sex = sex;
        this.birthday = birthday;
        this.nation = nation;
        this.name = name;
        this.address = address;
    }

    public String getNumber() {
        return number;
    }
    public String getSex() {
        return sex;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getNation() {
        return nation;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
}
