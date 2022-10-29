package huce.fit.appreadstories.interfaces;

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
import huce.fit.appreadstories.model.TruyenTheoDoi;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    //Tài khoản
    @FormUrlEncoded
    @POST(URL_LOGIN)//oke
    Call<TaiKhoan> login(@Field("taikhoan") String username,
                         @Field("matkhau") String password);

    @FormUrlEncoded
    @POST(URL_REGISTER)//oke
    Call<TaiKhoan> register(@Field("taikhoan") String username,
                            @Field("matkhau") String password,
                            @Field("email") String email,
                            @Field("tenhienthi") String name,
                            @Field("ngaysinh") String birthday);

    @GET(URL_ACCOUNT)//oke
    Call<TaiKhoan> getAccount(@Query("mataikhoan") int id);

    @FormUrlEncoded
    @POST(URL_ACCOUNT)//oke
    Call<TaiKhoan> updateAccount(@Field("mataikhoan") int id,
                                 @Field("matkhau") String password,
                                 @Field("email") String email,
                                 @Field("tenhienthi") String name);

    //Truyện
    @GET(URL_STORY)//oke
    Call<List<Truyen>> getListStories(@Query("matruyen") int id);

    @GET(URL_STORY)//oke
    Call<Truyen> getStory(@Query("matruyen") int id);

    @GET(URL_STORY)//oke
    Call<List<Truyen>> searchStory(@Query("tentruyen") String name);

    @FormUrlEncoded
    @POST(URL_STORY)//oke
    Call<Truyen> likeStory(@Field("matruyen") int id1, @Field("mataikhoan") int id2);

    @GET(URL_STORY)//oke
    Call<Truyen> checkLikeStory(@Query("matruyen") int id1, @Query("mataikhoan") int id2);

    //Đánh giá
    @GET(URL_RATE)//oke
    Call<List<DanhGia>> getListRate(@Query("matruyen") int id);

    @GET(URL_RATE)//oke
    Call<DanhGia> checkRateOfAccount(@Query("matruyen") int id1, @Query("mataikhoan") int id2);

    @FormUrlEncoded
    @POST(URL_RATE)//oke
    Call<DanhGia> addRate(@Field("matruyen") int id1,
                              @Field("mataikhoan") int id2,
                              @Field("tenhienthi") String name,
                              @Field("diemdanhgia") int point,
                              @Field("danhgia") String rate);

    @FormUrlEncoded
    @POST(URL_RATE)//oke
    Call<DanhGia> updateRate(@Field("madanhgia") int id1,
                          @Field("diemdanhgia") int point,
                          @Field("danhgia") String rate);

    @GET(URL_RATE)
    Call<DanhGia> deleteRate(@Query("madanhgia") int id);

    //Lọc truyện
    @GET(URL_STORY_FILTER)//oke
    Call<List<Truyen>> getListStoriesFilter(@Query("theloai") String species, @Query("trangthai") String status);

    //Chương truyện
    @GET(URL_CHAPTER)//oke
    Call<List<ChuongTruyen>> getListChapter(@Query("matruyen") int id);

    @GET(URL_CHAPTER)//oke
    Call<List<ChuongTruyen>> getListChapterRead(@Query("matruyen") int id1, @Query("mataikhoan") int id2, @Query("so") int num);

    @GET(URL_CHAPTER)//oke
    Call<ChuongTruyen> getChapterReading(@Query("matruyen") int id1, @Query("mataikhoan") int id2, @Query("so") int num);

    @GET(URL_CHAPTER)//oke
    Call<List<ChuongTruyen>> searchChapter(@Query("matruyen") int id, @Query("sochuong") String num);

    @GET(URL_CHAPTER)//Chapter, nextChapter, previousChapter //oke
    Call<ChuongTruyen> getChapter(@Query("matruyen") int id1, @Query("machuong") int id2, @Query("thaydoichuong") int num, @Query("mataikhoan") int id3);

    @FormUrlEncoded
    @POST(URL_CHAPTER)//first_chapter //oke
    Call<ChuongTruyen> firstChapter(@Field("matruyen") int id);

    //Truyện theo dõi
    @GET(URL_STORY_FOLLOW)//oke
    Call<List<TruyenTheoDoi>> getListStoriesFollow(@Query("mataikhoan") int id);

    @GET(URL_STORY_FOLLOW)//oke
    Call<TruyenTheoDoi> checkStoryFollow(@Query("mataikhoan") int id1, @Query("matruyen") int id2);

    @FormUrlEncoded
    @POST(URL_STORY_FOLLOW)//oke
    Call<TruyenTheoDoi> add_delete_StoryFollow(@Field("mataikhoan") int id1,
                                       @Field("matruyen") int id2,
                                       @Field("tentruyen") String story,
                                       @Field("tacgia") String author,
                                       @Field("trangthai") String status,
                                       @Field("sochuong") int chapter_number,
                                       @Field("anh") String image);

    //Bình luận
    @GET(URL_COMMENT)//oke
    Call<List<BinhLuan>> getListComment(@Query("matruyen") int id);

    @FormUrlEncoded
    @POST(URL_COMMENT)//oke
    Call<BinhLuan> addCommnet(@Field("matruyen") int id1,
                              @Field("mataikhoan") int id2,
                              @Field("tenhienthi") String name,
                              @Field("binhluan") String comment);

    @GET(URL_COMMENT)//oke
    Call<BinhLuan> checkCommentOfAccount(@Query("mabinhluan") int id1, @Query("mataikhoan") int id2);

    @GET(URL_COMMENT)//oke
    Call<BinhLuan> deleteComment(@Query("mabinhluan") int id);

    @FormUrlEncoded
    @POST(URL_COMMENT)//oke
    Call<BinhLuan> updateCommnet(@Field("mabinhluan") int id,
                              @Field("binhluan") String comment);
}
