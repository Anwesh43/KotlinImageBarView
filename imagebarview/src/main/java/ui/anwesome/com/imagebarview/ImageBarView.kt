package ui.anwesome.com.imagebarview

/**
 * Created by anweshmishra on 20/02/18.
 */
import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*

class ImageBarView(ctx:Context, var bitmap:Bitmap, var n:Int = 10):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    var imageBarListener : ImageBarListener ?= null
    fun addImageBarListener(onImageCreated: () -> Unit, onImageDestroyed: () -> Unit) {
        imageBarListener = ImageBarListener(onImageCreated, onImageDestroyed)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class ImageBarState(var scale:Float = 0f, var dir:Float = 0f, var prevScale:Float = 0f) {
        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1f - 2 * scale
                startcb()
            }
        }
    }
    data class Animator(var view:View, var animated:Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class ImageBar(var bitmap:Bitmap, var w:Float, var h:Float, var n:Int) {
        val state = ImageBarState()
        fun draw(canvas:Canvas, paint:Paint) {
            val bw = bitmap.width
            val bh = bitmap.height
            val y_gap = bh.toFloat()/n
            var y = 0f
            for(i in 1..n) {
                canvas.save()
                val ox = -bw/2 + (w + bw)*((i+1)%2)
                canvas.translate(ox + (w/2 - ox) * state.scale, h/2 - bh.toFloat()/2)
                val path = Path()
                path.addRect(RectF(-bw/2f, y - y_gap/2, bw/2f, y + y_gap/2), Path.Direction.CW)
                canvas.clipPath(path)
                canvas.drawBitmap(bitmap, -bw/2f, 0f, paint)
                canvas.restore()
                y += y_gap
            }
        }
        fun update(stopcb: (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view:ImageBarView, var time:Int = 0) {
        val animator = Animator(view)
        var imageBar:ImageBar ?= null
        fun render(canvas:Canvas, paint:Paint) {
            if(time == 0) {
                val w = canvas.width
                val h = canvas.height
                var bitmap:Bitmap = Bitmap.createScaledBitmap(view.bitmap, w/2, h/2, true)
                if(view.n > 2) {
                    imageBar = ImageBar(bitmap, w.toFloat(), h.toFloat(), view.n)
                }
            }
            canvas.drawColor(Color.parseColor("#212121"))
            imageBar?.draw(canvas, paint)
            time++
            animator.animate {
                imageBar?.update {
                    animator.stop()
                    when(it) {
                        1f -> view.imageBarListener?.onImageCreated?.invoke()
                        0f -> view.imageBarListener?.onImageDestroyed?.invoke()
                    }
                }
            }
        }
        fun handleTap() {
            imageBar?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity: Activity, bitmap: Bitmap):ImageBarView {
            val view = ImageBarView(activity, bitmap)
            activity.setContentView(view)
            return view
        }
    }
    data class ImageBarListener(var onImageCreated: () -> Unit, var onImageDestroyed: () -> Unit)
}