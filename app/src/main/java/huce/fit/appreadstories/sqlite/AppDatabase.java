package huce.fit.appreadstories.sqlite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.ChapterRead;
import huce.fit.appreadstories.model.Story;

@Database(entities = {Story.class, Chapter.class, ChapterRead.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "story.dp";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AppDao appDao();
}
