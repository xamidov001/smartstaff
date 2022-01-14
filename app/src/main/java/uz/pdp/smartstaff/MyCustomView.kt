package uz.pdp.smartstaff

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs

class MyCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private var path: Path = Path()
    private var paint: Paint = Paint()
    private val touchLerance = ViewConfiguration.get(context).scaledEdgeSlop
    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f
    var isClicked = false
    var color = "#FFC6FF00"
    private val presenter = FloodFill()

    init {
        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.isDither = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, 0f, 0f, paint)

    }

    fun changePaintColor(s: String) {
        paint.color = Color.parseColor(s)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionX = event.x
        motionY = event.y
        if (!isClicked) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchDown()
                }
                MotionEvent.ACTION_MOVE -> {
                    touchMove()
                }
                MotionEvent.ACTION_UP -> {
                    touchUp()
                }
            }
        } else {
            val viewCoords = IntArray(2)
            getLocationOnScreen(viewCoords)
            val absX = event.rawX
            val absY = event.rawY

            val imgX = absX - viewCoords[0]
            val imgY = absY - viewCoords[1]

            val maxImgX = width
            val maxImgY = height

            val maxX = bitmap.width
            val maxY = bitmap.height

            val x = (maxX * imgX / maxImgX.toFloat()).toInt()
            val y = (maxY * imgY / maxImgY.toFloat()).toInt()

            touchUser(event)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.action == MotionEvent.ACTION_DOWN }
                .observeOn(Schedulers.computation())
                .map {
                    presenter.executeFloodFilling1(
                        0,
                        bitmap,
                        x,
                        y,
                        color
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ showResult(it) }, { it.printStackTrace() })
        }

        invalidate()
        return true
    }

    private fun showResult(it: Bitmap) {
        bitmap = it
    }

    private fun touchUser(event: MotionEvent): Observable<MotionEvent> = Observable.create { emmit ->
        emmit.onNext(event)
        true

    }

    private fun touchUp() {
        path.reset()
    }

    private fun touchMove() {
        val absX = abs(motionX - currentX)
        val absY = abs(motionY - currentY)
        if (touchLerance <= absX || absY >= touchLerance) {
            path.quadTo(currentX, currentY, (motionX + currentX) / 2, (motionY + currentY) / 2)
            currentX = motionX
            currentY = motionY
            canvas.drawPath(path, paint)
        }
    }

    private fun touchDown() {
        path.reset()
        path.moveTo(motionX, motionY)
        currentX = motionX
        currentY = motionY
    }

}