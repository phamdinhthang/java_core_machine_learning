package text.NLP;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.tartarus.snowball.ext.EnglishStemmer;

public class NLPEngine_English extends NLPEngine {
	public static Set<String> getStopWords() {
		CharArraySet stops = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
		Iterator<Object> iter = stops.iterator();
		Set<String> stopwords = new TreeSet<>();
		while(iter.hasNext()) {
		    char[] stopWord = (char[]) iter.next();
		    stopwords.add(new String (stopWord));
		}
		return stopwords;
	}
	public static String removeStopWords(String input) {
		CharArraySet stops = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
		Iterator<Object> iter = stops.iterator();
		Set<String> stopwords = new TreeSet<>();
		while(iter.hasNext()) {
		    char[] stopWord = (char[]) iter.next();
		    stopwords.add(new String (stopWord));
		}
		for (String word:stopwords) {
			String replaceRegEx = "\\s*\\b"+word+"\\b\\s*";
			input.replaceAll(replaceRegEx, "");
		}
		return input;
	}
	public static String stemWords(String input) {
		String[] words = input.split("\\s+");
		StringBuilder s = new StringBuilder();
		EnglishStemmer english = new EnglishStemmer();
		for(int i = 0; i < words.length; i++){
	        english.setCurrent(words[i]);
	        english.stem();
	        s.append(english.getCurrent() + " ");
	}
		return s.toString().trim();
	}
	public static void main(String[] args) {
		String txt = "i don't know differences between biology and biological and biologically";
		System.out.println("Original: " + txt);
		System.out.println("Stemmed: " + NLPEngine_English.stemWords(txt));
	}
}
