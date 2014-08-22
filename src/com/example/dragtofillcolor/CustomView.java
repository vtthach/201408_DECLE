package com.example.dragtofillcolor;

import java.util.ArrayList;
import java.util.Random;

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
    private static boolean IS_TEST = false;
    private static final float TEST_RADIUS = 5;
    private static final int POLYGON_SIZE = 20;
    private static float Y_TEST = 0;
    
    final ArrayList<Polygon> polygonLists = new ArrayList<>();
    Paint mPaint = new Paint();
    Paint mPaintDebug = new Paint();
    
    private boolean isStartedDrag;
    private Random rand = new Random(20);
    
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
            mPaintDebug.setColor(Color.RED);
            mPaintDebug.setStrokeWidth(3);
            mPaintDebug.setAntiAlias(true);
        }
        
        boolean isPortrail = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? true
                                                                                                                    : false;
        if(IS_TEST){
            rand = new Random(POLYGON_SIZE); 
        }
        
        //For test total = 16
//        initPolygon(0, deltaY);
//        initPolygon(deltaX + 50, deltaY);
        
        initComplextData();
    }
    
    private void initComplextData() {
        int deltaX = 30;
        int deltaY = 30;
//        if(isPortrail){
//            deltaX = 0;
//        } else {
//            deltaY = 0;
//        }
        int size = POLYGON_SIZE;
        for(int i = 1; i<=size; i++){
            initPolygon(deltaX * i, deltaY * i);
        }        
    }

    public void initPolygon(int deltaX, int deltaY) {
        float px[], py[], px2[], py2[];
        
        //-----TEST1-------------------------------------------------------
        int total2 = 4;
        px2 = new float[total2];
        py2 = new float[total2];
        
        px2[0] = deltaX + 100;
        px2[1] = deltaX + 300;
        px2[2] = deltaX + 200;
        px2[3] = deltaX -100;
        
        py2[0] = deltaY - 200;
        py2[1] = deltaY - 100;
        py2[2] = deltaY - 120;
        py2[3] = deltaY - 100;
        polygonLists.add(new Polygon(px2, py2, "a"));
        
        //-----TEST2-------------------------------------------------------
        int total = 16;
        px = new float[total];
        py = new float[total];
        px[0] = deltaX + 115;
        px[1] = deltaX + 353;
        px[2] = deltaX + 353;
        px[3] = deltaX + 274;
        px[4] = deltaX + 274;
        px[5] = deltaX + 232;
        px[6] = deltaX + 232;
        px[7] = deltaX + 210;
        px[8] = deltaX + 180;
        px[9] = deltaX + 179;
        px[10] = deltaX + 212;
        px[11] = deltaX + 215;
        px[12] = deltaX + 144;
        px[13] = deltaX + 144;
        px[14] = deltaX + 96;
        px[15] = deltaX + 95;

        py[0] = deltaY + 36;
        py[1] = deltaY + 36;
        py[2] = deltaY + 139;
        py[3] = deltaY + 139;
        py[4] = deltaY + 92;
        py[5] = deltaY + 92;
        py[6] = deltaY + 124;
        py[7] = deltaY + 148;
        py[8] = deltaY + 116;
        py[9] = deltaY + 80;
        py[10] = deltaY + 89;
        py[11] = deltaY + 52;
        py[12] = deltaY + 54;
        py[13] = deltaY + 145;
        py[14] = deltaY + 143;
        py[15] = deltaY + 57;

        Y_TEST = (py[total/2]+ py[total/2-1])/2;
        polygonLists.add(new Polygon(px, py, "a"));
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Polygon polygonMove = null;
        
        for (Polygon p : polygonLists) {
//            // Get last action up & move
//            if (actionUp.x != -1) {
//                if (p.contains((int)actionUp.x, (int)actionUp.y)) {
//                    p.isFilled = true;
//                    p.color = color;
//                }
//            }
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
        
        // Draw polygonMove to preview if any(only draw the last one)
        if (polygonMove != null) {
            mPaint.setColor(color);
            mPaint.setStyle(Style.FILL_AND_STROKE);
            canvas.drawPath(polygonMove.getPath(), mPaint);
            
            if(!isStartedDrag){
                //End drag => save preview color
                polygonMove.isFilled = true;
                polygonMove.color = color;
            }
        }
        
        // Draw point for debug
        if (IS_DEBUG) {
            if (actionLastMove.x != -1) {
                mPaintDebug.setColor(Color.RED);
                canvas.drawCircle(actionLastMove.x, actionLastMove.y, TEST_RADIUS, mPaintDebug);
            }
            
//            if (actionUp.x != -1) {
//                mPaintDebug.setColor(Color.GREEN);
//                canvas.drawCircle(actionUp.x, actionUp.y, TEST_RADIUS, mPaintDebug);
//            }
        }
        
        // Reset if drag event was end
        if (!isStartedDrag) {
            resetPoint();
        }
        
        if(IS_TEST){
            isStartedDrag = true;
            actionLastMove.y = Y_TEST;
            actionLastMove.x += 5;
            if (actionLastMove.x >= getWidth()) {
                actionLastMove.x = 0;
                resetYTest();
            }
            postInvalidateDelayed(50);    
        }
        
        // Draw border
        mPaint.setStyle(Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
    
//    DragPoint actionUp = new DragPoint();
    DragPoint actionLastMove = new DragPoint();
    
    public void resetPoint() {
//        actionUp.x = -1;
//        actionUp.y = -1;
        actionLastMove.x = -1;
        actionLastMove.y = -1;
        color = COLOR_STROKE;
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
//                setActionUp(event.getX(), event.getY());
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
//        actionUp.color = color;
    }

    private void setActionMove(float x, float y) {
        actionLastMove.x = x;
        actionLastMove.y = y;
    }
    
//    private void setActionUp(float x, float y) {
//        actionUp.x = x;
//        actionUp.y = y;
//    }
    
    public class DragPoint extends PointF {
        public int color;
    }

    public void starTestMode(boolean isChecked) {
        IS_TEST = isChecked;
        resetYTest();
        invalidate();
    }

    private void resetYTest() {
        Polygon ptest = polygonLists.get(rand.nextInt(polygonLists.size()));
        Y_TEST = ((ptest.getMaxY()>getHeight()?getHeight(): ptest.getMaxY())
                + (ptest.getMinY())>getHeight()?getHeight(): ptest.getMinY())/2;        
    }

    public void setUsingComplexData(boolean isChecked) {
        polygonLists.clear();
        if(!isChecked){
            initPolygon(100, 200);
        }else {
            initComplextData();  
        }
        invalidate();
    }
}
