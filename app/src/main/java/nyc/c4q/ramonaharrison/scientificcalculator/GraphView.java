package nyc.c4q.ramonaharrison.scientificcalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by mona on 5/17/15.
 */
public class GraphView extends View {

    private Path drawPath, textPath, gridPath;
    private Paint drawPaint, canvasPaint, gridPaint, canvasGridPaint, textPaint, boxPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private HashMap<Float, Float> coordinates;
    private Float min, max;
    private int width, height, center;
    private float yMax;
    private float yMin;

    private float scaleX;
    private float scaleY;

    private float centerX;
    private float centerY;



    public GraphView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setValues(HashMap<Float, Float> coord, Float mn, Float mx) {
        coordinates = coord;
        min = mn;
        max = mx;
    }

    public void drawGraph() {
        invalidate();
    }

    public void setupGrid(Canvas canvas, int width) {
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(48);
        textPath = new Path();

        boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setAntiAlias(true);
        boxPaint.setAlpha(125);
        boxPaint.setColor(Color.CYAN);

        gridPath = new Path();
        gridPaint = new Paint();
        gridPaint.setAlpha(125);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(5);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeJoin(Paint.Join.BEVEL);
        gridPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvasGridPaint = new Paint(Paint.DITHER_FLAG);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasGridPaint);

        gridPaint.setColor(Color.LTGRAY);
        gridPath.moveTo(width/2, 0);
        gridPath.lineTo(width/2, width);
        canvas.drawPath(gridPath, gridPaint);
        gridPath.moveTo(0, width/2);
        gridPath.lineTo(width, width/2);
        canvas.drawPath(gridPath, gridPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int xw, int xh) {
        super.onSizeChanged(w, h, xw, xh);
        width = GraphActivity.getWidth() - 100;
        height = width;
        center = width/2;
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        setupGrid(drawCanvas, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // check for null inputs
        if (min == null) {
            min = Float.valueOf(0);
        }
        if (max == null) {
            max = Float.valueOf(0);
        }
        if (coordinates == null) {
            coordinates = new HashMap<Float, Float>();
            coordinates.put(min, max);
        }

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setAlpha(125);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.BUTT);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        yMax = Collections.max(coordinates.values());
        yMin = Collections.min(coordinates.values());
        scaleX = width / (max - min);
        scaleY = height / (yMax - yMin);
        centerX = (max - min)/2;
        centerY = (yMax - yMin)/2;

        // draw a graph of the equation
        drawPaint.setColor(Color.BLACK);
        for (float x = min; x < max; x++) {
            float thisX = (centerX + x) * scaleX;
            float thisY = height - (centerY + coordinates.get(x)) * scaleY;
            float nextX = (centerX + (x + 1)) * scaleX;
            float nextY = height - (centerY + coordinates.get(x + 1)) * scaleY;
            drawPath.moveTo(thisX, thisY);
            drawPath.lineTo(nextX, nextY);
            canvas.drawPath(drawPath, drawPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        float thisx = (touchX - center) / scaleX;
        float thisy = 0 - ((touchY - center) / scaleY);

        String pos = "X = " + String.format("%.1f", thisx) + ", Y = " + String.format("%.1f", thisy);

        float textStart = center - (pos.length() * 12);
        float textEnd = center + (pos.length() * 12);
        textPath.moveTo(textStart, height - 30);
        textPath.lineTo(textEnd, height - 30);

        Rect rect = new Rect((int)textStart - 30, height - 100, (int)textEnd + 30, height);
        RectF rectF = new RectF(rect);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawCanvas.drawRoundRect(rectF, 15, 15, boxPaint);
                drawCanvas.drawTextOnPath(pos, textPath, 0, 0, textPaint);
                break;
            case MotionEvent.ACTION_MOVE:
                drawCanvas.drawRoundRect(rectF, 15, 15, boxPaint);
                drawCanvas.drawTextOnPath(pos, textPath, 0, 0, textPaint);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}
