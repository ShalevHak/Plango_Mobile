package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ThemeUtils;
import com.example.calendarapp.Utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventComponent2 extends FrameLayout implements IComponent, View.OnClickListener {
    private Event event;
    private TextView eventView;
    private static final int MIN_HEIGHT_PX = 50; // Ensure text is always visible

    public EventComponent2(Context context) {
        super(context);
        initComponent(context);
    }

    public EventComponent2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public EventComponent2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public EventComponent2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        ensureId();
        inflate(context, R.layout.event_component2,this);
        this.setOnClickListener(this); // Set the click listener

        eventView = this.findViewById(R.id.tvEventTitle2);
        eventView.setMaxLines(1);
        eventView.setEllipsize(TextUtils.TruncateAt.END); // Truncate with "..."
        eventView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        eventView.setPadding(8, 4, 8, 4); // Reduce padding to avoid text clipping
    }
    private void ensureId() {
        if (getId() == NO_ID) {
            setId(View.generateViewId());
        }
    }
    public void initFullDayEvent(Event event) {
        this.event = event;

        eventView.setText(truncateTitle(event.getTitle(), 15));

        Drawable backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.round_corner_background);
        if (backgroundDrawable != null) {
            int color = event.getColor() != 0 ? event.getColor() : Color.parseColor("#FF9800");
            backgroundDrawable.setTint(color);
            this.setBackground(backgroundDrawable);
        }

        eventView.setTextColor(Color.WHITE);
        eventView.setPadding(16, 8, 16, 8);
        eventView.setGravity(Gravity.CENTER);

        setFullDayEventLayout();
    }

    public void initEvent(Event event, int hourHeightPx){
        this.event = event;

        initEventStyle(event);

        // Position the event based on start time
        setEventLayout(hourHeightPx);
    }

    private void initEventStyle(Event event) {
        TextView eventView = this.findViewById(R.id.tvEventTitle2);
        eventView.setText(event.getTitle());
        //eventView.setBackgroundColor(event.getColor());
        // Load the rounded corner background
        Drawable backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.round_corner_background);
        if (backgroundDrawable != null) {
            int color = event.getColor() != 0 ? event.getColor() : Color.parseColor("red");
            // Apply a red tint to the background drawable
            backgroundDrawable.setTint(color);
            this.setBackground(backgroundDrawable); // Set the tinted drawable as the background
        }

        eventView.setTextColor(0xFFFFFFFF);
        eventView.setPadding(2, 2, 2, 2);
    }

    private void setEventLayout(int hourHeightPx) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) (event.getDurationHours() * hourHeightPx)
        );
        params.topMargin = (int) (event.getStartHour() * hourHeightPx);
        this.setLayoutParams(params);
    }

    private void setFullDayEventLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                300, // Fixed width for each full-day event
                100  // Fixed height
        );
        params.setMargins(10, 5, 10, 5);
        this.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        showEventDetailsDialog();
    }
    private void showEventDetailsDialog() {
        Context context = getContext();

        // Create a Dialog
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);

        // Inflate a custom layout for the dialog
        View dialogView = View.inflate(context, R.layout.dialog_event_details, null);

        // Set the custom view for the dialog
        dialogBuilder.setView(dialogView);

        // Access and populate dialog views
        TextView tvTitle = dialogView.findViewById(R.id.tvEventDialogTitle);
        TextView tvStartTime = dialogView.findViewById(R.id.tvEventDialogStartTime);
        TextView tvEndTime = dialogView.findViewById(R.id.tvEventDialogEndTime);
        TextView tvDescription = dialogView.findViewById(R.id.tvEventDialogDescription);
        View btnEdit = dialogView.findViewById(R.id.btnEditEvent);
        View btnClose = dialogView.findViewById(R.id.btnCloseEventDialog);

        // Populate details
        tvTitle.setText(event.getTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

        tvStartTime.setText(dateFormat.format(event.getStartDate()));
        tvEndTime.setText(dateFormat.format(event.getEndDate()));
        //tvDescription.setText(event.getDescription());

        // Create and show the dialog
        androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();

        // Set button actions
        btnEdit.setOnClickListener(v -> {
            openEditEventDialog(context);
        });
        btnClose.setOnClickListener(v -> {
            // Dismiss the dialog
            if (dialog != null) dialog.dismiss();
        });

        // Make the dialog larger and with rounded corners
        dialog.setOnShowListener(d -> {
            if (dialog.getWindow() != null) {
                // Get screen dimensions
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
                int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.9);

                // Set dialog dimensions
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_background);

                // Resolve the background color from the theme
                int backgroundColor = ThemeUtils.resolveColorFromTheme(getContext(), R.attr.colorBackground);

                // Tint the drawable with the resolved color
                Drawable backgroundDrawable = dialog.getWindow().getDecorView().getBackground();
                if (backgroundDrawable != null) {
                    backgroundDrawable.setTint(backgroundColor);
                }

                // Resize the root view of the dialog (dialogView)
                ViewGroup.LayoutParams params = dialogView.getLayoutParams();
                if (params != null) {
                    params.width = width;
                    params.height = height;
                    dialogView.setLayoutParams(params); // Apply the new layout params
                }
            }
            // Apply expanding animation
            applyExpandAnimation(dialogView);
        });

        dialog.show();
    }

    private void openEditEventDialog(Context context) {
        // Create a dialog for editing the event (this can be replaced with an Activity or Fragment later)
        androidx.appcompat.app.AlertDialog.Builder editDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        editDialogBuilder.setTitle("Edit Event");
        editDialogBuilder.setMessage("Edit event functionality is under development.");
        editDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        editDialogBuilder.create().show();
    }
    private void applyExpandAnimation(View dialogView) {
        // Set initial scale and alpha values
        dialogView.setScaleX(0.8f);
        dialogView.setScaleY(0.8f);
        dialogView.setAlpha(0f);

        // Animate the scale and alpha properties
        dialogView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(400) // Animation duration
                .setInterpolator(new android.view.animation.DecelerateInterpolator()) // Smooth easing
                .start();
    }

    private void adjustTextSize(int eventHeight) {
        if (eventHeight < 60) {
            eventView.setTextSize(10); // Smaller text for tiny blocks
        } else {
            eventView.setTextSize(14); // Normal size
        }
    }

    private String truncateTitle(String title, int maxLength) {
        if (title.length() > maxLength) {
            return title.substring(0, maxLength) + "...";
        }
        return title;
    }

    public void initOrganizedEvent(DayComponent.UIDayEvent uiDayEvent) {
        this.event = uiDayEvent.event;

        initEventStyle(event);

        // TODO: fix flickering. find alternative for waiting for the parent to be measured.

        // Delay the layout until the parent is measured
        post(new Runnable() {
            @Override
            public void run() {
                setOrganizedEventLayout(uiDayEvent);
            }
        });

    }

    private void setOrganizedEventLayout(final DayComponent.UIDayEvent uiDayEvent) {
        if (getParent() instanceof ConstraintLayout) {
            ConstraintLayout parentLayout = (ConstraintLayout) getParent();
            ConstraintSet constraintSet = new ConstraintSet();

            // *Important* All children in 'parentLayout' must have an ID
            // We have already ensured in `ensureId()` that *this* view has a valid ID.
            // Also ensure any other children also have IDs if you add them.

            // Clone the layout so we can apply constraints
            constraintSet.clone(parentLayout);

            int viewId = getId();

            // Get parent's final measured dimensions (should be ~4800 px tall)
            int parentWidth = parentLayout.getWidth();
            int parentHeight = parentLayout.getHeight();

            // Convert the 4 float percentages into absolute px
            int topPx    = (int) (uiDayEvent.topMarginWeight    * parentHeight);
            int heightPx = (int) (uiDayEvent.heightWeight       * parentHeight);
            int leftPx   = (int) (uiDayEvent.leftMarginWeight   * parentWidth);
            int widthPx = (int) (uiDayEvent.widthWeight * parentWidth) ;

            // Create LayoutParams
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0, heightPx);
            setLayoutParams(params);

            // Constrain the width as absolute
            constraintSet.constrainWidth(viewId, widthPx);

            // Constrain the height as absolute
            constraintSet.constrainHeight(viewId, heightPx);

            // Connect
            constraintSet.connect(viewId, ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID, ConstraintSet.LEFT, leftPx);
            constraintSet.connect(viewId, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP,   topPx);

            // Apply
            constraintSet.applyTo(parentLayout);
        }
    }

}


