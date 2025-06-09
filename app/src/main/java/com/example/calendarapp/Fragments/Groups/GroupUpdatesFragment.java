package com.example.calendarapp.Fragments.Groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.widget.ScrollView;
import android.content.Context;
import android.util.TypedValue;

import com.example.calendarapp.R;

public class GroupUpdatesFragment extends Fragment {

    private LinearLayout updatesContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //TODO: replace with real updates
        Context context = requireContext();

        // Root ScrollView
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        scrollView.setPadding(32, 32, 32, 32);
        scrollView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));



        // Vertical layout for updates
        updatesContainer = new LinearLayout(context);
        updatesContainer.setOrientation(LinearLayout.VERTICAL);
        updatesContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        scrollView.addView(updatesContainer);

        // 🔹 Title: "Updates"
        TextView title = new TextView(context);
        title.setText("Notifications");
        title.setTextSize(24);
        title.setTextColor(ContextCompat.getColor(context, R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 24);
        updatesContainer.addView(title);

        // Add fake updates
        addFakeUpdate("", "📅 Upcoming: Team sync at 10:00 AM tomorrow.");
        addFakeUpdate("", "📌 Reminder: Project deadline in 2 days.");
        addFakeUpdate("", "🕒 New event: Client presentation next Monday at 3:00 PM.");


        return scrollView;
    }

    private void addFakeUpdate(String author, String message) {
        Context context = requireContext();

        CardView card = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setCardElevation(8);
        card.setRadius(16);
        card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundLightSecondary));

        // Inner layout
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        // Author
        TextView authorView = new TextView(context);
        authorView.setText(author);
        authorView.setTextSize(16);
        authorView.setTextColor(ContextCompat.getColor(context, R.color.btnGoogleColorMid1));
        authorView.setTypeface(null, android.graphics.Typeface.BOLD);

        // Message
        TextView messageView = new TextView(context);
        messageView.setText(message);
        messageView.setTextSize(15);
        messageView.setTextColor(ContextCompat.getColor(context, R.color.black));
        messageView.setPadding(0, 8, 0, 0);

        layout.addView(authorView);
        layout.addView(messageView);
        card.addView(layout);

        updatesContainer.addView(card);
    }
}
