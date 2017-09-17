package searcher.lucene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;

public class Student implements LuceneDocument {
	public String name;
	public String birth;
	public int age;
	public double score;
	public Student(boolean random) {
		if (random) {
			List<String> names = Arrays.asList("An","Binh","Cuong","Duc","Giang","Hoang","Khanh");
			List<String> births = Arrays.asList("Hanoi","Hue","Danang","Saigon","Nhatrang","Phuquoc");
			List<Integer> ages = new ArrayList<>();
			for (int i=6;i<18;i++) ages.add(new Integer(i));
			List<Double> scores = new ArrayList<>();
			for (double i=4.0;i<10.0;i=i+0.1) scores.add(new Double(i));
			
			Random r = new Random();
			this.name = names.get(r.nextInt(names.size()));
			this.age = ages.get(r.nextInt(ages.size()));
			this.score = scores.get(r.nextInt(scores.size()));
			this.birth = births.get(r.nextInt(births.size()));
		}
	}
	@Override
	public Document toDocument() {
		Document document = new Document();
    	document.add(new TextField("name", this.name.toLowerCase(), Field.Store.YES));
    	document.add(new TextField("birth", this.birth.toLowerCase(), Field.Store.YES));
    	document.add(new IntPoint("age", this.age));
    	document.add(new DoublePoint("score", this.score));
    	return document;
	}
}
