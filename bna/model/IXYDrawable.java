package bna.model;

import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public interface IXYDrawable {

    SimpleDateFormat formatDate = new SimpleDateFormat("MMM yyyy");

    static String getDateFormat(Calendar calendar) {
        return formatDate.format(calendar.getTime());
    }

    XYChart.Series getSeries();
}
