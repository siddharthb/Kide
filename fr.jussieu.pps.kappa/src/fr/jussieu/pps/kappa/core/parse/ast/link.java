package fr.jussieu.pps.kappa.core.parse.ast;

public class link extends AST {
	
	public link(){}
	public link mark (int l, int r) {super.setPos(l,r); return this;}
}