package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class variable extends AST {
	public variable mark (int l, int r) {super.setPos(l,r); return this;}
}