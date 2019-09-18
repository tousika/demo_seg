package org.ansj.recognition.impl;

import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeRecognitionTest {
	
	@Test
	public void test() {
		Pattern pattern = Pattern
				.compile(
						"(当前|本地|当地)时间|第?([0123456789]{1,3}(届|轮|任|场|局|季|节|公斤级?))|第?([一二三四五六七八九十]{1,3}(届|轮|任|场|局|季|节|公斤级?))|(\\d|[０１２３４５６７８９]){2,4}(-|/|至|到)(\\d|[０１２３４５６７８９]){2,4}",
						Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		System.out.println("05,50:"+("05".compareTo("50")));
		System.out.println("08,50:"+("08".compareTo("50")));
		System.out.println("10,50:"+("10".compareTo("50")));
		System.out.println("74,50:"+("74".compareTo("50")));
		List<String> list =new ArrayList<>();
		list.add( "当地时间");
		list.add( "第66场222");
		list.add( "66场");
		list.add( "第六十六场");
		list.add( "六十六场");
		list.add( "第六十六公斤级");
		list.add( "第六十六公斤");
		list.add( "六十六公斤级");
		list.add( "六十六公斤");
		System.out.println(pattern.pattern());
		Matcher matcher;
		for (String s:list){
			matcher=pattern.matcher(s);
			if(matcher.matches()){
				System.out.println("groupCount:"+matcher.groupCount());
				System.out.println("group:"+matcher.group());
				System.out.println("group1:"+matcher.group(1));
				System.out.println("group2:"+matcher.group(2));
				System.out.println("group3:"+matcher.group(3));
				System.out.println("group4:"+matcher.group(4));
				System.out.println("group5:"+matcher.group(5));
				System.out.println("----------");
			}else{
				System.out.println("xxx:"+matcher.groupCount());
			}
		}
		TimeRecognition timeRecognition = new TimeRecognition() ;
		//		System.out.println(IndexAnalysis.parse("dic_editorial",s).recognition(timeRecognition));



		
	}


}
