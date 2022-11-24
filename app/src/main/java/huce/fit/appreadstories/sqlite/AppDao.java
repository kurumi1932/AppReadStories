package huce.fit.appreadstories.sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void insertStory(Story... story);

    @Delete
    void deleteStory(Story story);

    @Query("SELECT * FROM story")
    List<Story> getAllStory();

    @Query("SELECT * FROM story WHERE idStory = :id")
    Story getStory(int id);

    @Insert
    void insertChapter(Chapter... chapter);

    @Delete
    void deleteChapter(Chapter chapter);

    @Query("SELECT * FROM chapter WHERE idStory = :id")
    List<Chapter> getAllChapter(int id);

    @Query("SELECT * FROM chapter WHERE idStory = :id1 AND idChapter=:id2")
    Chapter getChapter(int id1, int id2);

    @Insert
    void insertChapterRead(ChapterRead... chapterRead);

    @Delete
    void deleteChapterRead(ChapterRead chapterRead);

    @Query("SELECT * FROM chapter_read WHERE idStory = :id")
    List<Chapter> getAllChapterRead(int id);

}
