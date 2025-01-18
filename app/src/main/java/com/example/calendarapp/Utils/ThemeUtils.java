package com.example.calendarapp.Utils;

import android.content.Context;
import android.util.TypedValue;

public class ThemeUtils {
    /**
     * Resolves a color attribute from the current theme.
     *
     * @param context   The current context.
     * @param attrResId The attribute resource ID (e.g., R.attr.colorPrimary).
     * @return The resolved color as an integer.
     * @throws IllegalArgumentException if the attribute is not found or not a valid color.
     */
    public static int resolveColorFromTheme(Context context, int attrResId) {
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

}
