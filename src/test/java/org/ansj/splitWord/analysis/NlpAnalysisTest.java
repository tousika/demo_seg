package org.ansj.splitWord.analysis;

import org.ansj.CorpusTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NlpAnalysisTest extends CorpusTest {

	@Test
	public void test() {
		for (String string : lines) {
			System.out.println(NlpAnalysis.parse(string));
		}
		List<String> sen= new ArrayList<>();
		sen.add("复旦人大连出手");
		sen.add("复旦人大连出手。");
		for(String s: sen){
			System.out.println(NlpAnalysis.parse(s));
		}
	}

}
