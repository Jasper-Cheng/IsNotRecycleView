package com.example.isnotrecycleview.Data;

public class ViewData {

    private float cTop;
    private float cBottom;
    private float cLeft;
    private float cRight;

    @Override
    public String toString() {
        return "ViewData{" +
                "cTop=" + cTop +
                ", cBottom=" + cBottom +
                ", cLeft=" + cLeft +
                ", cRight=" + cRight +
                '}';
    }

    public float getcTop() {
        return cTop;
    }

    public void setcTop(float cTop) {
        this.cTop = cTop;
    }

    public float getcBottom() {
        return cBottom;
    }

    public void setcBottom(float cBottom) {
        this.cBottom = cBottom;
    }

    public float getcLeft() {
        return cLeft;
    }

    public void setcLeft(float cLeft) {
        this.cLeft = cLeft;
    }

    public float getcRight() {
        return cRight;
    }

    public void setcRight(float cRight) {
        this.cRight = cRight;
    }
}
