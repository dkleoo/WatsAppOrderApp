package com.example.watsapporder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WatsAppOrderMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data[TITLE_KEY]
            ?: remoteMessage.data[ORDER_NUMBER_KEY]?.let { ORDER_TITLE_FORMAT.format(it) }
            ?: return
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data[BODY_KEY]
            ?: return
        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
    }

    private fun showNotification(title: String, body: String) {
        createChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NotificationManagerCompat.from(this).notify(body.hashCode(), notification)
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        manager.createNotificationChannel(channel)
    }

    private companion object {
        const val CHANNEL_ID = "watsapporder_orders"
        const val CHANNEL_NAME = "Pedidos"
        const val TITLE_KEY = "title"
        const val BODY_KEY = "body"
        const val ORDER_NUMBER_KEY = "orderNumber"
        const val ORDER_TITLE_FORMAT = "Nuevo pedido #%s"
    }
}
