package com.example.hello.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultBean {


    /**
     * loadingPic :
     * bufferPic :
     * loadingPicFS :
     * bufferPicFS :
     * subed : false
     * data : {"id":5404646,"name":"光年之外","artistId":7763,"artistName":"G.E.M.邓紫棋","briefDesc":" 邓紫棋深情献唱演绎史诗爱情","desc":"年度科幻冒险巨制《太空旅客》将于2017年1月13日正式登陆内地大银幕。新生代天后邓紫棋创作并演唱电影中国区主题曲《光年之外》。邓紫棋深情献唱，吟咏着史诗级太空之旅的浪漫和真挚，道尽两位太空旅客之间令人动容的纯粹爱恋。","cover":"http://p1.music.126.net/T5u6tvoe6_AJkbOBP6jsIQ==/18635622580858178.jpg","coverId":18635622580858178,"playCount":5971291,"subCount":63572,"shareCount":6430,"likeCount":49572,"commentCount":4453,"duration":236000,"nType":0,"publishTime":"2016-12-30","brs":{"240":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/6b492b3db4d45fd2687a91d10a474f43.mp4?wsSecret=1d379d4b908fb9c4acffdc5c6c5a8efc&wsTime=1538816298","480":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/66111ccae007d17090e4b8338f1ef222.mp4?wsSecret=25e6921bc26a5c2887100d8c8ab04da1&wsTime=1538816298","720":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/3b157a011b3349ad30fea9aa32967fc8.mp4?wsSecret=ecab07f465b95b80e8c0462caf8fdabb&wsTime=1538816298","1080":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/41a8e7815c380b8840f3ec86e3e44e48.mp4?wsSecret=459b9a99b7c08839f41249e2167ac6f6&wsTime=1538816298"},"artists":[{"id":7763,"name":"G.E.M.邓紫棋"}],"isReward":false,"commentThreadId":"R_MV_5_5404646"}
     * code : 200
     */

    private String loadingPic;
    private String bufferPic;
    private String loadingPicFS;
    private String bufferPicFS;
    private boolean subed;
    private DataBean data;
    private int code;

    public String getLoadingPic() {
        return loadingPic;
    }

    public void setLoadingPic(String loadingPic) {
        this.loadingPic = loadingPic;
    }

    public String getBufferPic() {
        return bufferPic;
    }

    public void setBufferPic(String bufferPic) {
        this.bufferPic = bufferPic;
    }

    public String getLoadingPicFS() {
        return loadingPicFS;
    }

    public void setLoadingPicFS(String loadingPicFS) {
        this.loadingPicFS = loadingPicFS;
    }

    public String getBufferPicFS() {
        return bufferPicFS;
    }

    public void setBufferPicFS(String bufferPicFS) {
        this.bufferPicFS = bufferPicFS;
    }

    public boolean isSubed() {
        return subed;
    }

    public void setSubed(boolean subed) {
        this.subed = subed;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * id : 5404646
         * name : 光年之外
         * artistId : 7763
         * artistName : G.E.M.邓紫棋
         * briefDesc :  邓紫棋深情献唱演绎史诗爱情
         * desc : 年度科幻冒险巨制《太空旅客》将于2017年1月13日正式登陆内地大银幕。新生代天后邓紫棋创作并演唱电影中国区主题曲《光年之外》。邓紫棋深情献唱，吟咏着史诗级太空之旅的浪漫和真挚，道尽两位太空旅客之间令人动容的纯粹爱恋。
         * cover : http://p1.music.126.net/T5u6tvoe6_AJkbOBP6jsIQ==/18635622580858178.jpg
         * coverId : 18635622580858178
         * playCount : 5971291
         * subCount : 63572
         * shareCount : 6430
         * likeCount : 49572
         * commentCount : 4453
         * duration : 236000
         * nType : 0
         * publishTime : 2016-12-30
         * brs : {"240":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/6b492b3db4d45fd2687a91d10a474f43.mp4?wsSecret=1d379d4b908fb9c4acffdc5c6c5a8efc&wsTime=1538816298","480":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/66111ccae007d17090e4b8338f1ef222.mp4?wsSecret=25e6921bc26a5c2887100d8c8ab04da1&wsTime=1538816298","720":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/3b157a011b3349ad30fea9aa32967fc8.mp4?wsSecret=ecab07f465b95b80e8c0462caf8fdabb&wsTime=1538816298","1080":"http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/41a8e7815c380b8840f3ec86e3e44e48.mp4?wsSecret=459b9a99b7c08839f41249e2167ac6f6&wsTime=1538816298"}
         * artists : [{"id":7763,"name":"G.E.M.邓紫棋"}]
         * isReward : false
         * commentThreadId : R_MV_5_5404646
         */

        private int id;
        private String name;
        private int artistId;
        private String artistName;
        private String briefDesc;
        private String desc;
        private String cover;
        private long coverId;
        private int playCount;
        private int subCount;
        private int shareCount;
        private int likeCount;
        private int commentCount;
        private int duration;
        private int nType;
        private String publishTime;
        private BrsBean brs;
        private boolean isReward;
        private String commentThreadId;
        private List<ArtistsBean> artists;

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

        public int getArtistId() {
            return artistId;
        }

        public void setArtistId(int artistId) {
            this.artistId = artistId;
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public long getCoverId() {
            return coverId;
        }

        public void setCoverId(long coverId) {
            this.coverId = coverId;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public int getSubCount() {
            return subCount;
        }

        public void setSubCount(int subCount) {
            this.subCount = subCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getNType() {
            return nType;
        }

        public void setNType(int nType) {
            this.nType = nType;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public BrsBean getBrs() {
            return brs;
        }

        public void setBrs(BrsBean brs) {
            this.brs = brs;
        }

        public boolean isIsReward() {
            return isReward;
        }

        public void setIsReward(boolean isReward) {
            this.isReward = isReward;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public void setCommentThreadId(String commentThreadId) {
            this.commentThreadId = commentThreadId;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public static class BrsBean {
            /**
             * 240 : http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/6b492b3db4d45fd2687a91d10a474f43.mp4?wsSecret=1d379d4b908fb9c4acffdc5c6c5a8efc&wsTime=1538816298
             * 480 : http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/66111ccae007d17090e4b8338f1ef222.mp4?wsSecret=25e6921bc26a5c2887100d8c8ab04da1&wsTime=1538816298
             * 720 : http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/3b157a011b3349ad30fea9aa32967fc8.mp4?wsSecret=ecab07f465b95b80e8c0462caf8fdabb&wsTime=1538816298
             * 1080 : http://vodkgeyttp8.vod.126.net/cloudmusic/IjBiITkyMTEgMSI2JCAwZA==/mv/5404646/41a8e7815c380b8840f3ec86e3e44e48.mp4?wsSecret=459b9a99b7c08839f41249e2167ac6f6&wsTime=1538816298
             */

            @SerializedName("240")
            private String _$240;
            @SerializedName("480")
            private String _$480;
            @SerializedName("720")
            private String _$720;
            @SerializedName("1080")
            private String _$1080;

            public String get_$240() {
                return _$240;
            }

            public void set_$240(String _$240) {
                this._$240 = _$240;
            }

            public String get_$480() {
                return _$480;
            }

            public void set_$480(String _$480) {
                this._$480 = _$480;
            }

            public String get_$720() {
                return _$720;
            }

            public void set_$720(String _$720) {
                this._$720 = _$720;
            }

            public String get_$1080() {
                return _$1080;
            }

            public void set_$1080(String _$1080) {
                this._$1080 = _$1080;
            }
        }

        public static class ArtistsBean {
            /**
             * id : 7763
             * name : G.E.M.邓紫棋
             */

            private int id;
            private String name;

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
        }
    }
}
