package com.biomorgoth.outlinetextview;

import android.content.Context;

/**
 * Created by epalacios on 12/8/16.
 */

public class ConvertUtil {
    /**
     * Convenience method to convert scale-independent pixel(sp) value
     * into device display specific pixel value.
     * @param context Context to access device specific display metrics
     * @param sp scale independent pixel value
     * @return device specific pixel value.
     */
    protected static int spToPx(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
