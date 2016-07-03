package com.hannan.kevin.model;

import com.hannan.kevin.model.Image;
import com.hannan.kevin.model.Audio;

import java.util.ArrayList;
import java.util.List;

public class Links {

    private List<Audio> audio = new ArrayList<Audio>();
    private List<Image> image = new ArrayList<Image>();


    /**
     *
     * @return
     * The audio
     */
    public List<Audio> getAudio() {
        return audio;
    }

    /**
     *
     * @param audio
     * The audio
     */
    public void setAudio(List<Audio> audio) {
        this.audio = audio;
    }

    /**
     *
     * @return
     * The image
     */
    public List<Image> getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(List<Image> image) {
        this.image = image;
    }
}