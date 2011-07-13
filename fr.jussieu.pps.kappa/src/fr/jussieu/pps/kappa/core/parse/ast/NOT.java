package fr.jussieu.pps.kappa.core.parse.ast;
public class NOT extends bool_expr {
    public bool_expr v1;
    public NOT (bool_expr f1) {v1=f1;}
    public NOT () {}
}