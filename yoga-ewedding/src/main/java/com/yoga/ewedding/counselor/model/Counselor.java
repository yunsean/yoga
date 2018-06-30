package com.yoga.ewedding.counselor.model;

import com.yoga.ewedding.counselor.enums.CounselorStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ew_counselor")
public class Counselor {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
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
    @Column(name = "prove_token")
    private String proveToken;
    @Column(name = "name")
    private String name;
    @Column(name = "nation")
    private String nation;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "gender")
    private String gender;
    @Column(name = "address")
    private String address;
    @Column(name = "expire")
    private Date expire;

    public Counselor() {
    }
    public Counselor(long id, long tenantId) {
        this.id = id;
        this.tenantId = tenantId;
        this.status = CounselorStatus.filling;
    }
    public void set(String pid, String name, String nation, Date birthday, String gender, String address, Date expire) {
        this.pid = pid;
        this.name = name;
        this.nation = nation;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.expire = expire;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getTenantId() {
        return tenantId;
    }
    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
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

    public String getProveToken() {
        return proveToken;
    }
    public void setProveToken(String proveToken) {
        this.proveToken = proveToken;
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

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Date getExpire() {
        return expire;
    }
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    @Transient
    public String[] getImagesList() {
        if (images == null) return new String[]{};
        else return images.split("\\*");
    }
    @Transactional
    public void setImageList(String[] images) {
        if (images == null) this.images = null;
        else this.images = StringUtils.join(images, "*");
    }
}