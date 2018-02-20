package ui.anwesome.com.kotlinimagebarview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.imagebarview.ImageBarView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImageBarView.create(this, BitmapFactory.decodeResource(resources, R.drawable.nature_more))
    }
}
