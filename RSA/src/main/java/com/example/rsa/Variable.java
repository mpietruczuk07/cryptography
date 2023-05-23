package com.example.rsa;

public class Variable {
    private String variable, value, name, formula;

    public Variable(String variable, String value, String name, String formula) {
        this.variable = variable;
        this.value = value;
        this.name = name;
        this.formula = formula;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
