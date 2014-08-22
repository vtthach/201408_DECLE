package com.example.dragtofillcolor;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class CustomView extends View {
    private static final String TAG = "vtt";
    public static final int COLOR_STROKE = Color.BLUE;
    private static final boolean IS_DEBUG = true;
    private static final boolean IS_TEST = true;
    
    final ArrayList<Polygon> polygonLists = new ArrayList<>();
    Paint mPaint = new Paint();
    Paint mPaintDebug = new Paint();
    
    private boolean isStartedDrag;
    Polygon lastPolygonSelected;
    
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public CustomView(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(COLOR_STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        if(IS_DEBUG){
            mPaintDebug = new Paint();
            mPaintDebug.setColor(Color.YELLOW);
            mPaintDebug.setStrokeWidth(3);
            mPaintDebug.setAntiAlias(true);
        }
        boolean isPortrail = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? true
                                                                                                                    : false;
        int deltaX = 200;
        int deltaY = 200;
        if(isPortrail){
            deltaX = 0;
        } else {
            deltaY = 0;
        }
        initPolygon(deltaX, deltaY);
//        for(int i = 1; i<=3; i++){
//            initPolygon(deltaX * i, deltaY * i);
//        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Polygon polygonMove = null;
        
        for (Polygon p : polygonLists) {
            // Get last action up & move
            if (actionUp.x != -1) {
                if (p.contains((int)actionUp.x, (int)actionUp.y)) {
                    p.isFilled = true;
                    p.color = color;
                }
            }
            if (actionLastMove.x != -1) {
                if (p.contains((int)actionLastMove.x, (int)actionLastMove.y)) {
                    polygonMove = p;
                }
            }
            
            // Draw filled
            if (p.isFilled) {
                mPaint.setStyle(Style.FILL_AND_STROKE);
            } else {
                mPaint.setStyle(Style.STROKE);
            }
            
            // Set color to draw
            mPaint.setColor(p.color);
            canvas.drawPath(p.getPath(), mPaint);
        }
        
        // Draw polygonMove to preview if any
        if (polygonMove != null) {
            mPaint.setColor(polygonMove.color);
            mPaint.setStyle(Style.FILL_AND_STROKE);
            canvas.drawPath(polygonMove.getPath(), mPaint);
        }
        
        
        // Draw point
        if (IS_DEBUG) {
            if (actionLastMove.x != -1) {
                mPaintDebug.setColor(Color.YELLOW);
                canvas.drawCircle(actionLastMove.x, actionLastMove.y, 60, mPaintDebug);
            }
            
            if (actionUp.x != -1) {
                mPaintDebug.setColor(Color.GREEN);
                canvas.drawCircle(actionUp.x, actionUp.y, 60, mPaintDebug);
            }
        }
        
        if (!isStartedDrag) {
            resetPoint();
        }
    }
    
    DragPoint actionUp = new DragPoint();
    DragPoint actionLastMove = new DragPoint();
    
    public void resetPoint() {
        actionUp.x = -1;
        actionUp.y = -1;
        actionLastMove.x = -1;
        actionLastMove.y = -1;
        color = COLOR_STROKE;
    }
    
    public void initPolygon(int deltaX, int deltaY) {
        float px[], py[];
        int total = 4;
        px = new float[total];
        py = new float[total];
        
        px[0] = deltaX + 400;
        px[1] = deltaX + 600;
        px[2] = deltaX + 400;
        px[3] = deltaX + 200;
        
        py[0] = deltaY + 100;
        py[1] = deltaY + 200;
        py[2] = deltaY + 180;
        py[3] = deltaY + 200;
        polygonLists.add(new Polygon(px, py, "a"));
    }
    
    public void startDrag() {
        isStartedDrag = true;
    }
    
    int color;
    
    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED: {
                // claim to accept any dragged content
                Log.i(TAG, "Drag started, event=" + event);
                Log.d(TAG, "Drag event.getClipData(): " + event.getClipData());
                Log.d(TAG, "Drag event.getClipDescription(): " + event.getClipDescription().getLabel());
                Log.d(TAG, "------color: " + color);
                
                // cache whether we accept the drag to return for LOCATION events
                // Redraw in the new visual state if we are a potential drop target
                color = Integer.valueOf((String) event.getClipDescription().getLabel());
                setColorDragPoint(color);
                isStartedDrag = true;
            }
                break;
            
            case DragEvent.ACTION_DRAG_ENDED: {
                Log.i(TAG, "Drag ended.");
            }
                break;
            
            case DragEvent.ACTION_DRAG_LOCATION: {
                // we returned true to DRAG_STARTED, so return true here
                Log.i(TAG, "Vtt... seeing drag locations ...");
                setActionMove(event.getX(), event.getY());
                invalidate();
            }
                break;
            
            case DragEvent.ACTION_DROP: {
                Log.i(TAG, "Got a drop! dot=" + this + " event=" + event);
                isStartedDrag = false;
                setActionUp(event.getX(), event.getY());
                invalidate();
            }
                break;
            
            case DragEvent.ACTION_DRAG_ENTERED: {
                Log.i(TAG, "Entered dot @ " + this);
            }
                break;
            
            case DragEvent.ACTION_DRAG_EXITED: {
                Log.i(TAG, "Exited dot @ " + this);
            }
                break;
            
            default:
                Log.i(TAG, "other drag event: " + event);
        }
        
        return true;
    }
    
    private void setColorDragPoint(int color) {
        actionLastMove.color = color;
        actionUp.color = color;
    }

    private void setActionMove(float x, float y) {
        actionLastMove.x = x;
        actionLastMove.y = y;
    }
    
    private void setActionUp(float x, float y) {
        actionUp.x = x;
        actionUp.y = y;
    }
    
    public class DragPoint extends PointF {
        public int color;
    }
}
