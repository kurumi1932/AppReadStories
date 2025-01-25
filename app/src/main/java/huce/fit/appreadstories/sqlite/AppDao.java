package huce.fit.appreadstories.sqlite;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.ChapterRead;
import huce.fit.appreadstories.model.Story;

@Dao
public interface AppDao {
//    ----------------Story----------------
    @Insert
    void insertStory(Story... story);

    @Update
    void updateStory(Story... story);

    @Query("UPDATE story SET chapterReading=:chapterReadId WHERE storyId = :storyId")
    void updateChapterReadId(int storyId, int chapterReadId);

    @Query("DELETE FROM story WHERE storyId = :storyId")
    void deleteStory(int storyId);

    @Query("SELECT * FROM story")
    List<Story> getAllStory();

    @Query("SELECT * FROM story WHERE storyId = :storyId")
    Story getStory(int storyId);

//    ----------------Chapter----------------
    @Insert
    void insertChapter(Chapter... chapter);

    @Query("DELETE FROM chapter WHERE storyId = :storyId")
    void deleteChapter(int storyId);

    @Query("SELECT * FROM chapter WHERE storyId = :storyId")
    List<Chapter> getChapterList(int storyId);

    @Query("SELECT COUNT(*) FROM chapter WHERE storyId = :storyId")
    int getSumChapter(int storyId);

    @Query("SELECT * FROM chapter WHERE storyId = :storyId AND chapterId = :chapterId")
    Chapter getChapter(int storyId, int chapterId);

    @Query("SELECT chapterId FROM chapter WHERE storyId = :storyId ORDER BY chapterId ASC LIMIT 1")
    int getChapterFirst(int storyId);

    @Query("SELECT chapterId FROM chapter WHERE  storyId = :storyId AND chapterId < :chapterId AND chapterId >= :chapterFirstId ORDER BY chapterId DESC LIMIT 1")
    int getPreviousChapter(int storyId, int chapterId, int chapterFirstId);

    @Query("SELECT chapterId FROM chapter WHERE storyId = :storyId ORDER BY chapterId DESC LIMIT 1")
    int getChapterFinal(int storyId);

    @Query("SELECT chapterId FROM chapter WHERE  storyId = :storyId AND chapterId > :chapterId AND chapterId <= :chapterFinalId ORDER BY chapterId ASC LIMIT 1")
    int getNextChapter(int storyId, int chapterId, int chapterFinalId);

//    ----------------Chapter Read----------------
    @Insert
    void insertChapterRead(ChapterRead... chapterRead);

    @Query("DELETE FROM chapter_read WHERE storyId = :storyId")
    void deleteChapterRead(int storyId);

    @Query("SELECT * FROM chapter_read WHERE storyId = :storyId")
    List<ChapterRead> getChapterReadList(int storyId);

    @Query("SELECT COUNT(*) FROM chapter_read WHERE storyId = :storyId")
    int getSumChapterRead(int storyId);

    @Query("SELECT * FROM chapter_read WHERE storyId = :storyId AND chapterId = :chapterId")
    ChapterRead checkChapterRead(int storyId, int chapterId);
}
