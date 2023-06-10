package org.is.bupt.banksystemmanagement.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.is.bupt.banksystemmanagement.common.Gender;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    @TableId
    private long userId;
    @TableField
    private Date birth;
    @TableField
    private String address;
    @TableField
    private Gender gender;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", birth=" + birth +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                '}';
    }
}
