package huce.fit.appreadstories.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {

    private lateinit var retrofit: Retrofit
    companion object {
        private const val BASE_URL = "http://192.168.1.3/appstory/"

        const val URL_LOGIN: String = "login.php"
        const val URL_REGISTER = "register.php"
        const val URL_ACCOUNT = "account.php"

        const val URL_STORY = "story.php"
        const val URL_STORY_FILTER = "story_filter.php"

        const val URL_CHAPTER = "chapter.php"

        const val URL_STORY_FOLLOW = "story_follow.php"

        const val URL_COMMENT = "comment.php"

        const val URL_RATE = "rate.php"
    }

    fun apiInterface(): ApiInterface {
        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        return retrofit.create(ApiInterface::class.java)
    }
}