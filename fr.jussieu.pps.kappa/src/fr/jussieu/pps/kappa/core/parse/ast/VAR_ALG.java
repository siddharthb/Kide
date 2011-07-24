package fr.jussieu.pps.kappa.core.parse.ast;
public class VAR_ALG extends Variables {
    public alg_expr v1;
	public String v3;
    public VAR_ALG (alg_expr f1,String f3) {v1=f1;v3=f3;super.nameMe(f3);}
}