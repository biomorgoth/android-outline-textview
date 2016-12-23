package com.biomorgoth.outlinetextview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

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

public class StrokedEditText extends EditText {

    private static final int DEFAULT_STROKE_WIDTH = 0;

    // fields
    private int _strokeColor;
    private float _strokeWidth;
    private int _hintStrokeColor;
    private float _hintStrokeWidth;
    private boolean isDrawing;
    private Bitmap altBitmap;
    private Canvas altCanvas;

    public StrokedEditText(Context context) {
        this(context, null);
    }

    public StrokedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public StrokedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StrokedEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextAttrs);
            _strokeColor = a.getColor(R.styleable.StrokedTextAttrs_textStrokeColor,
                    getCurrentTextColor());
            _strokeWidth = a.getFloat(R.styleable.StrokedTextAttrs_textStrokeWidth,
                    DEFAULT_STROKE_WIDTH);
            _hintStrokeColor = a.getColor(R.styleable.StrokedTextAttrs_textHintStrokeColor,
                    getCurrentHintTextColor());
            _hintStrokeWidth = a.getFloat(R.styleable.StrokedTextAttrs_textHintStrokeWidth,
                    DEFAULT_STROKE_WIDTH);

            a.recycle();
        } else {
            _strokeColor = getCurrentTextColor();
            _strokeWidth = DEFAULT_STROKE_WIDTH;
            _hintStrokeColor = getCurrentHintTextColor();
            _hintStrokeWidth = DEFAULT_STROKE_WIDTH;
        }
        setStrokeWidth(_strokeWidth);
        setHintStrokeWidth(_hintStrokeWidth);
    }

    @Override
    public void invalidate() {
        // Ignore invalidate() calls when isDrawing == true
        // (setTextColor(color) calls will trigger them,
        // creating an infinite loop)
        if(isDrawing) return;
        super.invalidate();
    }

    public void setHintStrokeColor(int color) {
        _hintStrokeColor = color;
    }

    public void setHintStrokeWidth(float width) {
        //convert values specified in dp in XML layout to
        //px, otherwise stroke width would appear different
        //on different screens
        _hintStrokeWidth = ConvertUtil.spToPx(getContext(), width);
    }

    public void setHintStrokeWidth(int unit, float width) {
        _hintStrokeWidth = TypedValue.applyDimension(
                unit, width, getContext().getResources().getDisplayMetrics());
    }

    public void setStrokeColor(int color) {
        _strokeColor = color;
    }

    public void setStrokeWidth(float width) {
        //convert values specified in dp in XML layout to
        //px, otherwise stroke width would appear different
        //on different screens
        _strokeWidth = ConvertUtil.spToPx(getContext(), width);
    }

    public void setStrokeWidth(int unit, float width) {
        _strokeWidth = TypedValue.applyDimension(
                unit, width, getContext().getResources().getDisplayMetrics());
    }

    // overridden methods
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        boolean paintHint = getHint() != null && getText().length() == 0;
        if((paintHint && _hintStrokeWidth > 0) || (!paintHint && _strokeWidth > 0)) {
            isDrawing = true;
            if(altBitmap == null) {
                altBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                altCanvas = new Canvas(altBitmap);
            } else if(altCanvas.getWidth() != canvas.getWidth() ||
                    altCanvas.getHeight() != canvas.getHeight()) {
                altBitmap.recycle();
                altBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                altCanvas.setBitmap(altBitmap);
            }
            //draw the fill part of text
            super.onDraw(canvas);
            //save the text color
            int currentTextColor = paintHint ? getCurrentHintTextColor() : getCurrentTextColor();
            //clear alternate canvas
            altBitmap.eraseColor(Color.TRANSPARENT);
            //set paint to stroke mode and specify
            //stroke color and width
            Paint p = getPaint();
            p.setStyle(Paint.Style.STROKE);
            if(paintHint) {
                p.setStrokeWidth(_hintStrokeWidth);
                setHintTextColor(_hintStrokeColor);
            } else {
                p.setStrokeWidth(_strokeWidth);
                setTextColor(_strokeColor);
            }
            //draw text stroke
            super.onDraw(altCanvas);
            canvas.drawBitmap(altBitmap, 0, 0, null);
            //revert the color back to the one
            //initially specified
            if(paintHint) {
                setHintTextColor(currentTextColor);
            } else {
                setTextColor(currentTextColor);
            }
            //set paint to fill mode (restore)
            p.setStyle(Paint.Style.FILL);
            isDrawing = false;
        } else {
            super.onDraw(canvas);
        }
    }
}
