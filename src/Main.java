
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

/**
 * A demo illustrating how to call the OpenIE system programmatically.
 */
public class Main {

	//static ArrayList<Event> events = new ArrayList<Event>();
	static ArrayList<Verb> verbs = new ArrayList<Verb>();
	static ArrayList<Noun> nouns = new ArrayList<Noun>();

	private Main() {
	} 

	public static void printtree(SemanticGraph a, IndexedWord root) {
		if (!a.hasChildren(root)) {
			return;
		} else {
			Set<IndexedWord> y = a.getChildren(root);
			System.out.println(root + "" + y);
			for (Iterator<IndexedWord> it = y.iterator(); it.hasNext();) {
				IndexedWord n = it.next();
				System.out.println(a.relns(n));
				printtree(a, n);
			}

		}
	}

	/*public static void checkCop(ArrayList<SemanticGraphEdge> sge) {
		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("cop")
					|| sge.get(i).getRelation().getShortName()
							.equals("auxpass")) {
				for (int j = 0; j < events.size(); j++) {
					if (events.get(j).rname.equals(sge.get(i).getGovernor()
							.lemma())) {
						events.get(j).cop = true;
						events.get(j).name = events.get(j).rname;
					}
				}
			}
		}
	}
*/
	public static void checkCopula(ArrayList<SemanticGraphEdge> sge, Verb e) {
		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("cop")
					|| sge.get(i).getRelation().getShortName().equals("aux")
					&& sge.get(i).getGovernor().tag().equals("NN")
					|| sge.get(i).getGovernor().tag().equals("JJ")) {
				if (e.name.equals(sge.get(i).getGovernor().lemma())) {
					e.cop = true;
				}
			}
		}
	}

/*	public static void logicForm(ArrayList<SemanticGraphEdge> sge) {

		Event tempa = null;

		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("nsubj")
					|| sge.get(i).getRelation().getShortName()
							.equals("nsubjpass")) {
				Event e = new Event(sge.get(i).getGovernor());
				events.add(e);
				checkCop(sge);
				tempa = e;
				if (!e.cop) {
					if (sge.get(i).getDependent().lemma().equals("who")
							|| sge.get(i).getDependent().lemma()
									.equals("whose")
							|| sge.get(i).getDependent().lemma().equals("that")) {
						Agent ag = new Agent(sge.get(i).getDependent(), e);
						ag.relcl = true;
						e.agent.add(ag);
					} else {
						Agent ag = new Agent(sge.get(i).getDependent(), e);
						e.agent.add(ag);
					}

				} else {
					if (sge.get(i).getDependent().lemma().equals("which")
							|| sge.get(i).getDependent().lemma()
									.equals("whose")
							|| sge.get(i).getDependent().lemma().equals("who")
							|| sge.get(i).getDependent().lemma().equals("that")) {
						Patient p = new Patient(sge.get(i).getDependent(), e);
						p.relcl = true;
						e.patient.add(p);
					} else {
						Patient p = new Patient(sge.get(i).getDependent(), e);
						e.patient.add(p);
					}
				}
			}
			if (sge.get(i).getRelation().getShortName().equals("acl")) {
				Event e = new Event(sge.get(i).getDependent());
				events.add(e);
				checkCop(sge);
				tempa = e;
				if (!e.cop) {
					if (sge.get(i).getDependent().lemma().equals("who")
							|| sge.get(i).getDependent().lemma()
									.equals("whose")
							|| sge.get(i).getDependent().lemma().equals("that")) {
						Agent ag = new Agent(sge.get(i).getGovernor(), e);
						ag.relcl = true;
						e.agent.add(ag);
					} else {
						Agent ag = new Agent(sge.get(i).getGovernor(), e);
						e.agent.add(ag);
					}

				} else {
					if (sge.get(i).getDependent().lemma().equals("which")
							|| sge.get(i).getDependent().lemma()
									.equals("whose")
							|| sge.get(i).getDependent().lemma().equals("who")
							|| sge.get(i).getDependent().lemma().equals("that")) {
						Patient p = new Patient(sge.get(i).getGovernor(), e);
						p.relcl = true;
						e.patient.add(p);
					} else {
						Patient p = new Patient(sge.get(i).getGovernor(), e);
						e.patient.add(p);
					}
				}
			}
			if (sge.get(i).getRelation().getShortName().equals("dobj")
					|| sge.get(i).getRelation().getShortName().equals("iobj")) {
				for (int j = 0; j < events.size(); j++) {
					Patient temp = null;
					if (sge.get(i).getGovernor().lemma()
							.equals(events.get(j).rname)) {
						Patient pa = new Patient(sge.get(i).getDependent(),
								events.get(j));
						events.get(j).patient.add(pa);
						temp = pa;
					}

				}

			}
			if (sge.get(i).getRelation().getShortName().equals("nmod")) {
				if (sge.get(i).getRelation().getSpecific().equals("to")
						|| sge.get(i).getRelation().getSpecific().equals("for")
						|| sge.get(i).getRelation().getSpecific().equals("at")
						|| sge.get(i).getRelation().getSpecific().equals("in")
						|| sge.get(i).getRelation().getSpecific().equals("on")
						|| sge.get(i).getRelation().getSpecific()
								.equals("with")
						|| sge.get(i).getRelation().getSpecific()
								.equals("from")) {
					for (int j = 0; j < events.size(); j++) {
						if (sge.get(i).getGovernor().lemma()
								.equals(events.get(j).rname)) {
							String name = sge.get(i).getRelation()
									.getSpecific();
							IndexedWord loc = sge.get(i).getDependent();
							Prepostion P = new Prepostion(name, loc,
									events.get(j));
							events.get(j).prep.add(P);
						} else {
							if (events.get(j).prep.size() > 0) {
								String name = sge.get(i).getRelation()
										.getSpecific();
								IndexedWord loc = sge.get(i).getDependent();
								Prepostion P = new Prepostion(name, loc,
										events.get(j));
								events.get(j).prep.add(P);

							}
						}
					}
				}
			}

		}

		for (int i = 0; i < sge.size(); i++) {

			if (sge.get(i).getRelation().getShortName().equals("ccomp")) {
				for (int j = 0; j < events.size(); j++) {
					if (sge.get(i).getGovernor().lemma()
							.equals(events.get(j).rname)) {
						Patient p = new Patient();
						String term = "";
						for (int k = 0; k < events.size(); k++) {
							if (sge.get(i).getDependent().lemma()
									.equals(events.get(k).rname)) {
								term = events.get(k).toString();
							}
						}
						p.term = "ccomp(" + events.get(j).code + "," + term
								+ ")";
						events.get(j).patient.add(p);
					}
				}
			}

			if (sge.get(i).getRelation().getShortName().equals("amod")) {
				for (int j = 0; j < events.size(); j++) {
					for (int k = 0; k < events.get(j).agent.size(); k++) {
						if (sge.get(i).getGovernor().lemma()
								.equals(events.get(j).agent.get(k).rname)) {
							events.get(j).agent.get(k).adj = sge.get(i)
									.getDependent().lemma();
							events.get(j).agent.get(k).adjs.add(sge.get(i)
									.getDependent().lemma());
							events.get(j).agent.get(k).term += "^"
									+ events.get(j).agent.get(k).adj + "("
									+ events.get(j).agent.get(k).code + ")";
						}
					}
					for (int l = 0; l < events.get(j).patient.size(); l++) {
						if (sge.get(i).getGovernor().lemma()
								.equals(events.get(j).patient.get(l).rname)) {

							events.get(j).patient.get(l).adj = sge.get(i)
									.getDependent().lemma();
							events.get(j).patient.get(l).adjs.add(sge.get(i)
									.getDependent().lemma());
							events.get(j).patient.get(l).term += "^"
									+ events.get(j).patient.get(l).adj + "("
									+ events.get(j).patient.get(l).code + ")";
						}
					}
				}
			}
			Agent temp = null;
			Patient temp2 = null;
			if (sge.get(i).getRelation().getShortName().equals("acl:relcl")) {
				for (int j = 0; j < events.size(); j++) {
					if (sge.get(i).getDependent().lemma()
							.equals(events.get(j).rname)) {
						if (!events.get(j).cop) {
							Agent ag = new Agent(sge.get(i).getGovernor(),
									events.get(j));
							for (int k = 0; k < events.get(j).agent.size(); k++) {
								if (events.get(j).agent.get(k).relcl) {
									events.get(j).agent.remove(k);
								}
							}
							events.get(j).agent.add(ag);
							temp = ag;
						} else {
							Patient p = new Patient(sge.get(i).getGovernor(),
									events.get(j));
							for (int k = 0; k < events.get(j).patient.size(); k++) {
								if (events.get(j).patient.get(k).relcl) {
									events.get(j).patient.remove(k);
								}
							}
							events.get(j).patient.add(p);
							temp2 = p;
						}
					}
					if (temp != null) {
						for (int l = 0; l < events.get(j).agent.size(); l++) {
							if (events.get(j).agent.get(l).rname
									.equals(temp.rname)
									&& !events.get(j).agent.get(l).code
											.equals(temp.code)) {

								events.get(j).agent.get(l).setCode(temp.code);

							}
						}
					}
					if (temp2 != null) {
						for (int l = 0; l < events.get(j).patient.size(); l++) {
							if (events.get(j).patient.get(l).rname
									.equals(temp2.rname)
									&& !events.get(j).patient.get(l).code
											.equals(temp2.code)) {
								events.get(j).patient.get(l)
										.setCode(temp2.code);
							}
						}
					}
				}
			}
			if (sge.get(i).getRelation().getShortName().equals("det")) {
				if (sge.get(i).getDependent().lemma().equals("every")
						|| sge.get(i).getDependent().lemma().equals("all")) {

					for (int j = 0; j < events.size(); j++) {
						for (int k = 0; k < events.get(j).agent.size(); k++) {
							if (events.get(j).agent.get(k).rname.equals(sge
									.get(i).getGovernor().lemma())) {
								Quantifiers q = new Quantifiers(sge.get(i)
										.getGovernor(), events.get(j));
								events.get(j).scopeAgent = true;
								events.get(j).quant.add(q);
								events.get(j).quantSymboles.add(q.antecode);
								events.get(j).agent.get(k).code = q.antecode;
							}
						}
						for (int k = 0; k < events.get(j).patient.size(); k++) {
							if (events.get(j).patient.get(k).rname.equals(sge
									.get(i).getGovernor().lemma())) {
								Quantifiers q = new Quantifiers(sge.get(i)
										.getGovernor(), events.get(j));
								events.get(j).scopePatient = true;
								events.get(j).quant.add(q);
								events.get(j).quantSymboles.add(q.antecode);
								events.get(j).patient.get(k).code = q.antecode;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).quant.size() > 0) {
				events.get(i).skolemization();
				System.out.println(events.get(i).toString());

			} else {
				System.out.println(events.get(i).toString());

			}
		}
	}
*/
	public static void collectVerbs(ArrayList<SemanticGraphEdge> sge) {
		//Simply collects all words tagged as verbs from CoreNLP output and calls collect nouns method
		//collects the output of both in two arraylists verbs and nouns
		collectNouns(sge);
		for (int i = 0; i < sge.size(); i++) {
			//ArrayList<Verb> temp = verbs;
			//ArrayList<Noun> temp2 = nouns;
			if (sge.get(i).getRelation().getShortName().equals("nsubj")
					|| (sge.get(i).getRelation().getShortName().equals("nmod") && sge
							.get(i).getRelation().getSpecific().equals("agent"))) {
				Verb e = new Verb(sge.get(i).getGovernor());
				checkCopula(sge, e);
				if (e.cop) {
					Noun patient = getNoun(sge, sge.get(i).getDependent(), e);
					patient.property = true;
					e.owners.add(patient);
					e.predicate = e.name;
					verbs.add(e);
				} else {
					Noun agent = getNoun(sge, sge.get(i).getDependent(), e);
					e.agents.add(agent);
					verbs.add(e);
				}
			}
			if (sge.get(i).getRelation().getShortName().equals("dobj")
					|| sge.get(i).getRelation().getShortName().equals("iobj")
					|| sge.get(i).getRelation().getShortName()
							.equals("nsubjpass")) {
				for (int j = 0; j < verbs.size(); j++) {
					if (sge.get(i).getGovernor().lemma()
							.equals(verbs.get(j).name)) {
						Noun patient = getNoun(sge, sge.get(i).getDependent(),
								verbs.get(j));
						verbs.get(j).patients.add(patient);
					}
				}
			}

			if (sge.get(i).getRelation().getShortName().equals("nmod")
					&& !sge.get(i).getRelation().getSpecific().equals("agent")) {
				for (int j = 0; j < verbs.size(); j++) {
					if (sge.get(i).getGovernor().lemma()
							.equals(verbs.get(j).name)) {
						Noun prepostion = getNoun(sge, sge.get(i)
								.getDependent(), verbs.get(j));
						verbs.get(j).prepostions.add(prepostion);
					}
				}
			}
		}

		for (int i = 0; i < sge.size(); i++) {

			if (sge.get(i).getRelation().getShortName().equals("ccomp")) {
				for (int j = 0; j < verbs.size(); j++) {
					if (sge.get(i).getGovernor().lemma()
							.equals(verbs.get(j).name)) {
						String ccomp = "";
						for (int k = 0; k < verbs.size(); k++) {
							if (sge.get(i).getDependent().lemma()
									.equals(verbs.get(k).name)) {
								ccomp = verbs.get(k).toString(1);
							}
						}

						verbs.get(j).ccomp.add(ccomp);
					}
				}
			}

			if (sge.get(i).getRelation().getShortName().equals("nsubjpass")) {
				for (int j = 0; j < verbs.size(); j++) {
					if (sge.get(i).getGovernor().lemma()
							.equals(verbs.get(j).name)) {
						Noun patient = getNoun(sge, sge.get(i).getDependent(),
								verbs.get(j));
						verbs.get(j).patients.add(patient);
					}
				}
			}
		}
		for (int i = 0; i < verbs.size(); i++) {
			System.out.println(verbs.get(i).toString(2));
		}

	}

	public static void collectNouns(ArrayList<SemanticGraphEdge> sge) {
		//Extracts all the nouns in the sentence and get their determiners if they are not a named entity
		// calls getAdjectives at the end to check for Adjectives that denotes these nouns
		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("nsubj")
					|| (sge.get(i).getRelation().getShortName().equals("nmod") && sge
							.get(i).getRelation().getSpecific().equals("agent"))) {
				IndexedWord word = sge.get(i).getDependent();
				if (word.lemma().equals("who") || word.lemma().equals("which")
						|| word.lemma().equals("that")
						|| word.lemma().equals("whose")) {
					continue;
				}
				String det = "";
				if (word.ner().equals("O"))
					det = getDeterminers(sge, word);

				Noun n = new Noun(word, det);
				nouns.add(n);

			}
			if (sge.get(i).getRelation().getShortName().equals("dobj")
					|| sge.get(i).getRelation().getShortName().equals("iobj")
					|| sge.get(i).getRelation().getShortName()
							.equals("nsubjpass")) {
				IndexedWord word = sge.get(i).getDependent();
				if (word.lemma().equals("who") || word.lemma().equals("Which")
						|| word.lemma().equals("that")
						|| word.lemma().equals("whose")) {
					continue;
				}
				String det = "";
				if (word.ner().equals("O"))
					det = getDeterminers(sge, word);

				Noun n = new Noun(word, det);
				nouns.add(n);
			}

			if (sge.get(i).getRelation().getShortName().equals("case")) {
				IndexedWord word = sge.get(i).getGovernor();
				String det = "";
				if (word.ner().equals("O"))
					det = getDeterminers(sge, word);

				Noun n = new Noun(word, det);
				n.prepostion = sge.get(i).getDependent().lemma();
				nouns.add(n);
			}

		}
		collectAdjectives(sge);
	}

	public static void collectAdjectives(ArrayList<SemanticGraphEdge> sge) {
		//collect all the adjectives for a certain noun.
		
		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("amod")) {
				IndexedWord word = sge.get(i).getDependent();
				String det = "";
				Noun n = new Noun(word, det);
				Noun mod = getNoun(sge, sge.get(i).getGovernor(), null);
				mod.amods.add(n);
				mod.setAdjectives();
				nouns.add(n);
			}
		}
	}

	public static Noun getNoun(ArrayList<SemanticGraphEdge> sge, IndexedWord word, Verb e) {
		//
		if (word.lemma().equals("who") || word.lemma().equals("which")
				|| word.lemma().equals("that") || word.lemma().equals("whose")) {
			word = getsubj(sge, word, e);
		}
		for (int i = 0; i < nouns.size(); i++) {
			if (nouns.get(i).name.equals(word.lemma())) {
				return nouns.get(i);
			}
		}
		return null;
	}

	public static IndexedWord getsubj(ArrayList<SemanticGraphEdge> sge,
			IndexedWord word, Verb e) {
		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("acl:relcl")) {
				if (sge.get(i).getDependent().lemma().equals(e.name)) {
					return sge.get(i).getGovernor();
				}
			}
		}
		return null;
	}

	public static String getDeterminers(ArrayList<SemanticGraphEdge> sge, IndexedWord word) {
		//collects the determiners for all the nouns to know if they will have existential or universal quantifiers
		for (int i = 0; i < sge.size(); i++) {
			if (sge.get(i).getRelation().getShortName().equals("det")) {
				if (sge.get(i).getGovernor().lemma().equals(word.lemma())) {
					return sge.get(i).getDependent().lemma();
				}
			}
		}
		return "";
	}

	public static void main(String[] args) throws Exception {
		// Create the Stanford CoreNLP pipeline
		
		Properties props = PropertiesUtils.asProperties("annotators",
				"tokenize,ssplit,pos,lemma,ner,depparse"
				// , "	depparse.model",
		// "edu/stanford/nlp/models/parser/nndep/english_SD.gz"
		// "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,openie"
		// , "parse.originalDependencies", "true"
				);
		//These are the properties needed for part of speech tagger 
		//to work other properties are commented above if needed
		
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// Annotate an example document.
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 10; i++) {
			verbs = new ArrayList<Verb>();
			nouns = new ArrayList<Noun>();
			String text;
			if (args.length > 0) {
				text = IOUtils.slurpFile(args[0]);
			} else {
				text = sc.nextLine();
			}
			Annotation doc = new Annotation(text);
			pipeline.annotate(doc);

			// Loop over sentences in the document, exctracting all words part of speech
			int sentNo = 0;
			for (CoreMap sentence : doc
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				System.out.println("Sentence #" + ++sentNo + ": "
						+ sentence.get(CoreAnnotations.TextAnnotation.class));

				SemanticGraph a = sentence
						.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

				System.out.println(a.toString(SemanticGraph.OutputFormat.LIST));

				ArrayList<SemanticGraphEdge> sge = new ArrayList<SemanticGraphEdge>();

				for (SemanticGraphEdge e : a.edgeIterable()) {
					sge.add(e);
				}
			
				//collect verbs is the method that intiates the whole translation process from english to FOPL
				collectVerbs(sge);

			}
		}
	}
}