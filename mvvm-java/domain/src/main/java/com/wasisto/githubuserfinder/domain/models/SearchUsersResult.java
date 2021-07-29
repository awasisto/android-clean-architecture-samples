package com.wasisto.githubuserfinder.domain.models;

import com.google.common.base.MoreObjects;

import java.util.List;

public class SearchUsersResult {

    public static class Item {

        private String login;

        private String avatarUrl;

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

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("login", login)
                    .add("avatarUrl", avatarUrl)
                    .toString();
        }
    }

    private long totalCount;

    private List<Item> items;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("totalCount", totalCount)
                .add("items", items)
                .toString();
    }
}
