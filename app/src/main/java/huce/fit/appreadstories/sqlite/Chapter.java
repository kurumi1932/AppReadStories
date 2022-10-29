package huce.fit.appreadstories.sqlite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chapter")
public class Chapter {
    @PrimaryKey
    private int idChapter;
    private int idStory;
    private String numberChapter;
    private String nameChapter;
    private String content;
    private String poster;
    private String timePost;
    private int idChapterReading;

    public int getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(int idChapter) {
        this.idChapter = idChapter;
    }

    public int getIdStory() {
        return idStory;
    }

    public void setIdStory(int idStory) {
        this.idStory = idStory;
    }

    public String getNumberChapter() {
        return numberChapter;
    }

    public void setNumberChapter(String numberChapter) {
        this.numberChapter = numberChapter;
    }

    public String getNameChapter() {
        return nameChapter;
    }

    public void setNameChapter(String nameChapter) {
        this.nameChapter = nameChapter;
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

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public int getIdChapterReading() {
        return idChapterReading;
    }

    public void setIdChapterReading(int idChapterReading) {
        this.idChapterReading = idChapterReading;
    }
}
