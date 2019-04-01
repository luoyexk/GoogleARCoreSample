package com.zwl.arcore.sample.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.zwl.arcore.sample.R
import kotlinx.android.synthetic.main.layout_seek_bar.*

/**
 * Create: 2019/3/31  22:13
 * version:
 * desc:
 *
 * @author Zouweilin
 */
class ScaleCubeActivity : AppCompatActivity() {

    private val defaultLength = 0.04F
    private var node: Node? = null
    private var vector = Vector3(defaultLength, defaultLength, defaultLength)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_cube)
        val fragment = supportFragmentManager.findFragmentById(R.id.arFragment)
        addIncludeLayout()
        sceneTouch(fragment as ArFragment)
    }

    private fun sceneTouch(fragment: ArFragment) {
        fragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(fragment.arSceneView.scene)
            if (node == null) {
                node = Node()
                node?.setParent(anchorNode)
                createCube()
            }
        }
    }

    private fun addIncludeLayout() {
        View.inflate(this, R.layout.layout_seek_bar, window.decorView as ViewGroup)
        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val max = seekBar!!.max
                val percent = (progress.toFloat() - max / 2) / (max / 2)
                val result = defaultLength * (1 + percent)
                when (seekBar) {
                    seek1 -> vector.x = result
                    seek2 -> vector.y = result
                    seek3 -> vector.z = result
                }
                createCube()
            }
        }
        seek1.setOnSeekBarChangeListener(listener)
        seek2.setOnSeekBarChangeListener(listener)
        seek3.setOnSeekBarChangeListener(listener)
    }

    private fun createCube() {
        MaterialFactory.makeOpaqueWithColor(this, Color(255F, 0F, 0F))
                .thenAccept { material ->
                    val cube = ShapeFactory.makeCube(vector, Vector3(0F, vector.y / 2, 0F), material)
                    node?.renderable = cube
                }
    }
}