package com.example.medicineremainder

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var etMedicineName: EditText
    private lateinit var tvDate: TextView
    private lateinit var spinnerTimeOfDay: Spinner
    private lateinit var btnSave: Button
    private lateinit var dbHelper: SQLiteHelper
    private var selectedDate: Calendar? = null

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etMedicineName = findViewById(R.id.etMedicineName)
        tvDate = findViewById(R.id.tvDate)
        spinnerTimeOfDay = findViewById(R.id.spinnerTimeOfDay)
        btnSave = findViewById(R.id.btnSave)

        dbHelper = SQLiteHelper(this)

        setupTimeSpinner()
        requestNotificationPermission()

        tvDate.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            saveMedicine()
        }
    }

    private fun setupTimeSpinner() {
        val timeOptions = arrayOf(
            "Morning",
            "Afternoon",
            "Evening",
            "Night"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            timeOptions
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerTimeOfDay.adapter = adapter
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(
                    year,
                    month,
                    dayOfMonth
                )
                selectedDate = selectedCalendar

                val dateFormat = SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                )
                tvDate.text = dateFormat.format(
                    selectedCalendar.time
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun saveMedicine() {
        val medicineName = etMedicineName.text.toString().trim()

        if (medicineName.isEmpty()) {
            Toast.makeText(
                this,
                "Please enter Medicine Name",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (selectedDate == null) {
            Toast.makeText(
                this,
                "Please select Date",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val timeOfDay = spinnerTimeOfDay.selectedItem.toString()

        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        )
        val date = dateFormat.format(
            selectedDate!!.time
        )

        val result = dbHelper.insertMedicine(
            medicineName,
            date,
            timeOfDay
        )

        if (result != -1L) {
            scheduleAlarm(
                medicineName,
                timeOfDay
            )
            Toast.makeText(
                this,
                "Medicine Saved Successfully",
                Toast.LENGTH_SHORT
            ).show()
            etMedicineName.text.clear()
        } else {
            Toast.makeText(
                this,
                "Failed to save medicine",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun scheduleAlarm(
        medicineName: String,
        timeOfDay: String
    ) {
        val alarmTime = Calendar.getInstance()
        val selected = selectedDate!!

        alarmTime.set(
            selected.get(Calendar.YEAR),
            selected.get(Calendar.MONTH),
            selected.get(Calendar.DAY_OF_MONTH)
        )

        when (timeOfDay) {
            "Morning" -> {
                alarmTime.set(Calendar.HOUR_OF_DAY, 8)
                alarmTime.set(Calendar.MINUTE, 0)
            }
            "Afternoon" -> {
                alarmTime.set(Calendar.HOUR_OF_DAY, 13)
                alarmTime.set(Calendar.MINUTE, 0)
            }
            "Evening" -> {
                alarmTime.set(Calendar.HOUR_OF_DAY, 18)
                alarmTime.set(Calendar.MINUTE, 0)
            }
            "Night" -> {
                alarmTime.set(Calendar.HOUR_OF_DAY, 21)
                alarmTime.set(Calendar.MINUTE, 0)
            }
        }

        alarmTime.set(Calendar.SECOND, 0)
        alarmTime.set(Calendar.MILLISECOND, 0)

        if (alarmTime.timeInMillis <= System.currentTimeMillis()) {
            Toast.makeText(
                this,
                "Selected date and time has already passed",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val intent = Intent(
            this,
            AlarmReceiver::class.java
        )
        intent.putExtra(
            "MEDICINE_NAME",
            medicineName
        )

        val requestCode = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(
                    this,
                    "Please allow exact alarms in Settings",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            pendingIntent
        )

        Toast.makeText(
            this,
            "Alarm scheduled for $timeOfDay",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
