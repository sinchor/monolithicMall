package com.mall.pojo;

import java.util.Date;

public class Users {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.id
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.username
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.password
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.nickname
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String nickname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.realname
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String realname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.face
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String face;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.mobile
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.email
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.sex
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private Integer sex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.birthday
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private Date birthday;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.created_time
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private Date createdTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users.updated_time
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    private Date updatedTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.id
     *
     * @return the value of users.id
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.id
     *
     * @param id the value for users.id
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.username
     *
     * @return the value of users.username
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.username
     *
     * @param username the value for users.username
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.password
     *
     * @return the value of users.password
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.password
     *
     * @param password the value for users.password
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.nickname
     *
     * @return the value of users.nickname
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.nickname
     *
     * @param nickname the value for users.nickname
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.realname
     *
     * @return the value of users.realname
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getRealname() {
        return realname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.realname
     *
     * @param realname the value for users.realname
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.face
     *
     * @return the value of users.face
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getFace() {
        return face;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.face
     *
     * @param face the value for users.face
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setFace(String face) {
        this.face = face == null ? null : face.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.mobile
     *
     * @return the value of users.mobile
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.mobile
     *
     * @param mobile the value for users.mobile
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.email
     *
     * @return the value of users.email
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.email
     *
     * @param email the value for users.email
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.sex
     *
     * @return the value of users.sex
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.sex
     *
     * @param sex the value for users.sex
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.birthday
     *
     * @return the value of users.birthday
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.birthday
     *
     * @param birthday the value for users.birthday
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.created_time
     *
     * @return the value of users.created_time
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.created_time
     *
     * @param createdTime the value for users.created_time
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users.updated_time
     *
     * @return the value of users.updated_time
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users.updated_time
     *
     * @param updatedTime the value for users.updated_time
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}