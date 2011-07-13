package fr.jussieu.pps.kappa.core.parse.ast;
public class UPDATE extends modif_expr {
    public alg_expr v1;
    public String v2;
    public UPDATE (String f1,alg_expr f2) {v1=f2;v2=f1;}
    public UPDATE () {}
}