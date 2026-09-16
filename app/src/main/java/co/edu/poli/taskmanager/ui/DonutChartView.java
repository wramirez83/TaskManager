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
    private final Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arc = new RectF();
    private float progress;
    private String value = "0/0";
    private String caption = "";

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
        trackPaint.setStrokeWidth(16f);
        trackPaint.setColor(ContextCompat.getColor(getContext(), R.color.divider));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(16f);
        progressPaint.setStrokeCap(Paint.Cap.BUTT);
        progressPaint.setColor(ContextCompat.getColor(getContext(), R.color.primary));
        valuePaint.setColor(ContextCompat.getColor(getContext(), R.color.text_primary));
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setFakeBoldText(true);
        labelPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_muted));
        labelPaint.setTextAlign(Paint.Align.CENTER);
        caption = getContext().getString(R.string.completed_label);
    }

    public void setProgress(int completed, int total) {
        progress = total == 0 ? 0f : completed / (float) total;
        value = completed + "/" + total;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float pad = 16f;
        arc.set(pad, pad, getWidth() - pad, getHeight() - pad);
        canvas.drawArc(arc, -90, 360, false, trackPaint);
        canvas.drawArc(arc, -90, 360 * progress, false, progressPaint);
        valuePaint.setTextSize(getWidth() * 0.22f);
        labelPaint.setTextSize(getWidth() * 0.11f);
        float centerY = getHeight() / 2f;
        canvas.drawText(value, getWidth() / 2f, centerY - 4f, valuePaint);
        canvas.drawText(caption, getWidth() / 2f, centerY + labelPaint.getTextSize() + 6f, labelPaint);
    }
}
