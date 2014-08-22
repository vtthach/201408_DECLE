package com.example.dragtofillcolor;

import android.graphics.Color;
import android.graphics.Path;

public class Polygon {
	private float[] polyY, polyX;
	private String articleID;
	private float minX, maxX, minY, maxY;
	private int polySides;
	
	// Is preview(action move)
	public boolean isPreview;
	
	// Is filled(action up)
    public boolean isFilled;

	public Polygon(float[] px, float[] py, String articleID) {
		this.articleID = articleID;
		setPolyX(px);
		setPolyY(py);
		polySides = px.length;
		setMinMax();
	}

	public Polygon() {

	}

	public boolean contains(float x, float y) {
		boolean isContains = false;

		for (int i = 0, j = polySides - 1; i < polySides; j = i++) {
			if ((polyY[i] < y && polyY[j] >= y)
					|| (polyY[j] < y && polyY[i] >= y)) {
				if (polyX[i] + (y - polyX[i])
						/ (polyY[j] - polyY[i])
						* (polyX[j] - polyX[i]) < x) {
					isContains = !isContains;
				}
			}
		}
		return isContains;
	}
	
    public boolean contains(int x, int y) {
        boolean c = false;
        int i, j = 0;
        for (i = 0, j = polySides - 1; i < polySides; j = i++) {
            if (((polyY[i] > y) != (polyY[j] > y)) &&
                (x < (polyX[j] - polyX[i]) * (y - polyY[i]) / (polyY[j] - polyY[i]) + polyX[i]))
                c = !c;
        }
        return c;
    }

	public String getArticleID() {
		return articleID;
	}

	public float[] getPolyX() {
		return polyX;
	}

	public void setPolyX(float[] polyX) {
		this.polyX = polyX;
	}

	public float[] getPolyY() {
		return polyY;
	}

	public void setPolyY(float[] polyY) {
		this.polyY = polyY;
	}

	public Path getPathFromPolygon(float leftF, int left, float topF, int top,
			float imgScaleX, float imgScaleY, float xFactor, float yFactor) {
		float x[], y[];
		x = polyX.clone();
		y = polyY.clone();
		for (int i = 0; i < polySides; i++) {
			x[i] *= xFactor;
			x[i] += left;
			x[i] *= imgScaleX;
			x[i] += leftF;

			y[i] *= yFactor;
			y[i] += top;
			y[i] *= imgScaleY;
			y[i] += topF;
		}
		
		Path p = new Path();
		p.moveTo(x[0], y[0]);
		for (int i = 1; i < polySides; i++) {
			p.lineTo(x[i], y[i]);
		}
		p.lineTo(x[0], y[0]);
		return p;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMinX() {
		return minX;
	}

	public void setMinMax() {
		minX = maxX = polyX[0];
		minY = maxY = polyY[0];
		for (int i = 1; i < polySides; i++) {
			if (minX > polyX[i])
				minX = polyX[i];
			if (minY > polyY[i])
				minY = polyY[i];

			if (maxX < polyX[i])
				maxX = polyX[i];
			if (maxY < polyY[i])
				maxY = polyY[i];
		}
	}
	
    Path p;
    public int color = CustomView.COLOR_STROKE;

	public Path getPath(){
        if (p == null) {
            p = new Path();
	        p.moveTo(polyX[0], polyY[0]);
	        for (int i = 1; i < polySides; i++) {
	            p.lineTo(polyX[i], polyY[i]);
	        }
	        p.lineTo(polyX[0], polyY[0]);
	    }
        return p;
	}

}
