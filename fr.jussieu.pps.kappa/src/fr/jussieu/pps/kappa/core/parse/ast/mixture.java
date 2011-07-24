package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class mixture extends AST {
	public mixture mark (int l, int r) {super.setPos(l,r); return this;}
}