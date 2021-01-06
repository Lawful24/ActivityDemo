package com.appplanet.activitydemo.network

interface ServerResponseListener<in T> {
    fun getResult(result: T?)
}