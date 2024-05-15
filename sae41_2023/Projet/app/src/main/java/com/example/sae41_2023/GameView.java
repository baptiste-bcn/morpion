package com.example.sae41_2023;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.os.Handler;
import android.content.SharedPreferences;
import android.widget.Toast;

public class GameView extends View {

    private Point startPoint;
    private Paint paint, crossPaint, pathPaint;
    private Path drawPath;
    private final int cellSize = 83;
    private int offsetX = 0, offsetY = 0;
    private int score = 0;
    private boolean isTwoFingerTouch = false;
    private float lastTouchX, lastTouchY;
    private int currentCrossSize;
    private final HashMap<Point, Boolean> crossDictionary = new HashMap<>();
    private final ArrayList<Point> crossPositions = new ArrayList<>();
    private final Handler handler = new Handler();
    private final List<Pair<Point, Point>> validPaths = new ArrayList<>();
    private final HashSet<HashSet<Point>> validPathsPoints = new HashSet<>();
    private boolean shouldRecenter = false;
    private boolean isGameOver = false;

    private final Runnable recenterRunnable = this::recenter;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        crossPaint = new Paint();
        crossPaint.setColor(Color.BLACK);
        crossPaint.setStyle(Paint.Style.STROKE);
        crossPaint.setStrokeWidth(15);

        pathPaint = new Paint();
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(10);
        pathPaint.setAntiAlias(true);

        drawPath = new Path();

        loadCrossSizeFromPreferences();

        initPlateauDeBase();
    }

    public void recenter() {
        offsetX = 0;
        offsetY = 0;
        invalidate();
    }

    public void loadCrossSizeFromPreferences() {
        SharedPreferences prefs = getContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        currentCrossSize = prefs.getInt("CrossSize", 36);
        invalidate();
    }

    private void initPlateauDeBase() {
        drawBigCross();
    }

    private void drawBigCross(){

        int armSize = currentCrossSize / 4;
        int lineSize = armSize / 3;
        int posX = 5;
        int posY = 10;
        int actualPos = posX;


        for (int i = 0; i < lineSize; i++) {
            posY--;
            addCross(new Point(actualPos, posY));
        }
        actualPos = posY;

        for (int i = 0; i < lineSize; i++) {
            posX++;
            addCross(new Point(posX, actualPos));
        }
        actualPos = posX;

        for (int i = 0; i < lineSize; i++) {
            posY++;
            addCross(new Point(actualPos, posY));
        }
        actualPos = posY;

        for (int i = 0; i < lineSize; i++) {
            posX++;
            addCross(new Point(posX, actualPos));
        }
        actualPos = posX;

        for (int i = 0; i < lineSize; i++) {
            posY++;
            addCross(new Point(actualPos, posY));
        }
        actualPos = posY;

        for (int i = 0; i < lineSize; i++) {
            posX--;
            addCross(new Point(posX, actualPos));
        }
        actualPos = posX;

        for (int i = 0; i < lineSize; i++) {
            posY++;
            addCross(new Point(actualPos, posY));
        }
        actualPos = posY;

        for (int i = 0; i < lineSize; i++) {
            posX--;
            addCross(new Point(posX, actualPos));
        }
        actualPos = posX;

        for (int i = 0; i < lineSize; i++) {
            posY--;
            addCross(new Point(actualPos, posY));
        }
        actualPos = posY;

        for (int i = 0; i < lineSize; i++) {
            posX--;
            addCross(new Point(posX, actualPos));
        }
        actualPos = posX;

        for (int i = 0; i < lineSize; i++) {
            posY--;
            addCross(new Point(actualPos, posY));
        }
        actualPos = posY;

        for (int i = 0; i < lineSize; i++) {
            posX++;
            addCross(new Point(posX, actualPos));
        }
    }

    private void addCross(Point gridPoint) {
        crossPositions.add(gridPoint);
        crossDictionary.put(gridPoint, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInfiniteGrid(canvas);
        drawSmallCrosses(canvas);

        for (Pair<Point, Point> pair : validPaths) {
            Point startGridPoint = pair.first;
            Point endGridPoint = pair.second;

            float startX = startGridPoint.x * cellSize + offsetX;
            float startY = startGridPoint.y * cellSize + offsetY;
            float endX = endGridPoint.x * cellSize + offsetX;
            float endY = endGridPoint.y * cellSize + offsetY;

            canvas.drawLine(startX, startY, endX, endY, pathPaint);
        }

        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.BLUE);
        scorePaint.setTextSize(60);
        scorePaint.setAntiAlias(true);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("Score: " + score, 50, 100, scorePaint);

        canvas.drawPath(drawPath, pathPaint);
    }

    private void drawSmallCross(Canvas canvas, Point centerGridPoint, int cellSize) {
        float crossArmSize = 4;
        float centerX = centerGridPoint.x * cellSize + offsetX;
        float centerY = centerGridPoint.y * cellSize + offsetY;
        canvas.drawLine(centerX - cellSize / crossArmSize, centerY, centerX + cellSize / crossArmSize, centerY, crossPaint);
        canvas.drawLine(centerX, centerY - cellSize / crossArmSize, centerX, centerY + cellSize / crossArmSize, crossPaint);
    }

    private void drawSmallCrosses(Canvas canvas) {
        for (Point gridPoint : crossPositions) {
            drawSmallCross(canvas, gridPoint, cellSize);
        }
    }

    private void drawInfiniteGrid(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int numLinesHorizontal = (int) Math.ceil((float) width / cellSize) + 2;
        int numLinesVertical = (int) Math.ceil((float) height / cellSize) + 2;

        for (int i = -1; i < numLinesHorizontal - 1; i++) {
            float x = i * cellSize + (offsetX % cellSize);
            canvas.drawLine(x, 0, x, height, paint);
        }
        for (int j = -1; j < numLinesVertical - 1; j++) {
            float y = j * cellSize + (offsetY % cellSize);
            canvas.drawLine(0, y, width, y, paint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());

        // Sauvegarde des positions des croix
        bundle.putSerializable("crossPositions", crossPositions);

        // Sauvegarde des traits
        ArrayList<Integer> pathsData = new ArrayList<>();
        for (Pair<Point, Point> path : validPaths) {
            pathsData.add(path.first.x);
            pathsData.add(path.first.y);
            pathsData.add(path.second.x);
            pathsData.add(path.second.y);
        }
        bundle.putIntegerArrayList("validPaths", pathsData);

        // Ajout du score au Bundle
        bundle.putInt("score", score);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            ArrayList<Point> savedCrossPositions = (ArrayList<Point>) bundle.getSerializable("crossPositions");
            if (savedCrossPositions != null) {
                crossPositions.clear();
                crossPositions.addAll(savedCrossPositions);
            }

            ArrayList<Integer> pathsData = bundle.getIntegerArrayList("validPaths");
            if (pathsData != null) {
                validPaths.clear();
                for (int i = 0; i < pathsData.size(); i += 4) {
                    Point start = new Point(pathsData.get(i), pathsData.get(i + 1));
                    Point end = new Point(pathsData.get(i + 2), pathsData.get(i + 3));
                    validPaths.add(new Pair<>(start, end));
                }
            }

            score = bundle.getInt("score", 0); // Utilisez 0 comme valeur par défaut

            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    private Point findEmptyIntersection(Point start, Point end) {
        int deltaX = end.x - start.x;
        int deltaY = end.y - start.y;
        int steps = Math.max(Math.abs(deltaX), Math.abs(deltaY));

        for (int i = 0; i <= steps; i++) {
            int currentX = start.x + (i * (deltaX != 0 ? deltaX / Math.abs(deltaX) : 0));
            int currentY = start.y + (i * (deltaY != 0 ? deltaY / Math.abs(deltaY) : 0));
            Point currentPoint = new Point(currentX, currentY);

            Boolean hasCross = crossDictionary.get(currentPoint);
            if (hasCross == null || !hasCross) {
                return currentPoint;
            }
        }
        return null;
    }

    private void addValidPathPoints(Point start, Point end) {
        HashSet<Point> pathPoints = new HashSet<>();
        int deltaX = end.x - start.x;
        int deltaY = end.y - start.y;
        int steps = Math.max(Math.abs(deltaX), Math.abs(deltaY));

        for (int i = 0; i <= steps; i++) {
            int currentX = start.x + i * (deltaX != 0 ? deltaX / Math.abs(deltaX) : 0);
            int currentY = start.y + i * (deltaY != 0 ? deltaY / Math.abs(deltaY) : 0);
            pathPoints.add(new Point(currentX, currentY));
        }

        validPathsPoints.add(pathPoints);
    }

    private boolean checkLineValidity(Point start, Point end) {
        int deltaX = end.x - start.x;
        int deltaY = end.y - start.y;

        if (Math.abs(deltaX) != Math.abs(deltaY) && deltaX != 0 && deltaY != 0) {
            return false;
        }

        int steps = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        if (steps != 4) return false;

        ArrayList<Point> newLinePoints = new ArrayList<>();
        int crossCount = 0;
        boolean foundEmptyIntersection = false;

        for (int i = 0; i <= steps; i++) {
            int currentX = start.x + i * (deltaX != 0 ? deltaX / Math.abs(deltaX) : 0);
            int currentY = start.y + i * (deltaY != 0 ? deltaY / Math.abs(deltaY) : 0);
            Point currentPoint = new Point(currentX, currentY);
            newLinePoints.add(currentPoint);

            Boolean hasCross = crossDictionary.get(currentPoint);
            if (hasCross != null && hasCross) {
                crossCount++;
            } else if (!foundEmptyIntersection) {
                foundEmptyIntersection = true;
            }
        }

        if (crossCount != 4 || !foundEmptyIntersection) {
            return false;
        }

        for (HashSet<Point> existingLine : validPathsPoints) {
            int sharedPoints = 0;
            for (Point p : newLinePoints) {
                if (existingLine.contains(p)) {
                    sharedPoints++;
                    if (sharedPoints > 2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Point getNearestIntersection(float touchX, float touchY) {
        int nearestX = Math.round((touchX - offsetX) / cellSize);
        int nearestY = Math.round((touchY - offsetY) / cellSize);
        return new Point(nearestX, nearestY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            if (event.getPointerCount() == 2) {
                float touchX = (event.getX(0) + event.getX(1)) / 2;
                float touchY = (event.getY(0) + event.getY(1)) / 2;
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN:
                        lastTouchX = touchX;
                        lastTouchY = touchY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = touchX - lastTouchX;
                        float dy = touchY - lastTouchY;
                        if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                            offsetX += dx;
                            offsetY += dy;
                            lastTouchX = touchX;
                            lastTouchY = touchY;
                            invalidate();
                        }
                        break;
                }
            }
            return true;
        }

        float touchX = event.getX();
        float touchY = event.getY();
        if (event.getPointerCount() == 2) {
            touchX = (event.getX(0) + event.getX(1)) / 2;
            touchY = (event.getY(0) + event.getY(1)) / 2;
        }

        Point gridPoint = getNearestIntersection(touchX, touchY);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2 && !isTwoFingerTouch) {
                    isTwoFingerTouch = true;
                    shouldRecenter = true;
                    lastTouchX = touchX;
                    lastTouchY = touchY;
                    handler.postDelayed(() -> {
                        if (shouldRecenter) {
                            recenter();
                        }
                    }, 3000);
                } else if (event.getPointerCount() == 1) {
                    startPoint = gridPoint;
                    drawPath.moveTo(gridPoint.x * cellSize + offsetX, gridPoint.y * cellSize + offsetY);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isTwoFingerTouch) {
                    handler.removeCallbacks(recenterRunnable);

                    float dx = touchX - lastTouchX;
                    float dy = touchY - lastTouchY;

                    if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                        shouldRecenter = false;
                        offsetX += (int) dx;
                        offsetY += (int) dy;
                        lastTouchX = touchX;
                        lastTouchY = touchY;
                        invalidate();
                    }
                } else {
                    drawPath.reset();
                    drawPath.moveTo(startPoint.x * cellSize + offsetX, startPoint.y * cellSize + offsetY);
                    drawPath.lineTo(gridPoint.x * cellSize + offsetX, gridPoint.y * cellSize + offsetY);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (!isTwoFingerTouch && startPoint != null) {
                    if (checkLineValidity(startPoint, gridPoint)) {
                        validPaths.add(new Pair<>(new Point(startPoint), new Point(gridPoint)));
                        score++;
                        drawPath.reset();

                        addValidPathPoints(startPoint, gridPoint);

                        Point emptyIntersection = findEmptyIntersection(startPoint, gridPoint);
                        if (emptyIntersection != null) {
                            addCross(emptyIntersection);
                        }

                        invalidate();
                    } else {
                        drawPath.reset();
                        invalidate();
                    }
                }
                if (isTwoFingerTouch) {
                    shouldRecenter = false;
                }
                isTwoFingerTouch = false;
                startPoint = null;
                break;
        }
        return true;
    }

    private List<Point> getAllDirectionPoints() {
        List<Point> directions = new ArrayList<>();
        directions.add(new Point(0, 1));
        directions.add(new Point(1, 0));
        directions.add(new Point(0, -1));
        directions.add(new Point(-1, 0));
        return directions;
    }

    private boolean isValidPath(Point start, Point end) {
        boolean isLinear = start.x == end.x || start.y == end.y;
        if (!isLinear) {
            return false;
        }

        int stepX = Integer.compare(end.x, start.x);
        int stepY = Integer.compare(end.y, start.y);

        int currentX = start.x, currentY = start.y;
        while (currentX != end.x || currentY != end.y) {
            currentX += stepX;
            currentY += stepY;

            Point currentPoint = new Point(currentX, currentY);
            if (crossDictionary.containsKey(currentPoint) && !crossDictionary.get(currentPoint)) {
                return false;
            }
        }

        return true; // Le chemin est valide
    }

    private void showEndGameMessage() {
        Toast.makeText(getContext(), "Partie terminée ! Score final : " + score, Toast.LENGTH_LONG).show();
    }

    private boolean checkForEndGame() {
        for (int i = 0; i < getWidth(); i += cellSize) {
            for (int j = 0; j < getHeight(); j += cellSize) {
                Point gridPoint = new Point(i / cellSize, j / cellSize);
                if (!crossDictionary.containsKey(gridPoint)) {
                    crossDictionary.put(gridPoint, true);
                    for (Point direction : getAllDirectionPoints()) {
                        Point endPoint = new Point(gridPoint.x + direction.x, gridPoint.y + direction.y);
                        if (isValidPath(gridPoint, endPoint)) {
                            crossDictionary.remove(gridPoint);
                            return false;
                        }
                    }
                    crossDictionary.remove(gridPoint);
                }
            }
        }
        return true;
    }

    private void afterMove() {
        if (checkForEndGame()) {
            showEndGameMessage();
            isGameOver = true;
        }
        invalidate();
    }
}