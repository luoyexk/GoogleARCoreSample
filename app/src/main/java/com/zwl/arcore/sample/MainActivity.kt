package com.zwl.arcore.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.ux.TwistGesture
import com.zwl.arcore.sample.helper.Reflect
import com.zwl.arcore.sample.ui.CreateCubeActivity
import com.zwl.arcore.sample.ui.RotateCubeActivity
import com.zwl.arcore.sample.ui.ScaleCubeActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Array.setInt
import java.lang.reflect.AccessibleObject.setAccessible
import java.lang.reflect.Field
import java.lang.reflect.Modifier


class MainActivity : PermissionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            Reflect.change()
        } catch (e: Exception) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClick(view: View) {
        when (view) {
            createCube -> start(this, CreateCubeActivity::class.java)
            scaleCube -> start(this, ScaleCubeActivity::class.java)
            rotateCube -> start(this, RotateCubeActivity::class.java)
        }
    }

    private fun <T : AppCompatActivity> start(mainActivity: MainActivity, c: Class<T>) {
        val intent = Intent(this, c)
        mainActivity.startActivity(intent)
    }
}
