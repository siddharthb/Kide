package fr.jussieu.pps.kappa.core.parse.ast;
public class OR extends bool_expr {
    public bool_expr v1;
    public bool_expr v2;
    public OR (bool_expr f1,bool_expr f2) {v1=f1;v2=f2;}
    public OR () {}
}