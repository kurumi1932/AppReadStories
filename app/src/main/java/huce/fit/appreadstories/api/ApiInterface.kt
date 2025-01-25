package huce.fit.appreadstories.api;

import static huce.fit.appreadstories.api.Api.URL_ACCOUNT;
import static huce.fit.appreadstories.api.Api.URL_CHAPTER;
import static huce.fit.appreadstories.api.Api.URL_COMMENT;
import static huce.fit.appreadstories.api.Api.URL_LOGIN;
import static huce.fit.appreadstories.api.Api.URL_RATE;
import static huce.fit.appreadstories.api.Api.URL_REGISTER;
import static huce.fit.appreadstories.api.Api.URL_STORY;
import static huce.fit.appreadstories.api.Api.URL_STORY_FILTER;
import static huce.fit.appreadstories.api.Api.URL_STORY_FOLLOW;

import java.util.List;

import huce.fit.appreadstories.model.BinhLuan;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.DanhGia;
import huce.fit.appreadstories.model.TaiKhoan;
import huce.fit.appreadstories.model.Truyen;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    //Tài khoản
    @FormUrlEncoded
    @POST(URL_LOGIN)
    Call<TaiKhoan> login(@Field("taikhoan") String username,
                         @Field("matkhau") String password);

    @FormUrlEncoded
    @POST(URL_REGISTER)
    Call<TaiKhoan> register(@Field("taikhoan") String username,
                            @Field("matkhau") String password,
                            @Field("email") String email,
                            @Field("tenhienthi") String name,
                            @Field("ngaysinh") String birthday);

    @FormUrlEncoded
    @POST(URL_ACCOUNT)
    Call<TaiKhoan> updateAccount(@Field("mataikhoan") int idAccount,
                                 @Field("email") String email,
                                 @Field("tenhienthi") String name,
                                 @Field("ngaysinh") String birthday);

    @GET(URL_ACCOUNT)//oke
    Call<TaiKhoan> checkPassword(@Query("mataikhoan") int idAccount,
                                 @Query("matkhau") String password);

    @FormUrlEncoded
    @POST(URL_ACCOUNT)
    Call<TaiKhoan> updatePassword(@Field("mataikhoan") int idAccount,
                                 @Field("matkhau") String password);

    //Truyện
    @GET(URL_STORY)//oke
    Call<List<Truyen>> getListStories(@Query("gioihantuoi") int age);

    @GET(URL_STORY)//oke
    Call<Truyen> getStory(@Query("matruyen") int idStory);

    @GET(URL_STORY)//oke
    Call<List<Truyen>> searchStory(@Query("tentruyen") String name,
                                   @Query("gioihantuoi") int age);

    @FormUrlEncoded
    @POST(URL_STORY)//oke
    Call<Truyen> likeStory(@Field("matruyen") int idStory,
                           @Field("mataikhoan") int idAccount);

    @GET(URL_STORY)//oke
    Call<Truyen> checkLikeStory(@Query("matruyen") int idStory,
                                @Query("mataikhoan") int idAccount);

    //Đánh giá
    @GET(URL_RATE)//oke
    Call<List<DanhGia>> getListRate(@Query("matruyen") int idStory);

    @GET(URL_RATE)//oke
    Call<DanhGia> checkRateOfAccount(@Query("matruyen") int idStory,
                                     @Query("mataikhoan") int idAccount);

    @FormUrlEncoded
    @POST(URL_RATE)//oke
    Call<DanhGia> addRate(@Field("matruyen") int idStory,
                              @Field("mataikhoan") int idAccount,
                              @Field("tenhienthi") String name,
                              @Field("diemdanhgia") int point,
                              @Field("danhgia") String rate);

    @FormUrlEncoded
    @POST(URL_RATE)//oke
    Call<DanhGia> updateRate(@Field("madanhgia") int idRate,
                          @Field("diemdanhgia") int point,
                          @Field("danhgia") String rate);

    @GET(URL_RATE)
    Call<DanhGia> deleteRate(@Query("madanhgia") int idRate);

    //Lọc truyện
    @GET(URL_STORY_FILTER)//oke
    Call<List<Truyen>> getListStoriesFilter(@Query("theloai") String species,
                                            @Query("trangthai") String status,
                                            @Query("gioihantuoi") int age);

    //Chương truyện
    @GET(URL_CHAPTER)//oke
    Call<List<ChuongTruyen>> getListChapter(@Query("matruyen") int idStory);

    @GET(URL_CHAPTER)//oke
    Call<List<ChuongTruyen>> getListChapterRead(@Query("matruyen") int idStory,
                                                @Query("mataikhoan") int idAccount,
                                                @Query("so") int num);

    @GET(URL_CHAPTER)//oke
    Call<ChuongTruyen> getChapterRead(@Query("matruyen") int idStory,
                                      @Query("mataikhoan") int idAccount,
                                      @Query("so") int num);

    @GET(URL_CHAPTER)//oke
    Call<List<ChuongTruyen>> searchChapter(@Query("matruyen") int idStory,
                                           @Query("sochuong") String num);

    @GET(URL_CHAPTER)//Chapter, nextChapter, previousChapter //oke
    Call<ChuongTruyen> getChapter(@Query("matruyen") int idStory,
                                  @Query("machuong") int idChapter,
                                  @Query("thaydoichuong") int num,
                                  @Query("mataikhoan") int idAccount);

    @FormUrlEncoded
    @POST(URL_CHAPTER)//first_chapter //oke
    Call<ChuongTruyen> firstChapter(@Field("matruyen") int idStory);

    //Truyện theo dõi
    @GET(URL_STORY_FOLLOW)//oke
    Call<List<Truyen>> getListStoriesFollow(@Query("mataikhoan") int idAccount);

    @GET(URL_STORY_FOLLOW)//oke
    Call<Truyen> checkStoryFollow(@Query("mataikhoan") int idAccount,
                                  @Query("matruyen") int idStory);

    @FormUrlEncoded
    @POST(URL_STORY_FOLLOW)//oke
    Call<Truyen> followStory(@Field("mataikhoan") int idAccount,
                                       @Field("matruyen") int idStory,
                                       @Field("tentruyen") String story,
                                       @Field("tacgia") String author,
                                       @Field("gioihantuoi") int ageLimit,
                                       @Field("trangthai") String status,
                                       @Field("tongchuong") int chapterNumber,
                                       @Field("anh") String image,
                                       @Field("thoigiancapnhat") String updateTime);

    //Bình luận
    @GET(URL_COMMENT)//oke
    Call<List<BinhLuan>> getListComment(@Query("matruyen") int idStory);

    @FormUrlEncoded
    @POST(URL_COMMENT)//oke
    Call<BinhLuan> addCommnet(@Field("matruyen") int idStory,
                              @Field("mataikhoan") int idAccount,
                              @Field("tenhienthi") String name,
                              @Field("binhluan") String comment);

    @GET(URL_COMMENT)//oke
    Call<BinhLuan> checkCommentOfAccount(@Query("mabinhluan") int idComment,
                                         @Query("mataikhoan") int idAccount);

    @GET(URL_COMMENT)//oke
    Call<BinhLuan> deleteComment(@Query("mabinhluan") int idComment);

    @FormUrlEncoded
    @POST(URL_COMMENT)//oke
    Call<BinhLuan> updateCommnet(@Field("mabinhluan") int idComment,
                              @Field("binhluan") String comment);
}
