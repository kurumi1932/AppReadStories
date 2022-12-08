package huce.fit.appreadstories.sqlite;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppDao {
//    ----------------Story----------------
    @Insert
    void insertStory(Story... story);

    @Update
    void updateStory(Story... story);

    @Query("UPDATE story SET chapterReading=:idChapterRead WHERE idStory = :idStory")
    void updateIdChapterRead(int idStory, int idChapterRead);

    @Query("DELETE FROM story WHERE idStory = :idStory")
    void deleteStory(int idStory);

    @Query("SELECT * FROM story")
    List<Story> getAllStory();

    @Query("SELECT * FROM story WHERE idStory = :idStory")
    Story getStory(int idStory);

//    ----------------Chapter----------------
    @Insert
    void insertChapter(Chapter... chapter);

    @Query("DELETE FROM chapter WHERE idStory = :idStory")
    void deleteChapter(int idStory);

    @Query("SELECT * FROM chapter WHERE idStory = :idStory")
    List<Chapter> getAllChapter(int idStory);

    @Query("SELECT * FROM chapter WHERE idStory = :idStory AND numberChapter LIKE '%' || :number || '%'")
    List<Chapter> getAllChapterByNumberChapter(int idStory, String number);

    @Query("SELECT * FROM chapter WHERE idStory = :idStory AND idChapter = :idChapter")
    Chapter getChapter(int idStory, int idChapter);

    @Query("SELECT idChapter FROM chapter WHERE idStory = :idStory ORDER BY idChapter ASC LIMIT 1")
    int getChapterFirst(int idStory);

    @Query("SELECT idChapter FROM chapter WHERE  idStory = :idStory AND idChapter < :idChapter AND idChapter >= :idChapterFirst ORDER BY idChapter DESC LIMIT 1")
    int getPreviousChapter(int idStory, int idChapter, int idChapterFirst);

    @Query("SELECT idChapter FROM chapter WHERE idStory = :idStory ORDER BY idChapter DESC LIMIT 1")
    int getChapterFinal(int idStory);

    @Query("SELECT idChapter FROM chapter WHERE  idStory = :idStory AND idChapter > :idChapter AND idChapter <= :idChapterFinal ORDER BY idChapter ASC LIMIT 1")
    int getNextChapter(int idStory, int idChapter, int idChapterFinal);

//    ----------------Chapter Read----------------
    @Insert
    void insertChapterRead(ChapterRead... chapterRead);

    @Query("DELETE FROM chapter_read WHERE idStory = :idStory")
    void deleteChapterRead(int idStory);

    @Query("SELECT * FROM chapter_read WHERE idStory = :idStory")
    List<ChapterRead> getAllChapterRead(int idStory);

    @Query("SELECT * FROM chapter_read WHERE idStory = :idStory AND idChapter = :idChapter")
    ChapterRead checkChapterRead(int idStory, int idChapter);
}
