package ui.anwesome.com.imagebarview

/**
 * Created by anweshmishra on 20/02/18.
 */
import android.content.*
import android.view.*
import android.graphics.*

class ImageBarView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action == MotionEvent.ACTION_DOWN) {

        }
        return true
    }
}