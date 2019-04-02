package com.zwl.arcore.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zwl.arcore.sample.ui.CreateCubeActivity
import com.zwl.arcore.sample.ui.RotateCubeActivity
import com.zwl.arcore.sample.ui.ScaleCubeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PermissionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
