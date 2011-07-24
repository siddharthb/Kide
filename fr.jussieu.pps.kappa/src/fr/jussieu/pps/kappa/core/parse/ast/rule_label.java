package fr.jussieu.pps.kappa.core.parse.ast;
public class rule_label extends AST {
    public String v4;
    public String v5;
	public rule_label(String f2,String f3) {v5=f2;v4=f3;}
	public rule_label(String f2) {v5=f2;v4=new String();}
	public rule_label() {v5=new String();v4=new String();}

	public rule_label mark (int l, int r) {super.setPos(l,r); return this;}
}