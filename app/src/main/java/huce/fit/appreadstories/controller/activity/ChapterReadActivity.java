package huce.fit.appreadstories.controller.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.controller.adapters.ChapterDialogAdapter;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.Truyen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterReadActivity extends AppCompatActivity {
    private ConstraintLayout constrainLayout;
    private BottomNavigationView btNavigationView;
    private ProgressBar pbReLoad;
    private ScrollView scView;
    private TextView tv0, tv1, tv2, tv3, tvNumberChapter, tvChapterName, tvPostPerson, tvPostDay, tvContent;
    private RelativeLayout rlNextAndPrevious;
    private ImageView ivPrevious, ivNext;
    private int idAccount, idStory, idChapter;
    private int fontSize = 18;
    private int lineStretch = 100;
    private String textColor,backgroundColor;
    private float lineStretchFloat;
    private double progress;
    private boolean isComment;
    private AtomicInteger status = new AtomicInteger(1);
    Handler handler = new Handler();

    public void setIdChapter(int idChapter) {
        this.idChapter = idChapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_read);

        getSharedPreferences();

        idStory = getIntent().getIntExtra("idStory", 0);
        idChapter = getIntent().getIntExtra("idChapterReading", 0);

        constrainLayout = findViewById(R.id.constrainLayout);
        btNavigationView = findViewById(R.id.btNavigationView);
        scView = findViewById(R.id.scView);
        rlNextAndPrevious = findViewById(R.id.rlNextAndPrevious);
        ivPrevious = findViewById(R.id.ivPrevious);
        ivNext = findViewById(R.id.ivNext);

        pbReLoad = findViewById(R.id.pbReLoad);
        tv0 = findViewById(R.id.tv0);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tvNumberChapter = findViewById(R.id.tvNumberChapter);
        tvChapterName = findViewById(R.id.tvChapterName);
        tvPostPerson = findViewById(R.id.tvPostPerson);
        tvPostDay = findViewById(R.id.tvPostDay);
        tvContent = findViewById(R.id.tvContent);

        constrainLayout.setBackgroundColor(Color.parseColor(backgroundColor));
        setTextColor();

        btNavigationView.setVisibility(View.GONE);
        constrainLayout.setVisibility(View.GONE);
        rlNextAndPrevious.setVisibility(View.GONE);

        setFontSize(fontSize);
        //lineSpacingMultiplier = lineheight/textsize = mult
        //lineSpacingExtra = lineheight - textsize = add
        //setLineSpacing = lineheight * mul + add
        lineStretchFloat = Float.parseFloat(String.valueOf(lineStretch / 100));
        tvContent.setLineSpacing(4, lineStretchFloat);

        getData(2, "Không tìm thấy chương!");
        processEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkComment();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences1, sharedPreferences2;
        sharedPreferences1 = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences1.getInt("idAccount", 0);
        Log.e("idFragmentAccount", String.valueOf(idAccount));

        sharedPreferences2 = getSharedPreferences("SettingChapterRead", Context.MODE_PRIVATE);
        fontSize = sharedPreferences2.getInt("fontSize", 18);
        lineStretch = sharedPreferences2.getInt("lineStretch", 100);
        textColor  = sharedPreferences2.getString("textColor","#000000");
        backgroundColor  = sharedPreferences2.getString("backgroundColor","#ffffff");
        Log.e("fontsize: ", String.valueOf(fontSize));
        Log.e("lineStretch: ", String.valueOf(lineStretch));
        Log.e("textColor: ", textColor);
        Log.e("backgroundColor: ", backgroundColor);
    }

    private void setTextColor(){
        tv0.setTextColor(Color.parseColor(textColor));
        tvNumberChapter.setTextColor(Color.parseColor(textColor));
        tv1.setTextColor(Color.parseColor(textColor));
        tvChapterName.setTextColor(Color.parseColor(textColor));
        tv2.setTextColor(Color.parseColor(textColor));
        tvPostPerson.setTextColor(Color.parseColor(textColor));
        tv3.setTextColor(Color.parseColor(textColor));
        tvPostDay.setTextColor(Color.parseColor(textColor));
        tvContent.setTextColor(Color.parseColor(textColor));
    }

    private void setFontSize(int size) {
        int size0 = size + 2;
        tv0.setTextSize(size0);
        tvNumberChapter.setTextSize(size0);
        tv1.setTextSize(size0);
        tvChapterName.setTextSize(size0);
        tv2.setTextSize(size);
        tvPostPerson.setTextSize(size);
        tv3.setTextSize(size);
        tvPostDay.setTextSize(size);
        tvContent.setTextSize(size);
    }

    public void getData(int chapter_change, String text) {
//        Log.e("1: ", String.valueOf(idStory));
//        Log.e("2: ", String.valueOf(idChapter));
//        Log.e("3: ", String.valueOf(chapter_change));
//        Log.e("4: ", String.valueOf(idAccount));
        pbReLoad.setVisibility(View.VISIBLE);
        Api.apiInterface().getChapter(idStory, idChapter, chapter_change, idAccount).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(Call<ChuongTruyen> call, Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pbReLoad.setVisibility(View.GONE);
                    ChuongTruyen chuong = response.body();
                    if (chuong.getMachuong() != 0) {
                        tvNumberChapter.setText(chuong.getSochuong());
                        tvChapterName.setText(chuong.getTenchuong());
                        tvPostPerson.setText(chuong.getNguoidang());
                        tvPostDay.setText(chuong.getThoigiandang());
                        tvContent.setText(chuong.getNoidung());
                        setIdChapter(chuong.getMachuong());

                        constrainLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ChapterReadActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChuongTruyen> call, Throwable t) {
                Log.e("Err_ChapterRead", "getData", t);
            }
        });
    }

    private void checkComment() {
        Api.apiInterface().checkLikeStory(idStory, idAccount).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen t = response.body();

                    progress = Double.parseDouble(t.getTylechuongdadoc());
                    if (progress >= 5) {
                        isComment = true;
                    }
                    if (progress < 5) {
                        isComment = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_ChapterRead", "checkComment", t);
            }
        });
    }

    private void processEvents() {
        btNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btMenuBack:
                    finish();
                    break;
                case R.id.btMenuListChapter:
                    handler.postDelayed(() -> {
                        openDialogListChapter();
                    }, 300);
                    break;
                case R.id.btMenuComment:
                    if (isComment) {
                        Intent intent2 = new Intent(ChapterReadActivity.this, CommentListActivity.class);
                        intent2.putExtra("idStory", idStory);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(ChapterReadActivity.this, "Tiến độ đọc truyện chưa đạt 5%\n\tKhông thể bình luận!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btMenuSetting:
                    openDialogSetting();
                    break;
                default:
                    break;
            }
            return true;
        });

        //ẩn hiện btnavigation
        constrainLayout.setOnClickListener(v -> {
            status.getAndIncrement();
            if (status.get() % 2 != 0) {
                btNavigationView.setVisibility(View.GONE);
                rlNextAndPrevious.setVisibility(View.GONE);
                Log.e("statusHide:", String.valueOf(status.get()));
            } else {
                btNavigationView.setVisibility(View.VISIBLE);
                rlNextAndPrevious.setVisibility(View.VISIBLE);
                Log.e("statusShow:", String.valueOf(status.get()));
            }
        });

        ivPrevious.setOnClickListener(v -> {
            getData(1, "Đây là chương đầu!");

            scView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                // Ready, move up
                scView.fullScroll(View.FOCUS_UP);
            });
        });

        ivNext.setOnClickListener(v -> {
            getData(3, "Đây là chương cuối!");

            scView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                // Ready, move up
                scView.fullScroll(View.FOCUS_UP);
            });
        });
    }

    private void openDialogListChapter() {
        List<ChuongTruyen> listChapter = new ArrayList<>();
        ChapterDialogAdapter chapterDialogAdapter;
        RecyclerView rcViewChapter;

        status.getAndIncrement();

        Dialog dialogListChapter = new Dialog(this);
        dialogListChapter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListChapter.setContentView(R.layout.dialog_chapter_list);

        Window window = dialogListChapter.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        //setLayout
        windowAttributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        // vị trí dialog
        windowAttributes.gravity = Gravity.LEFT;
        //hiệu ứng di chuyển dialog
        windowAttributes.windowAnimations = R.style.DialogChapterlist;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogListChapter.setCancelable(true);
//        dialogListChapter.setCanceledOnTouchOutside(true);

        ImageView ivStory = dialogListChapter.findViewById(R.id.ivStory);
        ImageView ivReverse = dialogListChapter.findViewById(R.id.ivReverse);
        TextView tvStoryName = dialogListChapter.findViewById(R.id.tvStoryName);
        TextView tvAuthor = dialogListChapter.findViewById(R.id.tvAuthor);
        SearchView svChapter = dialogListChapter.findViewById(R.id.svChapter);

        //getDataStory
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen tc = response.body();
                tvStoryName.setText(tc.getTentruyen());
                tvAuthor.setText(tc.getTacgia());
                Picasso.get().load(tc.getAnh())
                        .into(ivStory);
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("E235_DialogChapterList", t.toString());
            }
        });

        //RecyclerView
        rcViewChapter = dialogListChapter.findViewById(R.id.rcListChapterOnChapterRead);
        rcViewChapter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chapterDialogAdapter = new ChapterDialogAdapter(listChapter, (position, view1) -> {
            pbReLoad.setVisibility(View.VISIBLE);
            Log.e("getIdChapter", String.valueOf(listChapter.get(position).getMachuong()));
            setIdChapter(listChapter.get(position).getMachuong());

            handler.postDelayed(() -> {
                getData(2, "Không tìm thấy chương!");
                pbReLoad.setVisibility(View.GONE);
            }, 1500);

            dialogListChapter.dismiss();
        });//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterDialogAdapter);
        //tạo dòng kẻ ngăn cách các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        rcViewChapter.addItemDecoration(itemDecoration);

        //getDataListChapter
        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
                    listChapter.clear();
                    listChapter.addAll(response.body());
                    chapterDialogAdapter.notifyDataSetChanged();

                    btNavigationView.setVisibility(View.GONE);
                    rlNextAndPrevious.setVisibility(View.GONE);
                    dialogListChapter.show();
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                Log.e("E272_DialogChapterList", t.toString());
            }
        });

        //event
        ivReverse.setOnClickListener(view -> {
            Collections.reverse(listChapter);// đảo ngược dánh sách
            rcViewChapter.setAdapter(chapterDialogAdapter);
        });

        svChapter.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Api.apiInterface().searchChapter(idStory, newText).enqueue(new Callback<List<ChuongTruyen>>() {
                    @Override
                    public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                        if (response.isSuccessful() && response.body().toString() != null) {
                            listChapter.clear();
                            listChapter.addAll(response.body());
                            chapterDialogAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                        Log.e("E302_DialogChapterList", t.toString());
                    }
                });
                return false;
            }
        });
    }

    private void openDialogSetting() {
        ImageView ivSettingColor, ivFontSizeDown, ivFontSizeUp, ivLineStretchDown, ivLineStretchUp;
        TextView tvFontSize, tvLineStretch;

        Dialog dialogSetting = new Dialog(this);
        dialogSetting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetting.setContentView(R.layout.dialog_setting);

        Window window = dialogSetting.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        //setLayout
        windowAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // vị trí dialog
        windowAttributes.gravity = Gravity.BOTTOM;
        //hiệu ứng di chuyển dialog
        windowAttributes.windowAnimations = R.style.DialogSetting;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogSetting.setCancelable(true);
//        dialogSetting.setCanceledOnTouchOutside(true);

        ivSettingColor = dialogSetting.findViewById(R.id.ivSettingColor);
        ivFontSizeDown = dialogSetting.findViewById(R.id.ivFontSizeDown);
        ivFontSizeUp = dialogSetting.findViewById(R.id.ivFontSizeUP);
        ivLineStretchDown = dialogSetting.findViewById(R.id.ivLineStretchDown);
        ivLineStretchUp = dialogSetting.findViewById(R.id.ivLineStretchUp);
        tvFontSize = dialogSetting.findViewById(R.id.tvFontSize);
        tvLineStretch = dialogSetting.findViewById(R.id.tvLineStretch);

        tvFontSize.setText(String.valueOf(fontSize));
        tvLineStretch.setText(lineStretch + "%");

        //event
        ivSettingColor.setOnClickListener(view -> {
            dialogSetting.hide();
            openDialogSettingColor();
        });

        ivFontSizeDown.setOnClickListener(view -> {
            if (fontSize > 15 && fontSize < 41) {
                fontSize = fontSize - 1;
                Log.e("fontsize: ", String.valueOf(fontSize));
                setFontSize(fontSize);
                tvFontSize.setText(String.valueOf(fontSize));
                setSharedPreferences(fontSize, lineStretch);
            }
        });

        ivFontSizeUp.setOnClickListener(view -> {
            if (fontSize > 14 && fontSize < 40) {
                fontSize = fontSize + 1;
                Log.e("fontsize: ", String.valueOf(fontSize));
                setFontSize(fontSize);
                tvFontSize.setText(String.valueOf(fontSize));
                setSharedPreferences(fontSize, lineStretch);
            }
        });

        ivLineStretchDown.setOnClickListener(view -> {
            if (lineStretch > 70 && lineStretch < 305) {
                lineStretch = lineStretch - 5;
                tvLineStretch.setText(lineStretch + "%");
                setLineStretch(lineStretch);
            }
        });

        ivLineStretchUp.setOnClickListener(view -> {
            if (lineStretch > 65 && lineStretch < 300) {
                lineStretch = lineStretch + 5;
                tvLineStretch.setText(lineStretch + "%");
                setLineStretch(lineStretch);
            }
        });

        dialogSetting.show();
    }

    private void setLineStretch(int line_stretch_int) {
        Log.e("lineStretch: ", String.valueOf(line_stretch_int));
        lineStretchFloat = Float.parseFloat(String.valueOf(line_stretch_int)) / 100;
        tvContent.setLineSpacing(4, lineStretchFloat);

        setSharedPreferences(fontSize, lineStretch);
    }

    private void setSharedPreferences(int font_size, int line_stretch) {
        SharedPreferences sharedPreferences = getSharedPreferences("SettingChapterRead", MODE_PRIVATE);
        SharedPreferences.Editor myedit = sharedPreferences.edit();

        myedit.putInt("fontSize", font_size);
        myedit.putInt("lineStretch", line_stretch);
        myedit.commit();
    }

    private void openDialogSettingColor() {
        ImageView ivTextColor, ivBackgroundColor;
        TextView tvUseTextColor, tvUseBackgroundColor;

        ColorPickerDialog colorPickerDialog= ColorPickerDialog.createColorPickerDialog(this);

        Dialog dialogSettingColor = new Dialog(this);
        dialogSettingColor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingColor.setContentView(R.layout.dialog_setting_color);

        Window window = dialogSettingColor.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        //setLayout
        windowAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // vị trí dialog
        windowAttributes.gravity = Gravity.BOTTOM;
        //hiệu ứng di chuyển dialog
        windowAttributes.windowAnimations = R.style.DialogSetting;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogSettingColor.setCancelable(true);

        ivTextColor = dialogSettingColor.findViewById(R.id.ivTextColor);
        ivBackgroundColor = dialogSettingColor.findViewById(R.id.ivBackgroundColor);
        tvUseTextColor = dialogSettingColor.findViewById(R.id.tvUseTextColor);
        tvUseBackgroundColor = dialogSettingColor.findViewById(R.id.tvUseBackgroundColor);

        ivTextColor.setBackgroundColor(Color.parseColor(textColor));
        ivBackgroundColor.setBackgroundColor(Color.parseColor(backgroundColor));

        //event
        ivTextColor.setOnClickListener(view -> {
            colorPickerDialog.setOnColorPickedListener((color, hexVal) -> {
                textColor = "#" + hexVal.substring(3);
                Log.e("color: ", String.valueOf(color));
                Log.e("hexVal",hexVal);
                Log.e("hexColor",textColor);
                ivTextColor.setBackgroundColor(Color.parseColor(textColor));
                setSharedPreferences(textColor,backgroundColor);
            });
            colorPickerDialog.setHexaDecimalTextColor(getResources().getColor(R.color.black));
            colorPickerDialog.setInitialColor(Color.parseColor(textColor));
            colorPickerDialog.setLastColor(textColor);
            colorPickerDialog.show();
        });

        ivBackgroundColor.setOnClickListener(view -> {
            colorPickerDialog.setOnColorPickedListener((color, hexVal) -> {
                backgroundColor = "#" + hexVal.substring(3);
                Log.e("color: ", String.valueOf(color));
                Log.e("hexVal",hexVal);
                Log.e("hexColor",backgroundColor);
                ivBackgroundColor.setBackgroundColor(Color.parseColor(backgroundColor));
                setSharedPreferences(textColor,backgroundColor);
            });
            colorPickerDialog.setHexaDecimalTextColor(getResources().getColor(R.color.black));
            colorPickerDialog.setInitialColor(Color.parseColor(backgroundColor));
            colorPickerDialog.setLastColor(backgroundColor);
            colorPickerDialog.show();
        });


        tvUseTextColor.setOnClickListener(view -> {
            setTextColor();
        });

        tvUseBackgroundColor.setOnClickListener(view -> {
            constrainLayout.setBackgroundColor(Color.parseColor(backgroundColor));
        });

        dialogSettingColor.show();
    }

    private void setSharedPreferences(String textColor, String backgraoundColor) {
        SharedPreferences sharedPreferences = getSharedPreferences("SettingChapterRead", MODE_PRIVATE);
        SharedPreferences.Editor myedit = sharedPreferences.edit();

        myedit.putString("textColor", textColor);
        myedit.putString("backgraoundColor", backgraoundColor);
        myedit.commit();
    }
}
