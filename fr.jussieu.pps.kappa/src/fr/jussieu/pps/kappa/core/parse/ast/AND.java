package fr.jussieu.pps.kappa.core.parse.ast;
public class AND extends bool_expr {
    public bool_expr v1;
    public bool_expr v2;
    public AND (bool_expr f1,bool_expr f2) {v1=f1;v2=f2;}
    public AND () {}
}