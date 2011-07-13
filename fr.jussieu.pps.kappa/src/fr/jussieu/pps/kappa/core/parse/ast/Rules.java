package fr.jussieu.pps.kappa.core.parse.ast;

public class Rules extends AST {
	public rule_label v1;
	public rule v2;
	public Rules(rule_label f1,rule f2){v1=f1;v2=f2;}
public Rules mark(int l,int r){super.setPos(l,r); return this;}
}