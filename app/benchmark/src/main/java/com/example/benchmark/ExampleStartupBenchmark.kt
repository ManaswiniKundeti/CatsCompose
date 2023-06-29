package com.example.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example.catscompose",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }

    @Test
    fun scrollList() = benchmarkRule.measureRepeated(
        packageName = "com.example.catscompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        scrollDownList()
    }

    @Test
    fun scrollClickAndNavigate() = benchmarkRule.measureRepeated(
        packageName = "com.example.catscompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        scrollDownListAndNavigateToDetails()
    }

    fun MacrobenchmarkScope.scrollDownList() {
        // try to access using test tags. Add them to composable views to identify
        val list = device.findObject(By.res("cats_list"))

        device.waitForIdle()

        list.setGestureMargin(device.displayWidth / 5)

        repeat(3) {
            list.fling(Direction.DOWN)
        }

        device.waitForIdle()
    }

    fun MacrobenchmarkScope.scrollDownListAndNavigateToDetails() {
        // try to access using test tags. Add them to composable views to identify
        val list = device.findObject(By.res("cats_list"))

        device.waitForIdle()

        list.setGestureMargin(device.displayWidth / 5)

        repeat(2) {
            list.fling(Direction.DOWN)
        }

        device.findObject(By.res("card")).click()

        device.waitForIdle()
    }
}

/**
 * RESULTS : June 29,2023
 *
 * Starting 3 tests on Pixel 5 - 12

ExampleStartupBenchmark_startup
timeToInitialDisplayMs   min 294.3,   median 313.5,   max 329.5
Traces: Iteration 0 1 2 3 4

ExampleStartupBenchmark_scrollClickAndNavigate
frameDurationCpuMs   P50    4.8,   P90   11.8,   P95   14.8,   P99   58.1
frameOverrunMs   P50   -8.9,   P90   -2.1,   P95    7.3,   P99   58.7
Traces: Iteration 0 1 2 3 4
Timed out waiting for process (com.example.catscompose) to appear on google-pixel_5-0B261FDD4004K7.

ExampleStartupBenchmark_scrollList
frameDurationCpuMs   P50    9.2,   P90   12.9,   P95   14.6,   P99   36.6
frameOverrunMs   P50   -4.6,   P90   -1.0,   P95    2.1,   P99   31.8
Traces: Iteration 0 1 2 3 4

BUILD SUCCESSFUL in 3m 51s

 */