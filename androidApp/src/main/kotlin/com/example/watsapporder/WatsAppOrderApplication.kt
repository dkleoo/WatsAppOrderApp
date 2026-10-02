package com.example.watsapporder

import android.app.Application
import com.example.watsapporder.platform.ActivityHolder

class WatsAppOrderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ActivityHolder.application = this
    }
}
