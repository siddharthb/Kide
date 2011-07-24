package fr.jussieu.pps.kappa.core.parse.ast;

public class Variables extends AST {
//	public variable v;
	public String name;
	public Variables(){}
public Variables mark(int l,int r){super.setPos(l,r); return this;}
public void nameMe(String s){name=s;}
}