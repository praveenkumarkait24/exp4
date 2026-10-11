# Experiment 4: Medicine Reminder Application using SQLite and Alarm

## Aim
To develop an Android application that accepts a medicine name, date, and time of day (Morning, Afternoon, Evening or Night) from the user, stores this data in an SQLite database, and triggers an alarm at the corresponding date and time to display the medicine name.

## Objectives
- To create and use an SQLite database via SQLiteOpenHelper.
- To capture a date using DatePickerDialog and a category using Spinner.
- To schedule a one-time alarm using AlarmManager and PendingIntent.
- To receive and handle the alarm using a BroadcastReceiver.

## Procedure / Algorithm
1. Create Project: Create a new Android Studio project named MedicineApp using Kotlin.
2. Design UI: Create activity_main.xml with input fields for Medicine Name, Date, Time of Day and button for SAVE MEDICINE.
3. Create Database: Create SQLiteHelper.kt by extending SQLiteOpenHelper.
4. Create Table: In onCreate(), create the medicine table with id, name, date, and timeOfDay.
5. Implement CRUD Operations: Insert medicine details using ContentValues.
6. Connect Activity: In MainActivity.kt, connect UI components with SQLiteHelper and implement button click events.
7. Schedule Alarm: Use AlarmManager to trigger AlarmReceiver at the scheduled time.
8. Run Application: Build and run the app on an emulator or Android device and test medicine reminder alarms.

## Source Code Files
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/example/medicineremainder/MainActivity.kt`
- `app/src/main/java/com/example/medicineremainder/AlarmReceiver.kt`
- `app/src/main/java/com/example/medicineremainder/SQLiteHelper.kt`
- `app/src/main/res/layout/activity_main.xml`

## Result
Thus, an Android application that stores medicine reminders in an SQLite database and triggers an alarm to display the medicine name at the scheduled date and time of day was successfully designed and implemented.
