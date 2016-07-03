package com.hannan.kevin.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by khannan on 7/2/16.
 */
public class ItemsList {

    @SerializedName("items")
    ArrayList<PodcastItem> itemsList;

    public int getSize() {
        return itemsList.size();
    }

    public ArrayList<PodcastItem> getItemsList() {
        return itemsList;
    }


}
