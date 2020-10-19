package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于规则的新词发现 jijiang feidiao dongsijia
 *
 * @author ansj
 */
public class BookRecognition implements Recognition {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Nature nature = new Nature("nz");

    private static Map<String, String> ruleMap = new HashMap<String, String>();

    static {
        ruleMap.put("《", "》");
        ruleMap.put("“", "”");//要对引号中判断是否是一句话
        ruleMap.put("‘", "’");
    }

    @Override
    public void recognition(Result result) {
        List<Term> terms = result.getTerms();
        String end = null;
        String name;
        List<Term> list = new LinkedList<Term>();
        int offe = 0;
//        String bookRightFlag = "[>)》）]+";

        for (Term term : terms) {
            name = term.getName();
            if (term.getOffe() >= offe) {
                if ((end = ruleMap.get(name)) != null) {

//                    Term book = term.to();
//                    book = new Term();TermNatures.M_ALB;
                    Term book = new Term(term.to().getName(), term.to().getOffe(), term.to().item());
                    book.setTo(term.to().to());
                    boolean tagPair = true;// 文章不规范，有左书名号但是没有右书名号的情况
                    while (!end.equals(book.to().getName())) {
                        if ("END".equals(book.to().getName())){
                            tagPair = false;
                            break;// 文章不规范，有左书名号但是没有右书名号的情况
                        }
                        book.merageWithBlank(book.to());
                    }
                    if (!tagPair && true) {
                        list.add(term);
                        continue;
                    }
//                    String[] split = book.getName().split(bookRightFlag);
                    book.setNature(nature);
                    book.setName(book.getName());
                    book.setRealName(book.getName());
                    list.add(term);
                    list.add(book);
                    offe = book.getOffe() + book.getName().length();
                } else {
                    list.add(term);
                }
            }
//            else {
//				mergeList.add(term);
//				if (end.equals(name)) {
//
//					Term ft = mergeList.pollFirst();
//					for (Term sub : mergeList) {
//						ft.merage(sub);
//					}
//					ft.setNature(nature);
//					list.add(ft);
//					mergeList = null;
//					end = null;
//				}
//            }
        }

//        if (mergeList != null) {
//            for (Term term : mergeList) {
//                list.add(term);
//            }
//        }

        result.setTerms(list);
    }

}
