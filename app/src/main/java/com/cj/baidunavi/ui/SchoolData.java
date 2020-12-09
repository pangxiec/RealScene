package com.cj.baidunavi.ui;

/**
 * Created by 大头 on 2020/5/8.
 */

public class SchoolData
{
    private String listName;
    private int id;


    public SchoolData()
    {

    }

    public SchoolData(String listName,int id)
    {
        this.listName = listName;
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
