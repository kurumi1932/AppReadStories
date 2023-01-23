package huce.fit.appreadstories.api;

import huce.fit.appreadstories.interfaces.ApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://192.168.43.78/appreadstories/";

    public static final String URL_LOGIN = "login.php";
    public static final String URL_REGISTER = "register.php";
    public static final String URL_ACCOUNT = "account.php";

    public static final String URL_STORY = "story.php";
    public static final String URL_STORY_FILTER = "story_filter.php";

    public static final String URL_CHAPTER = "chapter.php";

    public static final String URL_STORY_FOLLOW = "story_follow.php";

    public static final String URL_COMMENT = "comment.php";

    public static final String URL_RATE = "rate.php";

    public static ApiInterface apiInterface(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
