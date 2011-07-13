package fr.jussieu.pps.kappa.core.parse.ast;
public class rule extends AST {
    public alg_expr v1;
    public alg_expr v2;
    public mixture v3;
    public mixture v4;
    public arrow v5;
	public rule(mixture f1,arrow f2,mixture f3, alg_expr f4, alg_expr f5) {v3=f1;v5=f2;v4=f3;v1=f4;v2=f5;}
	public rule(mixture f1,arrow f2,mixture f3, alg_expr f4) {v3=f1;v5=f2;v4=f3;v1=f4;}
}