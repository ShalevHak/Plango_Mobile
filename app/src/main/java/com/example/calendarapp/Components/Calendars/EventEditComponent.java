package com.example.calendarapp.Components.Calendars;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
import com.example.calendarapp.Utils.TimeUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EventEditComponent extends LinearLayout implements IComponent {

    // ───────────────────────────────── UI references ──────────────────────────
    private EditText etTitle, etDescription, etStartDate, etStartTime, etEndDate, etEndTime;
    private TextInputLayout tilTitle, tilDescription, tilEndTime;
    private View colorPreview;
    private Spinner colorSpinner;
    private Button  btnSave;

    // ─────────────────────────────── helpers ──────────────────────────────────
    private final Calendar startCal = Calendar.getInstance();
    private final Calendar endCal   = Calendar.getInstance();

    private final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat TIME_FMT = new SimpleDateFormat("HH:mm",      Locale.getDefault());

    // colour arrays come from ThemeUtils so we can skin the app later
    private static final int[]    COLOR_ATTRS = ThemeUtils.eventColorsID;
    private static final String[] COLOR_NAMES = ThemeUtils.eventColorsNames;
    private int    selectedColorAttr = COLOR_ATTRS[0];
    private String selectedColorName = COLOR_NAMES[0];

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
        bindViews();
        bindPickers(context);


        btnSave.setOnClickListener(v -> saveEvent(context));

        setupColorPicker(context);
    }

    private void bindViews() {
        tilTitle = findViewById(R.id.tilEventTitle);
        tilDescription = findViewById(R.id.tilEventDescription);
        tilEndTime = findViewById(R.id.tilEndTime);

        etTitle = findViewById(R.id.etEventTitle);
        etDescription = findViewById(R.id.etEventDescription);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        btnSave = findViewById(R.id.btnSaveEvent);

        colorSpinner = findViewById(R.id.spinnerEventColor);
        colorPreview = findViewById(R.id.viewColorPreview);
    }

    private void bindPickers(Context ctx) {
        etStartDate.setOnClickListener(v -> showDatePicker(ctx, startCal, etStartDate));
        etEndDate  .setOnClickListener(v -> showDatePicker(ctx,   endCal, etEndDate));
        etStartTime.setOnClickListener(v -> showTimePicker(ctx, startCal, etStartTime));
        etEndTime  .setOnClickListener(v -> showTimePicker(ctx,   endCal, etEndTime));
    }

    private void showDatePicker(Context ctx, Calendar cal, EditText target) {
        new DatePickerDialog(ctx, (view, y,m,d) -> {
            cal.set(y, m, d);
            target.setText(DATE_FMT.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker(Context ctx, Calendar cal, EditText target) {
        new TimePickerDialog(ctx, (view, h, m) -> {
            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE,      m);
            target.setText(TIME_FMT.format(cal.getTime()));
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    private void debugLogEventTimes(String phase, Date start, Date end) {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
        localFormat.setTimeZone(TimeZone.getDefault());

        Log.d("DEBUG_EVENT_TIME", "=== " + phase + " ===");
        Log.d("DEBUG_EVENT_TIME", "Local Start: " + localFormat.format(start));
        Log.d("DEBUG_EVENT_TIME", "UTC Start:   " + utcFormat.format(start));
        Log.d("DEBUG_EVENT_TIME", "Local End:   " + localFormat.format(end));
        Log.d("DEBUG_EVENT_TIME", "UTC End:     " + utcFormat.format(end));
    }
    // Method to handle event saving
    private void saveEvent(Context context) {
        if(!validate()) return;
        Event event = new Event(
                etTitle.getText().toString().trim(),
                etDescription.getText().toString().trim(),
                startCal.getTime(),
                endCal.getTime(),
                selectedColorName);
        if(listener!=null) listener.onEventSaved(event);
    }

    private boolean validate() {
        clearErrors();
        boolean ok = true;

        if (TextUtils.isEmpty(etTitle.getText())) {
            tilTitle.setError("Title required");
            ok = false;
        }
        if (TextUtils.isEmpty(etStartDate.getText())) {
            etStartDate.setError("Start date");
            ok = false;
        }
        if (TextUtils.isEmpty(etStartTime.getText())) {
            etStartTime.setError("Start time");
            ok = false;
        }
        if (TextUtils.isEmpty(etEndDate.getText())) {
            etEndDate.setError("End date");
            ok = false;
        }
        if (TextUtils.isEmpty(etEndTime.getText())) {
            etEndTime.setError("End time");
            ok = false;
        }

        if (!ok) return false;

        if (endCal.before(startCal)) {
            tilEndTime.setError("End before start");
            return false;
        }
        return true;
    }

    private void clearErrors() {
        tilTitle.setError(null);
        tilDescription.setError(null);
        tilEndTime.setError(null);
        etStartDate.setError(null);
        etStartTime.setError(null);
        etEndDate.setError(null);
        etEndTime.setError(null);
    }


    // Public method to set event details
    public void setEventDetails(Event e) {
        if (e == null) return;
        etTitle.setText(e.getTitle());
        etDescription.setText(e.getDescription());

        startCal.setTime(e.getStartDate());
        endCal.setTime(e.getEndDate());

        etStartDate.setText(DATE_FMT.format(startCal.getTime()));
        etStartTime.setText(TIME_FMT.format(startCal.getTime()));
        etEndDate.setText(DATE_FMT.format(endCal.getTime()));
        etEndTime.setText(TIME_FMT.format(endCal.getTime()));

        if (e.getColor() != null) {
            for (int i = 0; i < COLOR_NAMES.length; i++) {
                if (COLOR_NAMES[i].equals(e.getColor())) {
                    colorSpinner.setSelection(i);
                    break;
                }
            }
        }
    }
    // Static method to show the event edit dialog
    public static void openEditEventDialog(Context context, Event event, OnEventSaveListener callback) {
        EventEditComponent eventEditComponent = new EventEditComponent(context);

        if (event != null) {
            eventEditComponent.setEventDetails(event);
        }

        AlertDialog d = new AlertDialog.Builder(context)
            .setTitle(event == null ? "Create Event" : "Edit Event")
            .setView(eventEditComponent)
            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
            .create();
        d.show();

        eventEditComponent.setOnEventSaveListener(savedEvent -> {
            if (callback != null) {
                callback.onEventSaved(savedEvent);
                d.dismiss();
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


        selectedColorAttr = COLOR_ATTRS[0];
        selectedColorName = COLOR_NAMES[0];

        // Create a dropdown list with color names

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, COLOR_NAMES);
        colorSpinner.setAdapter(adapter);

        // Handle color selection
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColorName = COLOR_NAMES[position];
                selectedColorAttr = ThemeUtils.getColorIDFromName(selectedColorName);
                colorPreview.setBackgroundColor(ThemeUtils.resolveColorFromTheme(context,selectedColorAttr));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {colorSpinner.setSelection(0);}
        });

    }
}
