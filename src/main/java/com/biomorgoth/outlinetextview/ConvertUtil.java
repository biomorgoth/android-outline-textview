package com.biomorgoth.outlinetextview;

import android.content.Context;

/**
 Copyright 2016 Evander Palacios

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
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
