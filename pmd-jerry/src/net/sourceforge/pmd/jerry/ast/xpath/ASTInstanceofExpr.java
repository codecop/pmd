/* Generated By:JJTree: Do not edit this line. ASTInstanceofExpr.java */

package net.sourceforge.pmd.jerry.ast.xpath;

public class ASTInstanceofExpr extends SimpleNode {
  public ASTInstanceofExpr(int id) {
    super(id);
  }

  public ASTInstanceofExpr(XPath2Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(XPath2ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}