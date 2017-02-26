import java.io.Reader;

import edu.stanford.nlp.trees.DiskTreebank;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.NPTmpRetainingTreeNormalizer;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.TreeReaderFactory;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.trees.TreebankLanguagePack;

class TypedDependenciesDemo {

  /** Convert Penn Treebank parse trees in a file to typed dependencies
   *  (collapsed).  It does it two different ways for pedagogical reasons.
   *  This is for English, but is easy to generalize. 
   *  Usage: java TypedDependenciesDemo filename
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: java TypedDependenciesDemo filename");
      return;
    } 
    // The fancy footwork here is to keep -TMP functional tags if present.
    // If not available, you could just use the no-argument constructor.
    Treebank tb = new DiskTreebank(new TreeReaderFactory() {
        public TreeReader newTreeReader(Reader in) {
          return new PennTreeReader(in, new LabeledScoredTreeFactory(),
                                    new NPTmpRetainingTreeNormalizer());
        }});
    tb.loadPath(args[0]);
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    TreePrint tp = new TreePrint("typedDependenciesCollapsed");

    for (Tree t : tb) {
      GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
      System.out.println(gs.typedDependenciesCollapsed());
      System.out.println();
      tp.printTree(t);
      System.out.println("----------");
    }
  }

}
