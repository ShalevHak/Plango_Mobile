package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventComponent2 extends FrameLayout implements IComponent, View.OnClickListener {
    // ────────────────────────────────────────────────────────────────────────────
    //  constants / helpers
    // ────────────────────────────────────────────────────────────────────────────
    private static final int   MIN_HEIGHT_PX   = 64;           // ensure small events stay tappable
    private static final int   H_MARGIN_DP     = 4;            // horizontal gap between columns
    private static final int   V_MARGIN_DP     = 2;            // vertical gap between stacked events

    private static int dp(Context ctx, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

    // ────────────────────────────────────────────────────────────────────────────
    //  members
    // ────────────────────────────────────────────────────────────────────────────
    private Event           event;
    private TextView        eventView;
    private DayComponent    parent;
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

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
        eventView.setPadding(dp(context,8), dp(context,4), dp(context,8), dp(context,4));
    }
    private void ensureId() {
        if (getId() == NO_ID) {
            setId(View.generateViewId());
        }
    }
    public void initParentDay(DayComponent dayComponent){
        // API called by DayComponent
        this.parent = dayComponent;
    }
    public void initFullDayEvent(Event event) {
        this.event = event;

        setStyle(event);

        setFullDayEventLayout();
    }

    private void setStyle(Event event) {
        eventView.setText(event.getTitle());
        eventView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        eventView.setMaxLines(2); // Allow wrapping
        eventView.setEllipsize(TextUtils.TruncateAt.END);
        eventView.setLineSpacing(0, 1.2f); // Better vertical space
        eventView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START); // Align nicely

        Drawable backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.round_corner_background);
        if (backgroundDrawable != null) {
            int colorId = ThemeUtils.getColorIDFromName(event.getColor());
            int color = ThemeUtils.resolveColorFromTheme(getContext(), colorId);
            backgroundDrawable.setTint(color);
            this.setBackground(backgroundDrawable);
        }
        eventView.setTextColor(Color.WHITE);
        eventView.setPadding(2, 2, 2, 2);
    }

    public void initOverflowEvent(Event event){
        this.event = event;
        setStyle(event);
    }

    private void setFullDayEventLayout() {
        int width = dp(getContext(), 300);
        int height = dp(getContext(), MIN_HEIGHT_PX); // Ensures tappable height

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.setMargins(dp(getContext(), H_MARGIN_DP), dp(getContext(), V_MARGIN_DP),
                dp(getContext(), H_MARGIN_DP), dp(getContext(), V_MARGIN_DP));

        setLayoutParams(lp);

        // Padding ensures internal text is not squished
        setPadding(dp(getContext(), 12), dp(getContext(), 8), dp(getContext(), 12), dp(getContext(), 8));

        setMinimumHeight(height); // Enforce min height
        setClickable(true);
        setFocusable(true);
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
        tvStartTime.setText(displayFormat.format(event.getStartDate()));
        tvEndTime.setText(displayFormat.format(event.getEndDate()));
        tvDescription.setText(event.getDescription());

        // Create and show the dialog
        androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();

        // Set button actions
        btnEdit.setOnClickListener(v -> {
            openEditEventDialog(context, dialog);
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
        });

        dialog.show();
    }

    private void openEditEventDialog(Context context, androidx.appcompat.app.AlertDialog dialog) {
        // Create a dialog for editing the event
        EventEditComponent.openEditEventDialog(
                context,
                event,
                (Event e) -> {
                    this.editEvent(e);
                    dialog.dismiss();
                }
        );
    }

    private void editEvent(Event event) {
        parent.editEvent(this.event,event);
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

    // Initialize event layout for overlapping UI scenarios using constraint positioning
    public void initOrganizedEvent(DayComponent.UIDayEvent uiDayEvent) {
        this.event = uiDayEvent.event;
        setStyle(event);


        // Wait until the parent layout has been measured
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to prevent multiple calls
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setOrganizedEventLayout(uiDayEvent);
            }
        });
    }

    private void setOrganizedEventLayout(final DayComponent.UIDayEvent uiDayEvent) {
        if (!(getParent() instanceof ConstraintLayout)) return;
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


