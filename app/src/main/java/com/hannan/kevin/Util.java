package com.hannan.kevin;

/**
 * Created by kehannan on 8/1/16.
 */
public class Util {

    public Util(){
    }

    public static String formatDuration(String durationString) {

        if (durationString != null) {

            int duration = Integer.parseInt(durationString);
            int mins = duration / 60;
            int seconds = duration % 60;
            return (mins + " min " + seconds + " sec");
        } else {
            return "";
        }
    }
}
