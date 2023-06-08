package com.yds.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


fun AppCompatActivity.countDown(
    time: Int,
    next: (time: Int) -> Unit,
    end: () -> Unit
) {
    lifecycleScope.launch {
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }.collect {
            next(it)
            if (it == 0) {
                end()
            }
        }
    }
}