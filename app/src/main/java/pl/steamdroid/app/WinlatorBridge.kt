package pl.steamdroid.app

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

object WinlatorBridge {
    const val PACKAGE_NAME = "com.winlator"
    const val OFFICIAL_RELEASES_URL = "https://github.com/brunodev85/winlator/releases"

    data class RuntimeStatus(
        val installed: Boolean,
        val versionName: String?,
        val versionCode: Long?
    )

    fun inspect(context: Context): RuntimeStatus {
        return try {
            val info = context.packageManager.getPackageInfo(
                PACKAGE_NAME,
                PackageManager.PackageInfoFlags.of(0)
            )
            RuntimeStatus(
                installed = true,
                versionName = info.versionName,
                versionCode = info.longVersionCode
            )
        } catch (_: PackageManager.NameNotFoundException) {
            RuntimeStatus(
                installed = false,
                versionName = null,
                versionCode = null
            )
        }
    }

    fun launch(context: Context): Boolean {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(PACKAGE_NAME)
            ?: return false

        return try {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }
}
