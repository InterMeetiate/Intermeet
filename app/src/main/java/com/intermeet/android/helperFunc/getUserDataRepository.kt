package com.intermeet.android.helperFunc

import android.app.Activity
import com.intermeet.android.SignUp_SignIn.IntermeetApp
import com.intermeet.android.SignUp_SignIn.UserDataRepository

fun Activity.getUserDataRepository(): UserDataRepository = (application as IntermeetApp).userDataRepository