package edu.gvsu.cis.campbjos.imgine.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Descriptions {

    @SerializedName("file_descriptions")
    @Expose
    private final Map<String, String> fileDescriptions;

    public Descriptions() {
        fileDescriptions = new HashMap<>();
    }

    public String getFileDescription(String filename) {
        return fileDescriptions.getOrDefault(filename, "");
    }
}
