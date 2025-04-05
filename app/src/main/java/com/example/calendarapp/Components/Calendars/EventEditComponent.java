package com.example.calendarapp.Components.Calendars;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ThemeUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventEditComponent extends LinearLayout implements IComponent {

    private EditText etEventTitle, etEventDescription, etStartDate, etEndDate, etStartTime, etEndTime;
    private Button btnSaveEvent;
    private Calendar startCalendar, endCalendar;
    private SimpleDateFormat dateFormat, timeFormat;
    private TextInputLayout tilEventTitle;
    private TextInputLayout tilEventDescription;
    private TextInputLayout tilEndTime;
    private Spinner spinnerEventColor;
    private View viewColorPreview;
    private int selectedColorAttr;
    private String selectedColorName;

    // Predefined color names and IDs
    private static final int[] eventColors = ThemeUtils.eventColorsID;
    private static final String[] colorNames = ThemeUtils.eventColorsNames;

    public EventEditComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public EventEditComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public EventEditComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        LayoutInflater.from(context).inflate(R.layout.event_edit_component, this, true);

        tilEventTitle = findViewById(R.id.tilEventTitle);
        tilEventDescription = findViewById(R.id.tilEventDescription);
        tilEndTime =  findViewById(R.id.tilEndTime);

        etEventTitle = findViewById(R.id.etEventTitle);
        etEventDescription = findViewById(R.id.etEventDescription);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);
        spinnerEventColor = findViewById(R.id.spinnerEventColor);
        viewColorPreview = findViewById(R.id.viewColorPreview);



        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Date Picker Listeners
        etStartDate.setOnClickListener(v -> showDatePickerDialog(context, startCalendar, etStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(context, endCalendar, etEndDate));

        // Time Picker Listeners
        etStartTime.setOnClickListener(v -> showTimePickerDialog(context, startCalendar, etStartTime));
        etEndTime.setOnClickListener(v -> showTimePickerDialog(context, endCalendar, etEndTime));

        btnSaveEvent.setOnClickListener(v -> saveEvent(context));

        setupColorPicker(context);
    }

    // Method to display DatePickerDialog
    private void showDatePickerDialog(Context context, Calendar calendar, EditText dateEditText) {
        new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            dateEditText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showTimePickerDialog(Context context, Calendar calendar, EditText targetEditText) {
        new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            targetEditText.setText(timeFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    // Method to handle event saving
    private void saveEvent(Context context) {
        boolean isValid = true;
        String title = etEventTitle.getText().toString().trim();
        String description = etEventDescription.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();

        // Reset errors before validation
        tilEventTitle.setError(null);
        tilEventDescription.setError(null);
        tilEndTime.setError(null);
        etEventTitle.setError(null);
        etEventDescription.setError(null);
        etStartDate.setError(null);
        etStartTime.setError(null);
        etEndDate.setError(null);
        etEndTime.setError(null);

        // Validate required fields
        if (title.isEmpty()) {
            tilEventTitle.setError("Title cannot be empty");
            isValid = false;
        }
        if (startDate.isEmpty()) {
            etStartDate.setError("Start date is required");
            isValid = false;
        }
        if (startTime.isEmpty()) {
            etStartTime.setError("Start time is required");
            isValid = false;
        }
        if (endDate.isEmpty()) {
            etEndDate.setError("End date is required");
            isValid = false;
        }
        if (endTime.isEmpty()) {
            etEndTime.setError("End time is required");
            isValid = false;
        }

        // Parse start and end times
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date parsedStartTime = timeFormat.parse(startTime);
            Date parsedEndTime = timeFormat.parse(endTime);

            if (parsedStartTime != null) {
                startCalendar.set(Calendar.HOUR_OF_DAY, parsedStartTime.getHours());
                startCalendar.set(Calendar.MINUTE, parsedStartTime.getMinutes());
            }
            if (parsedEndTime != null) {
                endCalendar.set(Calendar.HOUR_OF_DAY, parsedEndTime.getHours());
                endCalendar.set(Calendar.MINUTE, parsedEndTime.getMinutes());
            }

            // Ensure end date/time is not before start date/time
            if (endCalendar.before(startCalendar)) {
                tilEndTime.setError("End time cannot be before start time");
                isValid = false;;
            }


            // Create event object
            Event event = new Event(title, description, startCalendar.getTime(), endCalendar.getTime(), selectedColorName);

            // Ensure event's duration is at least 5 minutes
            if(event.getDurationMS()<=5*1000*60){
                tilEndTime.setError("Event's duration must be at least 5 minutes");
                isValid = false;
            }
            // Notify listener
            if(!isValid) return;

            if (listener != null) {
                listener.onEventSaved(event);
            }

        } catch (Exception e) {
            Toast.makeText(context, "Error parsing time: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    // Public method to set event details
    public void setEventDetails(Event event) {
        if (event == null) return;

        etEventTitle.setText(event.getTitle());
        etEventDescription.setText(event.getDescription());

        if (event.getStartDate() != null) {
            startCalendar.setTime(event.getStartDate());
            etStartDate.setText(dateFormat.format(event.getStartDate()));
            etStartTime.setText(timeFormat.format(event.getStartDate()));
        }

        if (event.getEndDate() != null) {
            endCalendar.setTime(event.getEndDate());
            etEndDate.setText(dateFormat.format(event.getEndDate()));
            etEndTime.setText(timeFormat.format(event.getEndDate()));
        }

        if (event.getColor() != null) {
            int colorIndex = 0;
            for (int i = 0; i < eventColors.length; i++) {
                if (colorNames[i].equals(event.getColor())) {
                    colorIndex = i;
                    break;
                }
            }
            spinnerEventColor.setSelection(colorIndex);
           // viewColorPreview.setBackgroundColor(event.getColor());
        }
    }
    // Static method to show the event edit dialog
    public static void openEditEventDialog(Context context, Event event, OnEventSaveListener callback) {
        EventEditComponent eventEditComponent = new EventEditComponent(context);

        if (event != null) {
            eventEditComponent.setEventDetails(event);
        }

        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(context);
        editDialogBuilder.setTitle(event == null ? "Create Event" : "Edit Event");
        editDialogBuilder.setView(eventEditComponent);
        editDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = editDialogBuilder.create();
        dialog.show();

        eventEditComponent.setOnEventSaveListener(savedEvent -> {
            if (callback != null) {
                callback.onEventSaved(savedEvent);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Event Saved: " + savedEvent.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Interface for event saving callback
    private OnEventSaveListener listener;

    public void setOnEventSaveListener(OnEventSaveListener listener) {
        this.listener = listener;
    }

    @FunctionalInterface
    public interface OnEventSaveListener {
        void onEventSaved(Event event);
    }
    // Setup the color picker spinner
    private void setupColorPicker(Context context) {


        selectedColorAttr = eventColors[0];
        selectedColorName = colorNames[0];

        // Create a dropdown list with color names

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, colorNames);
        spinnerEventColor.setAdapter(adapter);

        // Handle color selection
        spinnerEventColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColorName = colorNames[position];
                selectedColorAttr = ThemeUtils.getColorIDFromName(selectedColorName);
                viewColorPreview.setBackgroundColor(ThemeUtils.resolveColorFromTheme(context,selectedColorAttr));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {spinnerEventColor.setSelection(0);}
        });

    }
}
