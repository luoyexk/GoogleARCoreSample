package com.zwl.arcore.sample.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.zwl.arcore.sample.R
import kotlinx.android.synthetic.main.layout_select_cube.*

/**
 * Create: 2019/3/31  11:03
 * version:
 * desc:
 *
 * @author Zouweilin
 */
class CreateCubeActivity : AppCompatActivity() {

    private val defaultLength = 0.04F

    private var cube: Renderable? = null
    private var cylinder: Renderable? = null
    private var sphere: Renderable? = null
    private var cubeType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_cube)
        val fragment = supportFragmentManager.findFragmentById(R.id.arFragment)
        addIncludeLayout()
        createCube()
        sceneTouch(fragment as ArFragment)
    }

    private fun addIncludeLayout() {
        val inflate = View.inflate(this, R.layout.layout_select_cube, null)
        val view: View = window.decorView/*fragment.view*/
        if (view is FrameLayout) {
            val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.gravity = Gravity.BOTTOM
            inflate.layoutParams = layoutParams
            view.addView(inflate)

            rg.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbCube -> cubeType = 0
                    R.id.rbCylinder -> cubeType = 1
                    R.id.rbSphere -> cubeType = 2
                }
            }
        }
    }

    /*
     * generate a cube in plane with many white dots when click on scene
     */
    private fun sceneTouch(fragment: ArFragment) {
        fragment.setOnTapArPlaneListener { hitResult, _, _ ->

            // create anchor on finger touch down
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(fragment.arSceneView.scene)

            val node = TransformableNode(fragment.transformationSystem)
            node.setParent(anchorNode)
            node.select()
            when (cubeType) {
                0 -> node.renderable = cube
                1 -> node.renderable = cylinder
                2 -> node.renderable = sphere
            }
        }

    }

    private fun createCube() {
        // generate a cube of red color
        MaterialFactory.makeOpaqueWithColor(this, Color(255F, 0F, 0F))
                .thenAccept { material: Material? ->
                    cube = ShapeFactory.makeCube(Vector3(defaultLength, defaultLength, defaultLength), Vector3.zero(), material)
                }
        MaterialFactory.makeOpaqueWithColor(this, Color(0F, 255F, 0F))
                .thenAccept { material: Material? ->
                    cylinder = ShapeFactory.makeCylinder(defaultLength / 2, defaultLength, Vector3(0F, defaultLength / 2, 0F), material)
                }
        // generate a transparent sphere with blue color
        MaterialFactory.makeTransparentWithColor(this, Color(0F, 0F, 255F, 0.7F))
                .thenAccept { material: Material? ->
                    sphere = ShapeFactory.makeSphere(defaultLength, Vector3.zero(), material)
                    // false: hide cube shadow
                    sphere?.isShadowCaster = false
                }
    }

}