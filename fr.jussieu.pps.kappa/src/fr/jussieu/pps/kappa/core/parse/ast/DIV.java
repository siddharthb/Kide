package fr.jussieu.pps.kappa.core.parse.ast;
public class DIV extends alg_expr {
    public alg_expr v1;
    public alg_expr v2;
    public DIV (alg_expr f1,alg_expr f2) {v1=f1;v2=f2;}
    public DIV () {}
}