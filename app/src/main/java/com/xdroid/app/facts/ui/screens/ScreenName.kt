package com.xdroid.app.facts.ui.screens


object ScreenName {
    const val Login = "login"
    const val OTP = "otp"
    const val Home = "home"
    const val SignUp = "signup"
    const val SignupSecond = "signUpSecond"

    const val Recharge = "Recharge"
    const val Offer = "Offer"
    const val Profile = "Profile"
    const val Detail = "Detail"
    const val QuoteScreen = "QuoteScreen"
    const val RandomScreen = "RandomScreen"

    //Services
    const val NavigationScreen = "NavigationScreen"
    const val SimpleInterest = "SimpleInterest"
    const val TimeConverter = "TimeConverter"

    fun detailRoute(name: String, url: String): String {
        return "$name?url=$url"
    }

}