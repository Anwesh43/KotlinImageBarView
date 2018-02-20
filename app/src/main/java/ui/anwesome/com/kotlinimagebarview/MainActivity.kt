package ui.anwesome.com.kotlinimagebarview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ui.anwesome.com.imagebarview.ImageBarView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ImageBarView.create(this, BitmapFactory.decodeResource(resources, R.drawable.nature_more))
        view.addImageBarListener({
            Toast.makeText(this, "image created", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(this, "image destroyed", Toast.LENGTH_SHORT).show()
        })
        fullScreen()
    }
}
fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
