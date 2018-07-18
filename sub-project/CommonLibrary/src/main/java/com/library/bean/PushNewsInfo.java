package com.library.bean;

import java.util.List;

/**
 * @author jzy
 * created at 2018/6/4
 */
public class PushNewsInfo extends BaseBean {
    private String newsId;
    private String url;
    private List<String> images;
    private String shareUrl;
    private String title;
    private String duration;
    private String playCounts;
    private String updateTime;
    private String source;
    private String replyNum;
    private String collectionType;
    private boolean isAd;
    private String collectionId;
    private String moduleId;
    private int type; // news / video
    private String newsReply;
    private int videoDuration;
    private int watchSecond;
    private String dec;
    private String imageUrl;
    private String fullText;
    private boolean isH5Url;
    // 频道信息
    private String mSourceID;
    private String mSourceChannelID;
    private String mNewsType;

    public boolean isH5Url() {
        return isH5Url;
    }

    public void setH5Url(boolean h5Url) {
        isH5Url = h5Url;
    }

    public String getmSourceID() {
        return mSourceID;
    }

    public void setmSourceID(String mSourceID) {
        this.mSourceID = mSourceID;
    }

    public String getmSourceChannelID() {
        return mSourceChannelID;
    }

    public void setmSourceChannelID(String mSourceChannelID) {
        this.mSourceChannelID = mSourceChannelID;
    }

    public String getmNewsType() {
        return mNewsType;
    }

    public void setmNewsType(String mNewsType) {
        this.mNewsType = mNewsType;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlayCounts() {
        return playCounts;
    }

    public void setPlayCounts(String playCounts) {
        this.playCounts = playCounts;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getWatchSecond() {
        return watchSecond;
    }

    public void setWatchSecond(int watchSecond) {
        this.watchSecond = watchSecond;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsReply() {
        return newsReply;
    }

    public void setNewsReply(String newsReply) {
        this.newsReply = newsReply;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
}