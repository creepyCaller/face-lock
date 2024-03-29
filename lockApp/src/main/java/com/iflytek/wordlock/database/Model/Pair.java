package com.iflytek.wordlock.database.Model;

/**
 * Created by 57628 on 2019/5/24.
 */

public class Pair {
    private Integer id;
    private String name;
    private String value;

    public Pair() {}

    public Pair(Integer id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
