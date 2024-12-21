package com.example.aplikacja_android.Notifications

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit


fun scheduleReminder(context: Context, title: String, description: String, delayInMinutes: Long) {
    val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delayInMinutes, TimeUnit.MINUTES)
        .setInputData(workDataOf("title" to title, "description" to description))
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}