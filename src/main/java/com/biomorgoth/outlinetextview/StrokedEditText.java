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
import android.widget.EditText;

/**
 * Created by epalacios on 12/6/16.
 */

public class StrokedEditText extends EditText {

    private static final int DEFAULT_STROKE_WIDTH = 0;

    // fields
    private int _strokeColor;
    private float _strokeWidth;
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

            a.recycle();
        } else {
            _strokeColor = getCurrentTextColor();
            _strokeWidth = DEFAULT_STROKE_WIDTH;
        }
        setStrokeWidth(_strokeWidth);
    }

    @Override
    public void invalidate() {
        // Ignore invalidate() calls when isDrawing == true
        // (setTextColor(color) calls will trigger them,
        // creating an infinite loop)
        if(isDrawing) return;
        super.invalidate();
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

    // overridden methods
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if(_strokeWidth > 0) {
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
            //set paint to fill mode
            Paint p = getPaint();
            p.setStyle(Paint.Style.FILL);
            //draw the fill part of text
            super.onDraw(canvas);
            //save the text color
            int currentTextColor = getCurrentTextColor();
            //clear alternate canvas
            altBitmap.eraseColor(Color.TRANSPARENT);
            //set paint to stroke mode and specify
            //stroke color and width
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(_strokeWidth);
            setTextColor(_strokeColor);
            //draw text stroke
            super.onDraw(altCanvas);
            canvas.drawBitmap(altBitmap, 0, 0, null);
            //revert the color back to the one
            //initially specified
            setTextColor(currentTextColor);
            isDrawing = false;
        } else {
            super.onDraw(canvas);
        }
    }
}
