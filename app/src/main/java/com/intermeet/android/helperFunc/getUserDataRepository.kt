package com.intermeet.android.helperFunc

import android.app.Activity
import com.intermeet.android.IntermeetApp
import com.intermeet.android.UserDataRepository

fun Activity.getUserDataRepository(): UserDataRepository = (application as IntermeetApp).userDataRepository