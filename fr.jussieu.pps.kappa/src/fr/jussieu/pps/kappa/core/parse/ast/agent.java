package fr.jussieu.pps.kappa.core.parse.ast;
public class agent extends AST {
    public String ag_nme;
    public interface1 ag_intf;
    public agent (String f1,interface1 f2) {ag_nme=f1;ag_intf=f2;}
    public agent mark (int l, int r) {super.setPos(l,r); return this;}
}