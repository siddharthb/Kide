package fr.jussieu.pps.kappa.core.parse.ast;
public class INIT extends instruction {
    public alg_expr v1;
    public mixture v2;
    public INIT (alg_expr f1,mixture f2) {v1=f1;v2=f2;}
    public INIT () {}
}