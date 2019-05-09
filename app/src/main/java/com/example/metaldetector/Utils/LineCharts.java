package com.example.myfirstapp.Utils;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class LineCharts {
    private List<PointValue> values;
    private List<Line> lines;
    private LineChartData lineChartData;
    private List<Line> linesList;
    private List<PointValue> pointValueList;
    private List<PointValue> points;
    private int position = 0;
    private Axis axisY;
    public void makeCharts(final LineChartView lineChartView, float uT){

        PointValue value1 = new PointValue(position * 5, uT);
        value1.setLabel("00:00");
        pointValueList.add(value1);
        float x = value1.getX();
        float y = uT;

        Line line = new Line(pointValueList);
        line.setColor(Color.RED);
        line.setShape(ValueShape.DIAMOND);
        line.setCubic(true);
        line.setHasPoints(false);
        linesList.clear();
        linesList.add(line);
        lineChartData = initData(linesList);
        lineChartView.setLineChartData(lineChartData);

        Viewport port;
        if (x > 500) {
            port = initViewPort(x - 500, x, y);
        } else {
            port = initViewPort(0, 500 ,y);
        }
        lineChartView.setCurrentViewport(port);

        Viewport maxPort = initMaxViewPort(x,y);
        lineChartView.setMaximumViewport(maxPort);
        position++;
    }


    public void initView(LineChartView lineChartView) {
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();
        axisY = new Axis();
        lineChartData = initData(null);
        lineChartView.setLineChartData(lineChartData);
        Viewport port = initViewPort(0, 50,150);
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(false);
        lineChartView.setScrollEnabled(true);
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(true);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();
        points = new ArrayList<>();
    }



    protected LineChartData initData(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
       // data.setAxisXBottom(axisX);
        return data;
    }


    protected Viewport initMaxViewPort(float right,float top) {
        Viewport port = new Viewport();
        if(max > top){
            port.top = max + 150;
            // Log.d("IF",max + "");
        }else {
            max = top;
            port.top = max + 150;
            // Log.d("ELSE",max + "");
        }
        port.bottom = 0;
        port.left = 0;
        port.right = right + 500;
        return port;
    }



    float max = 150;
    protected Viewport initViewPort(float left, float right,float top) {
        Viewport port = new Viewport();
        if(max > top){
            port.top = max + 150;
            // Log.d("IF",max + "");
        }else {
            max = top;
            port.top = max + 150;
            //Log.d("ELSE",max + "");
        }
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }
}
