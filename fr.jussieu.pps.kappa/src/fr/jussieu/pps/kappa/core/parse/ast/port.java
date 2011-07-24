package fr.jussieu.pps.kappa.core.parse.ast;

public class port extends AST {
    public String port_nme;
    public internal port_int;
    public link port_lnk;
    public port (String f1,internal f2,link f3) {port_nme=f1;port_int=f2;port_lnk=f3;}
    public port mark (int l, int r) {super.setPos(l,r); return this;}
}