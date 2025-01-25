package huce.fit.appreadstories.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "chapter_read")
public class ChapterRead {
    @PrimaryKey(autoGenerate = true)
    int chapterReadId;

    @SerializedName("matruyen")
    int storyId;

    @SerializedName("machuong")
    int chapterId;

    public int getChapterReadId() {
        return chapterReadId;
    }

    public void setChapterReadId(int chapterReadId) {
        this.chapterReadId = chapterReadId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }
}
