package huce.fit.appreadstories.api

import huce.fit.appreadstories.api.Api.Companion.URL_ACCOUNT
import huce.fit.appreadstories.api.Api.Companion.URL_CHAPTER
import huce.fit.appreadstories.api.Api.Companion.URL_COMMENT
import huce.fit.appreadstories.api.Api.Companion.URL_LOGIN
import huce.fit.appreadstories.api.Api.Companion.URL_RATE
import huce.fit.appreadstories.api.Api.Companion.URL_REGISTER
import huce.fit.appreadstories.api.Api.Companion.URL_STORY
import huce.fit.appreadstories.api.Api.Companion.URL_STORY_FILTER
import huce.fit.appreadstories.api.Api.Companion.URL_STORY_FOLLOW
import huce.fit.appreadstories.model.Account
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.model.Comment
import huce.fit.appreadstories.model.Rate
import huce.fit.appreadstories.model.Story
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @FormUrlEncoded
    @POST(URL_LOGIN)
    fun login(
        @Field("taikhoan") username: String,
        @Field("matkhau") password: String
    ): Call<Account>

    @FormUrlEncoded
    @POST(URL_REGISTER)
    fun register(
        @Field("taikhoan") username: String,
        @Field("matkhau") password: String,
        @Field("email") email: String,
        @Field("tenhienthi") name: String,
        @Field("ngaysinh") birthday: String
    ): Call<Account>

    @FormUrlEncoded
    @POST(URL_ACCOUNT)
    fun updateAccount(
        @Field("mataikhoan") accountId: Int,
        @Field("email") email: String,
        @Field("tenhienthi") name: String,
        @Field("ngaysinh") birthday: String
    ): Call<Account>

    @GET(URL_ACCOUNT)
    fun checkPassword(
        @Query("mataikhoan") accountId: Int,
        @Query("matkhau") password: String
    ): Call<Account>

    @FormUrlEncoded
    @POST(URL_ACCOUNT)
    fun updatePassword(
        @Field("mataikhoan") accountId: Int,
        @Field("matkhau") password: String
    ): Call<Account>

    //Truyện
    @GET(URL_STORY)
    fun getListStories(@Query("gioihantuoi") age: Int): Call<List<Story>>

    @GET(URL_STORY)
    fun getStory(@Query("matruyen") storyId: Int): Call<Story>

    @GET(URL_STORY)
    fun searchStory(
        @Query("tentruyen") name: String,
        @Query("gioihantuoi") age: Int
    ): Call<List<Story>>

    @FormUrlEncoded
    @POST(URL_STORY)
    fun likeStory(
        @Field("matruyen") storyId: Int,
        @Field("mataikhoan") accountId: Int
    ): Call<Story>

    @GET(URL_STORY)
    fun checkLikeStory(
        @Query("matruyen") storyId: Int,
        @Query("mataikhoan") accountId: Int
    ): Call<Story>

    //Đánh giá
    @GET(URL_RATE)
    fun getRateList(@Query("matruyen") storyId: Int): Call<List<Rate>>

    @GET(URL_RATE)
    fun checkRateOfAccount(
        @Query("matruyen") storyId: Int,
        @Query("mataikhoan") accountId: Int
    ): Call<Rate>

    @FormUrlEncoded
    @POST(URL_RATE)
    fun addRate(
        @Field("matruyen") storyId: Int,
        @Field("mataikhoan") accountId: Int,
        @Field("tenhienthi") name: String,
        @Field("diemdanhgia") point: Int,
        @Field("danhgia") rate: String
    ): Call<Rate>

    @FormUrlEncoded
    @POST(URL_RATE)
    fun updateRate(
        @Field("madanhgia") rateId: Int,
        @Field("diemdanhgia") point: Int,
        @Field("danhgia") rate: String
    ): Call<Rate>

    @GET(URL_RATE)
    fun deleteRate(@Query("madanhgia") rateId: Int): Call<Rate>

    //Lọc truyện
    @GET(URL_STORY_FILTER)
    fun getListStoriesFilter(
        @Query("theloai") species: String,
        @Query("trangthai") status: String,
        @Query("gioihantuoi") age: Int
    ): Call<List<Story>>

    //Chương truyện
    @GET(URL_CHAPTER)
    fun getChapterList(@Query("matruyen") storyId: Int): Call<List<Chapter>>

    @GET(URL_CHAPTER)
    fun searchChapter(
        @Query("matruyen") storyId: Int,
        @Query("sochuong") num: String
    ): Call<List<Chapter>>

    @GET(URL_CHAPTER)
    fun getChapter(
        @Query("matruyen") storyId: Int,
        @Query("machuong") chapterId: Int,
        @Query("thaydoichuong") num: Int,
        @Query("mataikhoan") accountId: Int
    ): Call<Chapter>

    @GET(URL_CHAPTER)
    fun getChapterReadId(
        @Query("matruyen") storyId: Int,
        @Query("mataikhoan") accountId: Int,
        @Query("so") num: Int
    ): Call<Chapter>

    @FormUrlEncoded
    @POST(URL_CHAPTER)
    fun firstChapter(@Field("matruyen") storyId: Int): Call<Chapter>

    //Chương đã đọc
    @GET(URL_CHAPTER)
    fun getChapterListRead(
        @Query("matruyen") storyId: Int,
        @Query("mataikhoan") accountId: Int,
        @Query("so") num: Int
    ): Call<List<ChapterRead>>


    //Truyện theo dõi
    @GET(URL_STORY_FOLLOW)
    fun getListStoriesFollow(@Query("mataikhoan") accountId: Int): Call<List<Story>>

    @GET(URL_STORY_FOLLOW)
    fun checkStoryFollow(
        @Query("mataikhoan") accountId: Int,
        @Query("matruyen") storyId: Int
    ): Call<Story>

    @FormUrlEncoded
    @POST(URL_STORY_FOLLOW)
    fun followStory(
        @Field("mataikhoan") accountId: Int,
        @Field("matruyen") storyId: Int,
        @Field("tentruyen") story: String,
        @Field("tacgia") author: String,
        @Field("gioihantuoi") ageLimit: Int,
        @Field("trangthai") status: String,
        @Field("tongchuong") chapterNumber: Int,
        @Field("anh") image: String,
        @Field("thoigiancapnhat") updateTime: String
    ): Call<Story>

    //Bình luận
    @GET(URL_COMMENT)
    fun getCommentList(@Query("matruyen") storyId: Int): Call<List<Comment>>

    @FormUrlEncoded
    @POST(URL_COMMENT)
    fun addCommnet(
        @Field("matruyen") storyId: Int,
        @Field("mataikhoan") accountId: Int,
        @Field("tenhienthi") name: String,
        @Field("binhluan") comment: String
    ): Call<Comment>

    @GET(URL_COMMENT)
    fun checkCommentOfAccount(
        @Query("mabinhluan") commentId: Int,
        @Query("mataikhoan") accountId: Int
    ): Call<Comment>

    @GET(URL_COMMENT)
    fun deleteComment(@Query("mabinhluan") commentId: Int): Call<Comment>

    @FormUrlEncoded
    @POST(URL_COMMENT)
    fun updateCommnet(
        @Field("mabinhluan") commentId: Int,
        @Field("binhluan") comment: String
    ): Call<Comment>
}