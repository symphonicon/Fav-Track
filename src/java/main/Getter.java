package main;

import Parsers.*;
import MYSHOWS.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by symph on 17.07.2017.
 */
public class Getter {
    private static String showTitle, poster, seasonNumber, episodeNumber, episodeName, filename;
    private int duration, position;

    public static void main (String[] args){
        new Getter().getData();
        showTitle = new Shows().getShowname();
        episodeNumber = new Getter().getEpisode();
        System.out.println(showTitle);
        System.out.println(episodeNumber);
    }

    void getDataMPC(){
        MPC.getData();
        filename = new MPC().getFilename();
        switch(new Shows().searchByFile(filename)){
            case "ok":
                showTitle = new Shows().getShowname();
                seasonNumber = new Shows().getSeason();
                episodeNumber = new Shows().getEpisode();
                break;
            case "error":
                new Parser().parseFilename(filename);
                seasonNumber = new Parser().getSeason();
                episodeNumber = new Parser().getEpisode();
                showTitle = new Parser().getShowName();
                break;
        }
        duration = new MPC().getDuration();
        position = new MPC().getPosition();
    }
/*
    void getDataVLC(){
        VLC.getData();
        filename = new VLC().getFilename();
        if(new Shows().searchByFile(filename)=="ok") {
            showTitle = Shows.title;
            seasonNumber = Shows.seasonNumber;
            episodeNumber = Shows.episodeNumber;
        } else {
            if(new VLC().getEpisode()!=null){
                showTitle = new VLC().getShowname();
                episodeNumber = new VLC().getEpisode();
                seasonNumber = new VLC().getSeason();
            } else {
                new Parser().parseFilename(filename);
                seasonNumber = new Parser().getSeason();
                episodeNumber = new Parser().getEpisode();
                showTitle = new Parser().getShowName();
            }
        }
        duration = new VLC().getDuration();
        position = new VLC().getPosition();
    }
*/
    public void getData(){
        String prefPlayer = new GetPropetries().getUserPref_Player();
        switch(prefPlayer){
            case "MPC":
                getDataMPC();
                break;
            case "VLC":
                //getDataVLC();
                break;
        }
    }

    public String getShowname(){
        return this.showTitle;
    }
    public String getEpisode(){
        return this.episodeNumber;
    }
    public String getSeason(){
        return this.seasonNumber;
    }
    public int getDuration(){
        return this.duration;
    }
    public int getposition(){
        return this.position;
    }

}
