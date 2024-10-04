package com.ivan.spaceflightnews.jobs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ivan.spaceflightnews.common.LoginCore
import com.ivan.spaceflightnews.screens.utils.Constants
import timber.log.Timber

class LogoutAfterTenMinutesWork(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val loginCore: LoginCore
):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // This only logs the user out of the Application Layer by removing the ID Token.
        loginCore.deleteUserIDToken()
        with(NotificationManagerCompat.from(appContext)) {
            if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                val notifBuilder = NotificationCompat.Builder(appContext, Constants.NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(appContext.getString(com.ivan.spaceflightnews.R.string.notif_title))
                    .setContentText(appContext.getString(com.ivan.spaceflightnews.R.string.notif_text))
                    .setSmallIcon(com.ivan.spaceflightnews.R.drawable.ic_launcher_foreground)
                    .setChannelId(Constants.NOTIFICATION_CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                notify(Constants.NOTIFICATION_ID, notifBuilder.build())
            }
        }

        return Result.success()
    }

}