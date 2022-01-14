package uz.pdp.smartstaff

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import uz.pdp.smartstaff.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            brush.setOnClickListener {
                image.isClicked = true
            }

            pen.setOnClickListener {
                image.isClicked = false
            }

            color.setOnClickListener {
                ColorPickerDialog
                    .Builder(this@MainActivity)
                    .setTitle("Pick Theme")
                    .setColorShape(ColorShape.CIRCLE)
                    .setColorListener { color, colorHex ->
                        if (image.isClicked) {
                            image.color = colorHex
                            Toast.makeText(this@MainActivity, "$colorHex", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            image.changePaintColor(colorHex)
                        }
                    }
                    .show()
            }

        }
    }

}