package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class arrow extends AST {
	public arrow mark (int l, int r) {super.setPos(l,r); return this;}
}