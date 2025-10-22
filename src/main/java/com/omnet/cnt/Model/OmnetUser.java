package com.omnet.cnt.Model;

import javax.persistence.*;


@Entity
@Table(name = "OMNET_USERS")
public class OmnetUser {

    @Id
    @Column(name = "USER_ID")
    private String userId;
    private String userLastName;
    private String userFirstName;
    private String userMidName;
    private String userSuffixName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserMidName() {
        return userMidName;
    }

    public void setUserMidName(String userMidName) {
        this.userMidName = userMidName;
    }

    public String getUserSuffixName() {
        return userSuffixName;
    }

    public void setUserSuffixName(String userSuffixName) {
        this.userSuffixName = userSuffixName;
    }
    
}