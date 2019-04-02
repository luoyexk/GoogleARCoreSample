package com.zwl.arcore.sample.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
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

    private val defaultLength = 0.04F
    private var node: Node? = null
    private var vector = Vector3(defaultLength * 3, defaultLength * 2, defaultLength)
    private var quaternionDefault = Quaternion()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_cube)
        val fragment = supportFragmentManager.findFragmentById(R.id.arFragment)
        addIncludeLayout()
        sceneTouch(fragment as ArFragment)
    }

    private fun addIncludeLayout() {
        View.inflate(this, R.layout.layout_seek_bar, window.decorView as ViewGroup)
        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // todo 旋转角度既不是度数也不是弧度？
                val max = seekBar!!.max
                val percent = (progress.toFloat() - max / 2) / (max / 2)
                val result = (progress * Math.PI / 180).toFloat()
                val tmpQuaternion = Quaternion(quaternionDefault)
//                val result = 1 + percent
                when (seekBar) {
                    seek1 -> tmpQuaternion.x = result
                    seek2 -> tmpQuaternion.y = result
                    seek3 -> tmpQuaternion.z = result
                }
                rotateCube(tmpQuaternion)
            }
        }
        seek1.setOnSeekBarChangeListener(listener)
        seek2.setOnSeekBarChangeListener(listener)
        seek3.setOnSeekBarChangeListener(listener)
        val degree = 360
        seek1.max = degree
        seek2.max = degree
        seek3.max = degree

        seek1.progress = 0
        seek2.progress = 0
        seek3.progress = 0

    }

    private fun sceneTouch(fragment: ArFragment) {
        fragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(fragment.arSceneView.scene)
            if (node == null) {
                node = Node()
                node!!.setParent(anchorNode)
                quaternionDefault = node!!.localRotation
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

    private fun rotateCube(newRotation: Quaternion) {
        if (node != null) {
            node!!.localRotation = newRotation
        }

    }
}