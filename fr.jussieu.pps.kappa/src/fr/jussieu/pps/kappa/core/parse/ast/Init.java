package fr.jussieu.pps.kappa.core.parse.ast;

public class Init extends AST {
	public alg_expr v1;
	public mixture v2;
	public Init(alg_expr f1,mixture f2){v1=f1;v2=f2;}
public Init mark(int l,int r){super.setPos(l,r); return this;}
}