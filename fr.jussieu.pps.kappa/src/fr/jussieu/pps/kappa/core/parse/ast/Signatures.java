package fr.jussieu.pps.kappa.core.parse.ast;

public class Signatures extends AST {
	public agent a;
	public Signatures(agent f1){a=f1;}
public Signatures mark(int l,int r){super.setPos(l,r); return this;}
}