package huce.fit.appreadstories.sqlite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chapter_read")
public class ChapterRead {
    @PrimaryKey(autoGenerate = true)
    int idChapterRead;
    int idStory;
    int idChapter;

    public int getIdChapterRead() {
        return idChapterRead;
    }

    public void setIdChapterRead(int idChapterRead) {
        this.idChapterRead = idChapterRead;
    }

    public int getIdStory() {
        return idStory;
    }

    public void setIdStory(int idStory) {
        this.idStory = idStory;
    }

    public int getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(int idChapter) {
        this.idChapter = idChapter;
    }
}
