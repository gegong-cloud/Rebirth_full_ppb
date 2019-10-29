package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 搜索历史记录
 */
public class SerachHistory implements Serializable {
    private SearchWord[] list;

    public class SearchWord implements Serializable {
        private String q_word;//	string	搜索词

        public String getQ_word() {
            return q_word;
        }

        public void setQ_word(String q_word) {
            this.q_word = q_word;
        }
    }

    public SearchWord[] getList() {
        return list;
    }

    public void setList(SearchWord[] list) {
        this.list = list;
    }
}
