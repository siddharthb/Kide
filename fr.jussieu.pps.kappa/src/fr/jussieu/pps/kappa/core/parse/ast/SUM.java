package fr.jussieu.pps.kappa.core.parse.ast;
public class SUM extends alg_expr {
    public alg_expr v1;
    public alg_expr v2;
    public SUM (alg_expr f1,alg_expr f2) {v1=f1;v2=f2;}
    public SUM () {}
}