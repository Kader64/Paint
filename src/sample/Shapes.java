package sample;

import javafx.scene.canvas.GraphicsContext;

public class Shapes{
    private final GraphicsContext gc;

    public Shapes(GraphicsContext gc){
        this.gc = gc;
    }
    public void drawStar(String type,double cx,double cy,int spikes,double outerRadius,double innerRadius){
        cx+=outerRadius/1.5;
        cy+=outerRadius/1.5;
        double rot = Math.PI / 2 * 3;
        double x;
        double y;
        double step = Math.PI / spikes;
        gc.beginPath();
        gc.moveTo(cx, cy - outerRadius);
        for(int i = 0; i < spikes; i++) {
            x = cx + Math.cos(rot) * outerRadius;
            y = cy + Math.sin(rot) * outerRadius;
            gc.lineTo(x, y);
            rot += step;
            x = cx + Math.cos(rot) * innerRadius;
            y = cy + Math.sin(rot) * innerRadius;
            gc.lineTo(x, y);
            rot += step;
        }
        gc.lineTo(cx, cy - outerRadius);
        gc.closePath();
        if(type=="fill")gc.fill();
        else gc.stroke();
    }
    public void drawHeart(String type,double x,double y,double size){
        double topCurveHeight = size * 0.3;
        x = x+size/2;
        gc.save();
        gc.beginPath();
        gc.moveTo(x, y + topCurveHeight);
        gc.bezierCurveTo(x, y, x - size / 2, y, x - size / 2, y + topCurveHeight);
        gc.bezierCurveTo(x - size / 2, y + (size + topCurveHeight) / 2, x, y + (size + topCurveHeight) / 2, x, y + size);
        gc.bezierCurveTo(x, y + (size + topCurveHeight) / 2, x + size / 2, y + (size + topCurveHeight) / 2, x + size / 2, y + topCurveHeight);
        gc.bezierCurveTo(x + size / 2, y, x, y, x, y + topCurveHeight);
        gc.closePath();
        if(type=="fill")gc.fill();
        else gc.stroke();
        gc.restore();
    }
    public void drawTriangle(String type,double x,double y, double size){
        x = x+size/2;
        gc.beginPath();
        gc.moveTo(x, y);
        gc.lineTo(x + size / 2, y + size);
        gc.lineTo(x - size / 2, y + size);
        gc.closePath();
        if(type=="fill")gc.fill();
        else gc.stroke();
    }
}
