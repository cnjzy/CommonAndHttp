package com.library.util;


import com.library.bean.BaseBean;

/**
 * Created by jzy on 2017/5/31.
 */

public class ImageFolder extends BaseBean {
    private String folderId;
    private long fileId;
    private String name;
    private int index;
    private String finaName;
    private String path;
    private int count;
    private boolean isVideo;
    private String date;
    private long duration = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the folderId
     */
    public String getFolderId() {
        return folderId;
    }

    /**
     * @param folderId the folderId to set
     */
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the fileId
     */
    public long getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the finaName
     */
    public String getFinaName() {
        return finaName;
    }

    /**
     * @param finaName the finaName to set
     */
    public void setFinaName(String finaName) {
        this.finaName = finaName;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
