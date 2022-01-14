package uz.pdp.smartstaff

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.widget.ImageView
import java.util.*

class FloodFill {

    private val fillType = FillType()

    fun executeFloodFilling2(method: Int, view: ImageView, event: MotionEvent, time: Int): Bitmap {
        val viewCoords = IntArray(2)
        view.getLocationOnScreen(viewCoords)
        val absX = event.rawX
        val absY = event.rawY

        val imgX = absX - viewCoords[0]
        val imgY = absY - viewCoords[1]

        val maxImgX = view.width
        val maxImgY = view.height

        var bitmap : Bitmap
        try {
            bitmap = (view.drawable as BitmapDrawable).bitmap
        } catch (e: kotlin.TypeCastException) {
            e.printStackTrace()
            bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
            return bitmap
        }

        val maxX = bitmap.width
        val maxY = bitmap.height

        val x = (maxX * imgX / maxImgX.toFloat()).toInt()
        val y = (maxY * imgY / maxImgY.toFloat()).toInt()

        val color = bitmap.getPixel(x, y)
        val isBlack = color == Color.BLACK

        val replacementColor = if (isBlack) Color.RED else Color.YELLOW

        fillType.useImage(bitmap, time)
        fillType.floodFill(method, x, y, color, replacementColor)

        return bitmap

    }

    fun executeFloodFilling1(method: Int, bitmap: Bitmap, x: Int, y: Int, time: String): Bitmap {

        val color = bitmap.getPixel(x, y)
        val replacementColor = Color.parseColor(time)

        fillType.useImage(bitmap, 0)
        fillType.floodFill(method, x, y, color, replacementColor)

        return bitmap

    }


    fun generateRandomBitmaps(x: Int, y: Int): Array<Bitmap> {
        val bmp1 = generateRandomBitmap(x, y)
        val bmp2 = generateRandomBitmap(x, y)
        return arrayOf(bmp1, bmp2)
    }

    fun generateRandomBitmap(width: Int, height: Int): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (i in 0 until width) {
            for (j in 0 until height) {
                val color = when ((0..1).random()) {
                    0 -> Color.BLACK
                    1 -> Color.WHITE
                    else -> Color.BLACK
                }
                bmp.setPixel(i, j, color)
            }
        }
        return bmp
    }

    private fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start
}