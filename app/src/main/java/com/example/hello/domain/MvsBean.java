package com.example.hello.domain;

import java.util.List;

public class MvsBean extends SearchBean.ResultBean.MvsBean{

    /**
     * id : 5404646
     * cover : http://p3.music.126.net/T5u6tvoe6_AJkbOBP6jsIQ==/18635622580858178.jpg
     * name : 光年之外
     * playCount : 84
     * briefDesc :  邓紫棋深情献唱演绎史诗爱情
     * desc : null
     * artistName : G.E.M.邓紫棋
     * artistId : 7763
     * duration : 236000
     * mark : 0
     * artists : [{"id":7763,"name":"G.E.M.邓紫棋","alias":["G.E.M.","邓紫棋","邓紫棋","邓紫琪"],"transNames":null}]
     */

    private int id;
    private String cover;
    private String name;
    private int playCount;
    private String briefDesc;
    private Object desc;
    private String artistName;
    private int artistId;
    private int duration;
    private int mark;
    private List<SearchBean.ResultBean.MvsBean.ArtistsBean> artists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getBriefDesc() {
        return briefDesc;
    }

    public void setBriefDesc(String briefDesc) {
        this.briefDesc = briefDesc;
    }

    public Object getDesc() {
        return desc;
    }

    public void setDesc(Object desc) {
        this.desc = desc;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public List<SearchBean.ResultBean.MvsBean.ArtistsBean> getArtists() {
        return artists;
    }

    public void setArtists(List<SearchBean.ResultBean.MvsBean.ArtistsBean> artists) {
        this.artists = artists;
    }

    public static class ArtistsBean {
        /**
         * id : 7763
         * name : G.E.M.邓紫棋
         * alias : ["G.E.M.","邓紫棋","邓紫棋","邓紫琪"]
         * transNames : null
         */

        private int id;
        private String name;
        private Object transNames;
        private List<String> alias;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getTransNames() {
            return transNames;
        }

        public void setTransNames(Object transNames) {
            this.transNames = transNames;
        }

        public List<String> getAlias() {
            return alias;
        }

        public void setAlias(List<String> alias) {
            this.alias = alias;
        }
    }

}
