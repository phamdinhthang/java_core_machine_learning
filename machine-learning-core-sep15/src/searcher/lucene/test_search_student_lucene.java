package searcher.lucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class test_search_student_lucene {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		List<Student> students = new ArrayList<>();
		for (int i=0;i<100000;i++) students.add(new Student(true));
		
		LuceneIndexer indexer = new LuceneIndexer();
		indexer.index(students);
  		
  		Query name1 = LuceneSearcher.createTextQuery("binh", "name");
  		Query name2 = LuceneSearcher.createTextQuery("giang", "name");
  		Query birth = LuceneSearcher.createTextQuery("hanoi", "birth");
  		Query score1 = DoublePoint.newRangeQuery("score", 6.0, 11.0);
  		Query score2 = DoublePoint.newRangeQuery("score", 0.0, 5.0);
  		Query age1 = IntPoint.newRangeQuery("age", 10, 20);
  		Query age2 = IntPoint.newRangeQuery("age", 0, 8);
  		
  		BooleanQuery name = new BooleanQuery.Builder().add(name1, Occur.SHOULD).add(name2, Occur.SHOULD).build();
  		BooleanQuery score = new BooleanQuery.Builder().add(score1, Occur.SHOULD).add(score2, Occur.SHOULD).build();
  		BooleanQuery age = new BooleanQuery.Builder().add(age1, Occur.SHOULD).add(age2, Occur.SHOULD).build();
  		BooleanQuery sumup = new BooleanQuery.Builder().add(name, Occur.MUST)
  				.add(score, Occur.MUST)
  				.add(age, Occur.MUST)
  				.add(birth, Occur.MUST_NOT).build();
  		List<Document> hits = LuceneSearcher.search(sumup, Integer.MAX_VALUE);
  		System.out.println("Lucene search: Found: " + hits.size() + " results");
  		
  		linearSearch(students);
  		
		String query = "(name:binh OR name:giang) AND (score:[6.0 TO 11.0] OR score:[0.0 TO 5.0] AND (age:[10 TO 20] OR age:[0 TO 8]) NOT (birth:hanoi))";
		String[] searchInFields = {"name","birth","age"};
  		List<Document> foundDocs = LuceneSearcher.searchMultipleTextField(query, Integer.MAX_VALUE, searchInFields);
  		System.out.println("Lucene search: Found: " + foundDocs.size() + " results");
	}
	

	private static void linearSearch(List<Student> students) {
		List<Student> filtered = new ArrayList<>();
		for (Student s:students) 
			if ((s.name.toLowerCase().contains("binh") || s.name.toLowerCase().contains("giang"))
					&& !s.birth.toLowerCase().contains("hanoi")
					&& (s.score > 6.0 || s.score < 5.0)
					&& (s.age > 10 || s.age < 8)) filtered.add(s);
		System.out.println("Linear search: Found: " + filtered.size() + " results");
	}
}
