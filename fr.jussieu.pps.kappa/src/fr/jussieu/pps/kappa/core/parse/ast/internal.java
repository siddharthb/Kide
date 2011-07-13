package fr.jussieu.pps.kappa.core.parse.ast;

import java.util.List; 
import java.util.ArrayList;
public class internal extends AST {
    public List<String> v1;
    public internal () {v1=new ArrayList<String>();}
    public internal mark (int l, int r) {super.setPos(l,r); return this;}
}