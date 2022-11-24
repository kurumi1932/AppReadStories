package huce.fit.appreadstories.sqlite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "story")
public class Story {
    @PrimaryKey
    private int idStory;
    private String nameStory;
    private int sumChapter;
    private String author;
    private String status;
    private String species;
    private String timeUpdate;
    private String image;
    private String introduce;
    private float rate;
    private int view;
    private int like;
    private int sumComment;
    private int isFollow;
    private int chapterReading;

    public Story() {
    }

    public int getIdStory() {
        return idStory;
    }

    public void setIdStory(int idStory) {
        this.idStory = idStory;
    }

    public String getNameStory() {
        return nameStory;
    }

    public void setNameStory(String nameStory) {
        this.nameStory = nameStory;
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

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getSumComment() {
        return sumComment;
    }

    public void setSumComment(int sumComment) {
        this.sumComment = sumComment;
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
}
