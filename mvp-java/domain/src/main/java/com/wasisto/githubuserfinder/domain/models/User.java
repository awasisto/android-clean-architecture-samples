package com.wasisto.githubuserfinder.domain.models;

import com.google.common.base.MoreObjects;

public class User {

    private String login;

    private String avatarUrl;

    private String name;

    private String company;

    private String blog;

    private String location;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("login", login)
                .add("avatarUrl", avatarUrl)
                .add("name", name)
                .add("company", company)
                .add("blog", blog)
                .add("location", location)
                .toString();
    }
}
