package ua.kpi.comsys.iv8107;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import java.lang.Math;


public class DrawingView extends View {
    private String string = "";
    private int color = Color.GREEN;
    private float dimension = 0;
    private int numDrawing = 0;
    private TextPaint textPaint;


    private final Paint colorGr = new Paint();
    private final Paint colorBlck = new Paint();
    private final Paint colorCn = new Paint();
    private final Paint colorPrpl = new Paint();
    private final Paint colorYlw = new Paint();

    public DrawingView(Context context) {
        super(context);
        DataInit(null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DataInit(attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DataInit(attrs, defStyle);
    }

    private void DataInit(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DrawingView, defStyle, 0);

        string = a.getString(R.styleable.DrawingView_exampleString);
        color = a.getColor(R.styleable.DrawingView_exampleColor,
                color);

        dimension = a.getDimension(
                R.styleable.DrawingView_exampleDimension,
                dimension);

        if (a.hasValue(R.styleable.DrawingView_exampleDrawable)) {
            Drawable mExampleDrawable = a.getDrawable(R.styleable.DrawingView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        colorBlck.setColor(Color.BLACK);
        colorCn.setColor(Color.CYAN);
        colorPrpl.setColor(0xff6a0dad);
        colorYlw.setColor(Color.YELLOW);
        colorGr.setColor(Color.GRAY);

        colorBlck.setStrokeWidth(5);
        colorPrpl.setStrokeWidth(5);

        a.recycle();

        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);

        TextPaint();
    }

    private void TextPaint() {
        textPaint.setTextSize(dimension);
        textPaint.setColor(color);
        float mTextWidth = textPaint.measureText(string);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int pdnLft = getPaddingLeft();
        int pdnTop = getPaddingTop();
        int pdnRght = getPaddingRight();
        int pdnBttm = getPaddingBottom();

        int contentWidth = getWidth() - pdnLft - pdnRght;
        int contentHeight = getHeight() - pdnTop - pdnBttm;

        if (numDrawing == 0) {
            int n = 50;
            float x = 0;
            float y = 0;
            for (int i = 0; i < n; i++) {
                float y_new = contentHeight -
                        (float) (Math.cos(Math.PI * (2 * (double) i / (n - 1) - 1)) + 1 + 1)
                                * contentHeight / 4 + pdnTop;
                float x_new = (float) i / (n - 1) * contentWidth + pdnLft;
                if (i == 0) {
                    x = x_new;
                    y = y_new;
                } else {
                    canvas.drawLine(x, y, x_new, y_new, colorPrpl);
                    x = x_new;
                    y = y_new;
                }
            }

            canvas.drawLine(0, (float) contentHeight / 2 + pdnTop,
                    contentWidth + pdnLft + pdnRght,
                    (float) contentHeight / 2 + pdnTop, colorBlck);

            canvas.drawLine((float) contentWidth / 2 + pdnLft, 0,
                    (float) contentWidth / 2 + pdnLft,
                    contentHeight + pdnTop + pdnBttm, colorBlck);

            canvas.drawLine((float) contentWidth / 2 + pdnLft, 0,
                    (float) contentWidth / 2 + pdnLft - 25, 50, colorBlck);

            canvas.drawLine((float) contentWidth / 2 + pdnLft, 0,
                    (float) contentWidth / 2 + pdnLft + 25, 50, colorBlck);

            canvas.drawLine(contentWidth + pdnLft + pdnRght,
                    (float) contentHeight / 2 + pdnTop,
                    contentWidth + pdnLft + pdnRght - 50,
                    (float) contentHeight / 2 + pdnTop - 25, colorBlck);

            canvas.drawLine(contentWidth + pdnLft + pdnRght,
                    (float) contentHeight / 2 + pdnTop,
                    contentWidth + pdnLft + pdnRght - 50,
                    (float) contentHeight / 2 + pdnTop + 25, colorBlck);
        } else if (numDrawing == 1){
            int size = Math.min(contentWidth, contentHeight);
            int xFirst = pdnLft + (contentWidth - size) / 2;
            int yFirst = pdnTop + (contentHeight - size) / 2;
            RectF oval = new RectF(xFirst, yFirst, xFirst + size, yFirst + size);
            canvas.drawArc(oval, 0F, 162F, true, colorCn);
            canvas.drawArc(oval, 162F, 18F, true, colorPrpl);
            canvas.drawArc(oval, 180F, 90F, true, colorYlw);
            canvas.drawArc(oval, 270F, 90F, true, colorGr);
        }
    }

    public void setNumberOfDrawing(int numberOfDrawing) {
        numDrawing = numberOfDrawing;
    }

    public int getNumberOfDrawing() {
        return numDrawing;
    }


}