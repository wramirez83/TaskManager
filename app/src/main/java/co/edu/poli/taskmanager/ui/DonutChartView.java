package co.edu.poli.taskmanager.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import co.edu.poli.taskmanager.R;

public class DonutChartView extends View {

    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arc = new RectF();
    private float progress;
    private String label = "0%";

    public DonutChartView(Context context) {
        super(context);
        init();
    }

    public DonutChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(18f);
        trackPaint.setColor(ContextCompat.getColor(getContext(), R.color.divider));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(18f);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setColor(ContextCompat.getColor(getContext(), R.color.primary));
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_primary));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
    }

    public void setProgress(int completed, int total) {
        progress = total == 0 ? 0f : completed / (float) total;
        label = Math.round(progress * 100) + "%";
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float pad = 18f;
        arc.set(pad, pad, getWidth() - pad, getHeight() - pad);
        canvas.drawArc(arc, -90, 360, false, trackPaint);
        canvas.drawArc(arc, -90, 360 * progress, false, progressPaint);
        textPaint.setTextSize(getWidth() * 0.22f);
        canvas.drawText(label, getWidth() / 2f, getHeight() / 2f + textPaint.getTextSize() / 3f, textPaint);
    }
}
