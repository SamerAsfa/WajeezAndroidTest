package com.blackapp.wajeezandroiddevelopertask.common.extension_fun

import android.app.Activity
import android.view.View
import com.blackapp.tenders.business.domain.util.SuccessAnimation
import com.blackapp.tenders.business.domain.util.TryAgainAnimation
import com.blackapp.tenders.business.domain.util.WaitingAnimation
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.concurrent.schedule

var waitingAnimation: WaitingAnimation? = null
var tryAgainAnimation: TryAgainAnimation? = null
var successAnimation: SuccessAnimation? = null

fun Activity.displayLoadingAnimation(activity: Activity, display: Boolean) {

    if (waitingAnimation == null) {
        waitingAnimation = WaitingAnimation(activity)
    }
    if (display) {
        waitingAnimation!!.show()
    } else {
        waitingAnimation!!.dismiss()
    }
}

fun Activity.displaySuccessAnimation(activity: Activity) {

    if (successAnimation == null) {
        successAnimation = SuccessAnimation(activity)
    }

    successAnimation!!.show()
    Timer().schedule(1800) {
        successAnimation!!.dismiss()

    }
}


fun Activity.displayTryAgainAnimation(activity: Activity) {

    if (tryAgainAnimation == null) {
        tryAgainAnimation = TryAgainAnimation(activity)
    }

    tryAgainAnimation!!.show()
    Timer().schedule(3900) {
        tryAgainAnimation!!.dismiss()

    }
}

fun Activity.displayError(activity: Activity, message: String?) {
    Snackbar.make(
        View(activity),
        message ?: "Unknown error.",
        Snackbar.LENGTH_LONG
    ).show()
}
