package huce.fit.appreadstories.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "chapter")
public class Chapter {
    @PrimaryKey
    @SerializedName("machuong")
    private int chapterId;
    @SerializedName("matruyen")
    private int storyId;
    @SerializedName("sochuong")
    private String chapterNumber;
    @SerializedName("tenchuong")
    private String chapterName;
    @SerializedName("nguoidang")
    private String poster;
    @SerializedName("thoigiandang")
    private String postDay;
    @SerializedName("noidung")
    private String content;

    @SerializedName("thaydoichuong")
    private int chapterChange;//thay đổi chương

    @SerializedName("so")
    private int number;//phân biệt danh sách chương đã đọc và chương đang đọc

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPostDay() {
        return postDay;
    }

    public void setPostDay(String postDay) {
        this.postDay = postDay;
    }

    public int getChapterChange() {
        return chapterChange;
    }

    public void setChapterChange(int chapterChange) {
        this.chapterChange = chapterChange;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
