package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则识别抽取
 * 
 * @author dongsijia
 * 
 */
public class RegexRecognition implements Recognition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Nature nature = new Nature("t");

	@Override
	public void recognition(Result result) {
		String name = "";
		String timeWord = "";
		List<Term> terms = result.getTerms();
		LinkedList<Term> mergeList = new LinkedList<Term>();
		List<Term> list = new LinkedList<Term>();

		Pattern pattern = Pattern
				.compile(
						"(当前|本地|当地)时间|第?([0123456789]{1,3}(届|轮|任|场|局|季|节|公斤级?))|第?([一二三四五六七八九十]{1,2}(届|轮|任|场|局|季|节|公斤级?))|((\\d|[０１２３４５６７８９]){1,2})(/|至|到)((\\d|[０１２３４５６７８９]){1,2})|((\\d|[０１２３４５６７８９]){1,3})(-)((\\d|[０１２３４５６７８９]){1,3})",
						Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		int index = 10;//matcher.groups总数,pattern变了总数也要变
		for (int i = 0; i < terms.size(); i++) {
			if("\r".equals(terms.get(i).getName())
					|| "\n".equals(terms.get(i).getName())){
				continue;
			}
			boolean isTime = false;
			Term termBase = terms.get(i);
			int timeTermsLength = 1;
			int matchLength = 0; //匹配长度
			for (int j = i; j < terms.size() && matchLength < 5; j++) { //向后最大找14个词匹配是否是时间词
				Term term = terms.get(j);
				name = term.getName();
				timeWord += name;
				Matcher matcher = pattern.matcher(timeWord);
				mergeList.add(term);
				if (matcher.matches()) {
					isTime = true;
					timeTermsLength += (j - i);
//					i = j;
					if(matcher.group(2)!=null //31届
							|| matcher.group(4)!=null){
						//i nothing
					}else if(matcher.group(6)!=null && matcher.group(9)!=null){
						//i nothing
//						if(matcher.group(6).compareTo("50")<=0){ //目前不考虑时间变体18/19 2018 2019
//							list.add(new Term("20"+matcher.group(6),terms.get(j).getOffe(), TermNatures.M_ALB));
//							list.add(new Term("20"+matcher.group(9),terms.get(j).getOffe(), TermNatures.M_ALB));
//						}else{
//							list.add(new Term("19"+matcher.group(6),terms.get(j).getOffe(), TermNatures.M_ALB));
//							list.add(new Term("19"+matcher.group(9),terms.get(j).getOffe(), TermNatures.M_ALB));
//						}
						list.add(new Term(matcher.group(6),terms.get(i).getOffe(), TermNatures.M_ALB));
					}else{
						i = j;
					}
				}
				matchLength++;
			}		
			if (isTime) {
				Term ft = mergeList.pollFirst();
				for (int k = 0; k < timeTermsLength - 1; k++) {
					ft.merageWithBlank(mergeList.get(k));
				}
				ft.setNature(nature);
				list.add(ft);
			} else {
				list.add(termBase);
			}
			mergeList.clear();
			timeWord = "";

		}
		result.setTerms(list);
	}
	


}
