package jp.co.recruit_mp.android.lightcalendarview.kotlinsample

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.co.recruit_mp.android.lightcalendarview.LightCalendarView
import jp.co.recruit_mp.android.lightcalendarview.accent.Accent
import jp.co.recruit_mp.android.lightcalendarview.accent.DotAccent
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var calendarView: LightCalendarView

    private val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView) as? LightCalendarView
                ?: throw IllegalStateException("calendarView not found")

        calendarView.apply {
            displayOutside = true
            fixToday = true
            monthFrom = Calendar.getInstance().apply { set(Calendar.YEAR, 2014); set(Calendar.MONTH, 0) }.time
            monthTo = Calendar.getInstance().apply { add(Calendar.MONTH, 24) }.time
            monthCurrent = Calendar.getInstance().time

            val r = Random(System.currentTimeMillis())
            Handler().postDelayed({
                val cal = Calendar.getInstance()
                cal.set(Calendar.MONTH, 0)
                cal.set(Calendar.DAY_OF_MONTH, 1)
                val dates = (1..365).map {
                    cal.apply {
                        cal.add(Calendar.DAY_OF_MONTH, r.nextInt(4))
                    }.time
                }
                val map = mutableMapOf<Date, Collection<Accent>>().apply {
                    dates.forEach { date ->
                        val accents = (0..r.nextInt(5)).map { DotAccent(10f, key = "${formatter.format(date)}-$it") }
                        put(date, accents)
                    }
                }
                accents.putAll(map)
                updateAccents()
            }, 2000)

            // set the calendar view callbacks
            onMonthSelected = { date, view ->
                supportActionBar?.apply {
                    title = formatter.format(date).capitalize()
                }


                Log.i("MainActivity", "onMonthSelected: date = $date")
            }

            onDateSelected = { date -> Log.i("MainActivity", "onDateSelected: date = $date") }
        }


        // change the actionbar title
        supportActionBar?.title = formatter.format(calendarView.monthCurrent)
    }
}
