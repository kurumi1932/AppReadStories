package huce.fit.appreadstories.controller.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;

public class ContactInforActivity extends AppCompatActivity {

    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v->{
            finish();
        });
    }
}

