package huce.fit.appreadstories.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "story")
public class Story {
    @PrimaryKey
    @SerializedName("matruyen")
    private int storyId;
    @SerializedName("tentruyen")
    private String storyName;
    @SerializedName("tongchuong")
    private int sumChapter;
    @SerializedName("tacgia")
    private String author;
    @SerializedName("gioihantuoi")
    private int ageLimit;
    @SerializedName("trangthai")
    private String status;
    @SerializedName("theloai")
    private String species;
    @SerializedName("thoigiancapnhat")
    private String timeUpdate;
    @SerializedName("anh")
    private String image;
    @SerializedName("gioithieu")
    private String introduce;
    @SerializedName("diemdanhgia")
    private float ratePoint;
    @SerializedName("luotxem")
    private int totalViews;
    @SerializedName("luotthich")
    private int totalLikes;
    @SerializedName("luotbinhluan")
    private int totalComments;
    @SerializedName("storySuccess")
    private int storySuccess;
    private int isFollow;
    private int chapterReading;
    private int newChapter;

    public Story() {
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public int getSumChapter() {
        return sumChapter;
    }

    public void setSumChapter(int sumChapter) {
        this.sumChapter = sumChapter;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(String timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public float getRatePoint() {
        return ratePoint;
    }

    public void setRatePoint(float ratePoint) {
        this.ratePoint = ratePoint;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getStorySuccess() {
        return storySuccess;
    }

    public void setStorySuccess(int storySuccess) {
        this.storySuccess = storySuccess;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getChapterReading() {
        return chapterReading;
    }

    public void setChapterReading(int chapterReading) {
        this.chapterReading = chapterReading;
    }

    public int getNewChapter() {
        return newChapter;
    }

    public void setNewChapter(int newChapter) {
        this.newChapter = newChapter;
    }
}
