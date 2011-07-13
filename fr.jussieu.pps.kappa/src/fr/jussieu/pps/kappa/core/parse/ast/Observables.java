package fr.jussieu.pps.kappa.core.parse.ast;

public class Observables extends AST {
	public alg_expr a;
	public Observables(alg_expr f1){a=f1;}
public Observables mark(int l,int r){super.setPos(l,r); return this;}
}