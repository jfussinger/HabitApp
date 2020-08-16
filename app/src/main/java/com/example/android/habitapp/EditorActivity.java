package com.example.android.habitapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habitapp.data.HabitContract.HabitEntry;
import com.example.android.habitapp.data.HabitDbHelper;

/**
 * Allows user to create a new habit or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the habit's name */
    private EditText NameEditText;

    /** EditText field to enter the habit's day of week */
    private EditText DayOfWeekEditText;

    /** EditText field to enter the habit's frequency */
    private EditText FrequencyEditText;

    /** EditText field to enter the habit's time of day */
    private Spinner TimeOfDaySpinner;

    /**
     * Gender of the habit. The possible valid values are in the HabitContract.java file:
     * {@link HabitEntry#TIMEOFDAY_MORNING}, {@link HabitEntry#TIMEOFDAY_AFTERNOON}, or
     * {@link HabitEntry#TIMEOFDAY_EVENING}.
     */
    private int TimeOfDay = HabitEntry.TIMEOFDAY_MORNING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        NameEditText = (EditText) findViewById(R.id.edit_habit_name);
        DayOfWeekEditText = (EditText) findViewById(R.id.edit_habit_dayOfWeek);
        FrequencyEditText = (EditText) findViewById(R.id.edit_habit_frequency);
        TimeOfDaySpinner = (Spinner) findViewById(R.id.spinner_timeOfDay);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the time of day of the habit.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter timeOfDaySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_timeOfDay_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        timeOfDaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        TimeOfDaySpinner.setAdapter(timeOfDaySpinnerAdapter);

        // Set the integer mSelected to the constant values
        TimeOfDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.timeOfDay_afternoon))) {
                        TimeOfDay = HabitEntry.TIMEOFDAY_AFTERNOON;
                    } else if (selection.equals(getString(R.string.timeOfDay_evening))) {
                        TimeOfDay = HabitEntry.TIMEOFDAY_EVENING;
                    } else {
                        TimeOfDay = HabitEntry.TIMEOFDAY_MORNING;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TimeOfDay = HabitEntry.TIMEOFDAY_MORNING;
            }
        });
    }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = NameEditText.getText().toString().trim();
        String dayOfWeekString = DayOfWeekEditText.getText().toString().trim();
        String frequencyString = FrequencyEditText.getText().toString().trim();
        int frequency = Integer.parseInt(frequencyString);

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_DAYOFWEEK, dayOfWeekString);
        values.put(HabitEntry.COLUMN_HABIT_TIMEOFDAY, TimeOfDay);
        values.put(HabitEntry.COLUMN_HABIT_FREQUENCY, frequency);

        // Insert a new pet into the provider, returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert(HabitEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_habit_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_habit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit ();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
