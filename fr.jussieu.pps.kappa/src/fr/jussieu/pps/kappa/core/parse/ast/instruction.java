package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class instruction extends AST {
public instruction mark(int l,int r){super.setPos(l,r); return this;}
}