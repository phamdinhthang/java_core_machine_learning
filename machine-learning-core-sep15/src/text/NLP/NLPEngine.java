package text.NLP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;



public class NLPEngine {
	public static String textPreProcessing(String input) {
		input = removeExcessWhitespaces(input);
		input = removeSpecialChar(input);
		return input.toLowerCase();
	}
	public static String removeExcessWhitespaces(String input) {
		return input.replaceAll("\\s+", " ");
	}
	public static String removeSpecialChar(String input) {
		return input.replaceAll("[^A-Za-z0-9\\s]", "");
	}
	public static Map<String, Integer> toBagOfWord_Count(String input) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		for(String word: input.split("\\W+")) {
			Integer dk = res.get(word);
			res.put(word.toLowerCase(), dk==null ? 1 : dk+1);
		}
		return res;
	}
	public static Map<String, Double> toBagOfWord_Ratio(String input) {
		int word_count = input.split("\\W+").length;
		Map<String, Integer> bow = toBagOfWord_Count(input);
		Map<String, Double> res = new HashMap<String, Double>();
		for (String word:bow.keySet()) 
			res.put(word.toLowerCase(), new Double((double)bow.get(word).intValue()/(double)word_count));
		return res;
	}
	public static Map<String, Integer> toBagOfNgrams_Count(String input,int ngrams) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		String[] split = input.split("\\W+");
		for (int i=0;i<split.length-ngrams+1;i++) {
			String phrase = mergeNgrams(split,i,ngrams);
			Integer dk = res.get(phrase);
			res.put(phrase.toLowerCase(), dk==null ? 1 : dk+1);
		}
		return res;
	}
	private static String mergeNgrams(String[] split, int start, int n) {
		String res = "";
		try {
			StringBuilder s = new StringBuilder();
			for (int i=0;i<n;i++) s.append(split[start+i] + " ");
			res = s.toString().trim();
		} catch (Exception e) { e.printStackTrace(); }
		return res;
	}
	
	public static String mergeString(List<? extends Object> objs,String joiner) {
		if (joiner == null) joiner = " ";
		StringBuilder s = new StringBuilder();
		for (Object obj:objs) s.append(obj.toString() + joiner);
		return s.toString().toLowerCase().trim();
	}
	public static double compareBagOfWords_Count(Map<String, Integer> hist1, Map<String, Integer> hist2) 
	{
		Set<String> u = new TreeSet<>(hist1.keySet());
		u.addAll(hist2.keySet());
		
		double low = 0, hi = 0;
		
		for (String key: u) 
		{
			Integer ak = hist1.get(key);
			if(ak == null) ak = 0;
			
			Integer bk = hist2.get(key);
			if(bk == null) bk = 0;
			
			hi += Math.min(ak, bk);
			low += Math.max(ak, bk);
		}
		
		return hi/(low==0 ? 1 : low);
	}
	public static double calculateStringSimilarity(String str1, String str2) {
		str1 = NLPEngine.textPreProcessing(str1);
		str2 = NLPEngine.textPreProcessing(str2);
		return NLPEngine.compareBagOfWords_Count(NLPEngine.toBagOfWord_Count(str1), NLPEngine.toBagOfWord_Count(str2));
	}
	
	public static double TF(String doc, String term) {
		double res = 0;
		String[] docs = doc.split("\\s+");
        for (String word : docs) 
            if (term.equalsIgnoreCase(word)) res++;
        return res / doc.length();
	}
	public static double IDF(List<String> corpus, String term) {
        double n = 0;
        for (String doc : corpus) {
        	String[] words = doc.split("\\s+");
            for (String word : words) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(corpus.size() / n);
    }
	public static double TF_IDF(String doc, List<String> corpus, String term) {
		return NLPEngine.TF(doc, term) * NLPEngine.IDF(corpus, term);
	}
	public static Set<String> getRelevantTerm_TFIDF(List<String> corpus, int term_per_doc) {
		if (term_per_doc < 1) term_per_doc = 10;
		Set<String> terms = new HashSet<>();
		for (String doc:corpus) {
			String[] words = doc.split("\\s+");
			Map<String, Double> word_score = new HashMap<>();
			for (String word:words) {
				double score = NLPEngine.TF_IDF(doc, corpus, word);
				word_score.put(word, new Double(score));
			}
			Set<String> top_score_term = NLPEngine.get_top_score_term(word_score,term_per_doc);
			terms.addAll(top_score_term);
		}
		return terms;
	}
	
	private static Set<String> get_top_score_term(Map<String, Double> word_score, int term_per_doc) {
		if (term_per_doc <= word_score.keySet().size()) return word_score.keySet();
		Set<String> top = new HashSet<>();
		for (String word:word_score.keySet()) {
			if (top.size() < term_per_doc) top.add(word);
			else add_if_bigger(top,word_score,word);
		}
		return null;
	}
	private static void add_if_bigger(Set<String> top, Map<String, Double> word_score, String word) {
		double min = Double.MAX_VALUE;
		for (String term:top) if (word_score.get(term).doubleValue() < min) min = word_score.get(term).doubleValue();
		if (word_score.get(word).doubleValue() > min) {
			removeMinTerm(top,word_score);
			top.add(word);
		}
	}
	private static void removeMinTerm(Set<String> top, Map<String, Double> word_score) {
		double min = Double.MAX_VALUE;
		String min_term = null;
		for (String term:top) 
			if (word_score.get(term).doubleValue() < min) {
				min = word_score.get(term).doubleValue();
				min_term = term;
			}
		top.remove(min_term);
	}
	public static void main(String[] args) {
		String input = "tHi!s@,$ %i^s* a* (C)o_m+p;l'i,c.a/t\\e]d[   Sentence";
		System.out.println(textPreProcessing(input));
		
		String testMap = "this is a string and this is another string";
		Map<String, Double> m = toBagOfWord_Ratio(testMap);
		Map<String, Integer> m2 = toBagOfNgrams_Count(testMap, 2);
		for (String word:m.keySet()) System.out.println("Word: " + word + ", ratio: " + m.get(word));
		for (String phrase:m2.keySet()) System.out.println("Phrase: " + phrase + ", count: " + m2.get(phrase));
		
		String testMap2 = "this is a table and this is another table";
		System.out.println("Compare BOW " + compareBagOfWords_Count(toBagOfWord_Count(testMap), toBagOfWord_Count(testMap2)));
		System.out.println("Compare BO 2 grams " + compareBagOfWords_Count(toBagOfNgrams_Count(testMap,2), toBagOfNgrams_Count(testMap2,2)));
		

		Set<String> stopwords = NLPEngine_English.getStopWords();
		for (String word:stopwords) System.out.print(word +" - ");
	}
	
}
