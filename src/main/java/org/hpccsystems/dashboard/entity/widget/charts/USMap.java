package org.hpccsystems.dashboard.entity.widget.charts;

import java.util.List;

import org.hpccsystems.dashboard.entity.widget.Attribute;
import org.hpccsystems.dashboard.entity.widget.Measure;
import org.hpccsystems.dashboard.entity.widget.Widget;

public class USMap extends Widget{

    private Attribute states;
    private Measure measure;
    
    @Override
    public List<String> getColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    public Attribute getStates() {
        return states;
    }

    public void setStates(Attribute states) {
        this.states = states;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

}
