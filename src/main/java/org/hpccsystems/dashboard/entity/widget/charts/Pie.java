package org.hpccsystems.dashboard.entity.widget.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hpcc.HIPIE.dude.Element;
import org.hpcc.HIPIE.dude.ElementOption;
import org.hpcc.HIPIE.dude.FieldInstance;
import org.hpcc.HIPIE.dude.InputElement;
import org.hpcc.HIPIE.dude.RecordInstance;
import org.hpcc.HIPIE.dude.VisualElement;
import org.hpccsystems.dashboard.Constants.AGGREGATION;
import org.hpccsystems.dashboard.entity.widget.Attribute;
import org.hpccsystems.dashboard.entity.widget.Measure;
import org.hpccsystems.dashboard.entity.widget.Widget;
import org.hpccsystems.dashboard.util.DashboardUtil;
import org.zkoss.zul.ListModelList;

public class Pie extends Widget {
    private Attribute label;
    private Measure weight;
    private static final String DOT = ".";
    private static final String COMMA = " , ";
    

    @Override
    public String generateSQL() {        
        StringBuilder sql=new StringBuilder();
        sql.append("SELECT ")
        .append(getLogicalFile())
        .append(DOT)
        .append(label.getColumn())
        .append(COMMA);
        if(weight.getAggregation()!=null && weight.getAggregation()!= AGGREGATION.NONE){
            sql.append(weight.getAggregation())
            .append("(")
            .append(getLogicalFile())
            .append(DOT)
            .append(weight.getColumn())
            .append(")");
        }else{
            sql.append(getLogicalFile())
            .append(DOT)
            .append(weight.getColumn());
        }
        sql.append(" FROM ")
        .append(getLogicalFile());
        
        if((this.getFilters()!=null)&&(!this.getFilters().isEmpty())){
                sql.append(" WHERE ");
                this.getFilters().stream().forEach(eachFilter->{
                    sql.append(eachFilter.generateFilterSQL())
                    .append(" AND ");
                });                
                sql.substring(0, sql.length()-6);
            }
        return sql.toString();
    }

    public Attribute getLabel() {
        return label;
    }

    public void setLabel(Attribute label) {
        this.label = label;
    }

    public Measure getWeight() {
        return weight;
    }

    public void setWeight(Measure weight) {
        this.weight = weight;
    }
    @Override
    public List<String> getColumns() {
        List<String> columnList=new ArrayList<String>();
        columnList.add(label.getDisplayName());
        columnList.add(weight.getDisplayName());
        return columnList;
    }

    @Override
    public VisualElement generateVisualElement() {

        VisualElement visualElement = new VisualElement();
        // TODO:Need to set chart type using Hipie's 'Element' class
        visualElement.setType(this.getChartConfiguration().getHipieChartId());
        visualElement.addCustomOption(new ElementOption("_chartType",
                new FieldInstance(null, this.getChartConfiguration()
                        .getHipieChartName())));

        visualElement.setName(DashboardUtil.removeSpaceSplChar(this.getName()));

        RecordInstance ri = new RecordInstance();
        visualElement.setBasisQualifier(ri);

        // Attribute settings       
        ri.add(new FieldInstance(null, getPluginAttribute()));
        visualElement.addOption(new ElementOption(VisualElement.LABEL,
                new FieldInstance(null, getPluginAttribute())));

        // Measures settings
        ri.add(new FieldInstance((getWeight().getAggregation() != null) ? getWeight()
                .getAggregation().toString() : null, getPluginMeasure()));

        visualElement.addOption(new ElementOption(VisualElement.WEIGHT,
                new FieldInstance(null, getPluginMeasure())));

        // Setting Tittle for chart
        visualElement.addOption(new ElementOption(VisualElement.TITLE,
                new FieldInstance(null, this.getTitle())));

        return visualElement;
    }

    @Override
    public Map<String, String> getInstanceProperties() {
        Map<String, String> fieldNames = new HashMap<String, String>();
        fieldNames.put(getPluginAttribute(), this.getLabel().getColumn());
        fieldNames.put(getPluginMeasure(), this.getWeight().getColumn());
        return fieldNames;
    }

    @Override
    public List<InputElement> generateInputElement() {

        List<InputElement> inputs = new ListModelList<InputElement>();

        InputElement attributeInput = new InputElement();
        attributeInput.setName(getPluginAttribute());
        attributeInput.addOption(new ElementOption(Element.LABEL,
                new FieldInstance(null,getLabel().getColumn())));
        attributeInput.setType(InputElement.TYPE_FIELD);
        inputs.add(attributeInput);

        InputElement measureInput = new InputElement();
        measureInput.setName(getPluginMeasure());
        measureInput.addOption(new ElementOption(Element.LABEL,
                new FieldInstance(null,getWeight().getColumn())));
        measureInput.setType(InputElement.TYPE_FIELD);
        inputs.add(measureInput);

        return inputs;
    
    }

    @Override
    public boolean isConfigured() {
        return (this.weight!=null)&&(this.label!=null);
    }

    @Override
    public List<String> getSQLColumns() {
        List<String> sqlColumnList=new ArrayList<String>();
        sqlColumnList.add(label.getDisplayName());
        if((weight.getAggregation()!=null)&&(weight.getAggregation()!=AGGREGATION.NONE)){
            sqlColumnList.add(weight.getAggregation().toString()+"out1");
        }else{
            sqlColumnList.add(weight.getDisplayName());
        }        
        return sqlColumnList;
    }

    /**
     * generates Name as 'Attribute_chartName(ie: getName())'
     * @return String
     */
    public String getPluginAttribute() {
        StringBuilder builder = new StringBuilder();
        builder.append("Attribute").append("_").append(this.getName());
        return builder.toString();
    }

    /**
     * generates Name as 'Measure1_chartName[ie: getName()]'
     * @return String
     */
    public String getPluginMeasure() {
        StringBuilder builder = new StringBuilder();
        builder.append("Measure").append("_").append(this.getName());
        return builder.toString();
    }
    
    
}
