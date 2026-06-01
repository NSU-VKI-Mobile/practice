package com.example.app.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.calculations.CalculationActivity
import com.example.domain.interfaces.CalculationsNavigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCalculationsNavigator @Inject constructor() : CalculationsNavigator {
    override fun navigateToNewCalculation(context: Context, userId: Long) {
        val intent = Intent(context, CalculationActivity::class.java).apply {
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        val intent = Intent(context, CalculationActivity::class.java).apply {
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        val intent = Intent(activity, CalculationActivity::class.java).apply {
            putExtra("userId", userId)
        }
        activity.startActivityForResult(intent, 1001)
    }
}