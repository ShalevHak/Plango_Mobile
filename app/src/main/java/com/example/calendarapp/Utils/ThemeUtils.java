package com.example.calendarapp.Utils;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendarapp.R;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThemeUtils {
    /**
     * Resolves a color attribute from the current theme.
     *
     * @param context   The current context.
     * @param attrResId The attribute resource ID (e.g., R.attr.colorPrimary).
     * @return The resolved color as an integer.
     * @throws IllegalArgumentException if the attribute is not found or not a valid color.
     */
    public static final String[] eventColorsNames = {"Purple", "Pink", "Orange", "Red", "Green", "Blue"};

    public static final int[] eventColorsID = new int[] {
            R.attr.eventColor1,
            R.attr.eventColor2,
            R.attr.eventColor3,
            R.attr.eventColor4,
            R.attr.eventColor5,
            R.attr.eventColor6,
    };

    private static final HashMap<String,Integer> eventColorsMap = new HashMap<String,Integer>();

    static {
        if (eventColorsNames.length != eventColorsID.length) {
            throw new RuntimeException("eventColorsNames and eventColorsID must be the same length");
        }

        for (int i = 0; i < eventColorsNames.length; i++) {
            eventColorsMap.put(eventColorsNames[i], eventColorsID[i]);
        }
    }
    public static int resolveColorFromTheme(Context context, Integer attrResId) {
        TypedValue typedValue = new TypedValue();
        boolean found = context.getTheme().resolveAttribute(attrResId, typedValue, true);
        if (!found) {
            throw new IllegalArgumentException("Attribute not found in the current theme: " + attrResId);
        }
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return typedValue.data;
        } else {
            throw new IllegalArgumentException("Attribute is not a valid color: " + attrResId);
        }
    }

    public static int getColorIDFromName(String colorName){
        return eventColorsMap.getOrDefault(colorName, eventColorsID[0]);
    }

}
