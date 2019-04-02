package com.zwl.arcore.sample

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class App : Application() {

    companion object {
        var instance: App? = null
        private var toast: Toast? = null
        private val handler = Handler(Looper.getMainLooper())
        fun makeToast(content: String) {
            handler.post {
                toast?.setText(content)
                toast?.show()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
    }

}