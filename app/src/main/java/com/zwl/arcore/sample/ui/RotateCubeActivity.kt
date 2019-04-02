package com.zwl.arcore.sample.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.zwl.arcore.sample.App
import com.zwl.arcore.sample.R
import kotlinx.android.synthetic.main.layout_seek_bar.*

/**
 * Create: 2019/4/2  9:17
 * version:
 * desc:
 *
 * @author Zouweilin
 */
class RotateCubeActivity : AppCompatActivity() {
    // Rate that the node rotates in degrees per degree of twisting.
    private var rotationRateDegrees = 1f
    private val defaultLength = 0.04F
    private var node: Node? = null
    private var vector = Vector3(defaultLength * 3, defaultLength * 2, defaultLength)
    private var quaternionDefault = Quaternion()
    private var lastX = 0F
    private var lastY = 0F
    private var lastZ = 0F
    private var alreadyRotate = 0f
    private lateinit var demo1: RotateRunnable
    private lateinit var demo2: RotateRunnable
    private lateinit var demo3: RotateRunnable

    private val function: (HitTestResult, MotionEvent) -> Unit = { _, _ ->
        val tapRotateDegree = 30f
        rotate(Vector3.up(), tapRotateDegree, node!!)
        alreadyRotate += tapRotateDegree
        App.makeToast("already rotate $alreadyRotate")
        if (alreadyRotate == 360f) alreadyRotate = 0f
    }

    private val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            node ?: return
            val degree = 1f
            when (seekBar) {
                seek1 -> {
                    rotate(Vector3.right(), if (progress > lastX) degree else -degree, node!!)
                    lastX = progress.toFloat()
                }
                seek2 -> {
                    rotate(Vector3.up(), if (progress > lastY) degree else -degree, node!!)
                    lastY = progress.toFloat()
                }
                seek3 -> {
                    rotate(Vector3.forward(), if (progress > lastZ) degree else -degree, node!!)
                    lastZ = progress.toFloat()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_cube)
        val fragment = supportFragmentManager.findFragmentById(R.id.arFragment)
        addIncludeLayout()
        sceneTouch(fragment as ArFragment)
        prepareDemo()
    }

    private fun addIncludeLayout() {
        View.inflate(this, R.layout.layout_seek_bar, window.decorView as ViewGroup)
        seek1.setOnSeekBarChangeListener(listener)
        seek2.setOnSeekBarChangeListener(listener)
        seek3.setOnSeekBarChangeListener(listener)
        initBar()

        tv1.text = getString(R.string.rotate_x)
        tv2.text = getString(R.string.rotate_y)
        tv3.text = getString(R.string.rotate_z)

        play.visibility = View.VISIBLE
        play.setOnClickListener { startRotateDemo() }
    }

    private fun initBar() {
        val degree = 360
        seek1.max = degree
        seek2.max = degree
        seek3.max = degree
        seek1.progress = 0
        seek2.progress = 0
        seek3.progress = 0
    }

    private fun prepareDemo() {
        demo1 = RotateRunnable(seek1)
        demo2 = RotateRunnable(seek2)
        demo3 = RotateRunnable(seek3)
    }

    private fun startRotateDemo() {
        node ?: return
        stopRotateDemo()
        initBar()
        node?.localRotation = Quaternion(0F, 0F, 0F, 1F)
        val showTime = 16L * 180 + 1000L
        seek1.post(demo1)
        seek2.postDelayed(demo2, showTime)
        seek3.postDelayed(demo3, showTime * 2)
    }

    private fun stopRotateDemo() {
        seek1.removeCallbacks(demo1)
        seek1.removeCallbacks(demo2)
        seek1.removeCallbacks(demo3)
    }

    override fun onPause() {
        super.onPause()
        stopRotateDemo()
    }

    /**
     * detail see
     * class com.google.ar.sceneform.ux.RotationController
     * method onContinueTransformation
     */
    private fun rotate(axis: Vector3, degree: Float, node: Node) {
        val rotationAmount = degree * rotationRateDegrees
        val rotationDelta = Quaternion(axis, rotationAmount)
        var localrotation = node.localRotation
        localrotation = Quaternion.multiply(localrotation, rotationDelta)
        node.localRotation = localrotation
    }

    private fun sceneTouch(fragment: ArFragment) {
        fragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(fragment.arSceneView.scene)
            if (node == null) {
                node = Node()
                node!!.setParent(anchorNode)
                quaternionDefault = node!!.localRotation
                node!!.setOnTapListener(function)
                createCube()
            }
        }
    }

    private fun createCube() {
        MaterialFactory.makeOpaqueWithColor(this, Color(255F, 0F, 0F))
                .thenAccept { material ->
                    val cube = ShapeFactory.makeCube(vector, Vector3(0F, vector.y / 2, 0F), material)
                    node?.renderable = cube
                }
    }

    private class RotateRunnable(val seekBar: SeekBar) : Runnable {
        override fun run() {
            seekBar.progress = seekBar.progress + 1
            if (seekBar.progress < 180) {
                seekBar.postDelayed(this, 16L)
            }
        }
    }
}