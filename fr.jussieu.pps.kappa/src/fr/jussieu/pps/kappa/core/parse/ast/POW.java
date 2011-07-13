package fr.jussieu.pps.kappa.core.parse.ast;
public class POW extends alg_expr {
    public alg_expr v1;
    public alg_expr v2;
    public POW (alg_expr f1,alg_expr f2) {v1=f1;v2=f2;}
    public POW () {}
}