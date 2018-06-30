package com.yoga.ewedding.counselor.model;

import com.yoga.ewedding.counselor.enums.CounselorStatus;
import com.yoga.user.user.model.User;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;

public class CounselorUser extends User {
    @Id
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CounselorStatus status;
    @Column(name = "pid")
    private String pid;
    @Column(name = "pid_front")
    private String pidFront;
    @Column(name = "pid_back")
    private String pidBack;
    @Column(name = "images")
    private String images;
    @Column(name = "reject_reason")
    private String rejectReason;
    @Column(name = "pid_name")
    private String name;
    @Column(name = "pid_nation")
    private String nation;
    @Column(name = "pid_birthday")
    private Date birthday;
    @Column(name = "pid_gender")
    private String pidGender;
    @Column(name = "pid_address")
    private String address;
    @Column(name = "pid_expire")
    private Date expire;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public CounselorStatus getStatus() {
        return status;
    }
    public void setStatus(CounselorStatus status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPidFront() {
        return pidFront;
    }
    public void setPidFront(String pidFront) {
        this.pidFront = pidFront;
    }

    public String getPidBack() {
        return pidBack;
    }
    public void setPidBack(String pidBack) {
        this.pidBack = pidBack;
    }

    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }

    public String getRejectReason() {
        return rejectReason;
    }
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }

    @Override
    public Date getBirthday() {
        return birthday;
    }
    @Override
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPidGender() {
        return pidGender;
    }
    public void setPidGender(String pidGender) {
        this.pidGender = pidGender;
    }

    @Override
    public String getAddress() {
        return address;
    }
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Date getExpire() {
        return expire;
    }

    @Override
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    @Transient
    public String[] getImagesList() {
        if (images == null) return new String[]{};
        else return images.split("\\*");
    }
}