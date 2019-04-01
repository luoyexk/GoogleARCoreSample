package com.zwl.arcore.sample

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zwl.arcore.sample.helper.CameraPermissionHelper

/**
 * Create: 2019/3/30  18:42
 * version:
 * desc:
 *
 * @author Zouweilin
 */
abstract class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CameraPermissionHelper.requestCameraPermission(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                showRationaleDialog()
            }
//            finish()
        }
    }


    private fun showRationaleDialog() {
        val positiveBtn = DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
            CameraPermissionHelper.launchPermissionSettings(this)
        }
        AlertDialog.Builder(this)
                .setTitle("Authorization")
                .setMessage("Google ARCore need camera.")
                .setPositiveButton(android.R.string.ok, positiveBtn)
//                .setOnDismissListener { finish() }
                .show()
    }

    fun startActivity(context: Context) {

    }

//    fun checkPermission() {
//        val GRANTE = checkSelfPermission(android.Manifest.permission.CAMERA)
//    }
}