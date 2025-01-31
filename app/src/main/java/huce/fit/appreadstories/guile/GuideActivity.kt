package huce.fit.appreadstories.guile

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R

class GuideActivity: AppCompatActivity() {

    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        init()
        processEvent()
    }

    private fun init() {
        ivBack = findViewById(R.id.ivBack)
    }

    private fun processEvent() {
        ivBack.setOnClickListener { finish() }
    }
}
