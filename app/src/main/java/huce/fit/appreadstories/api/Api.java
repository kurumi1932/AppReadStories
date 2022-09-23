package huce.fit.appreadstories.api;

import huce.fit.appreadstories.interfaces.ApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit = null;
    //đt: 192.168.43.78
    //wifi: 192.168.1.21
    private static final String BASE_URL = "http://192.168.1.21/appreadstories/";

    public static final String URL_LOGIN = "login.php";//oke
    public static final String URL_REGISTER = "register.php";//oke
    public static final String URL_ACCOUNT = "account.php";//oke

    public static final String URL_STORY = "story.php";//oke
    public static final String URL_STORY_FILTER = "story_filter.php";

    public static final String URL_CHAPTER = "chapter.php";//oke

    public static final String URL_STORY_FOLLOW = "story_follow.php";//oke

    public static final String URL_COMMENT = "comment.php";

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
