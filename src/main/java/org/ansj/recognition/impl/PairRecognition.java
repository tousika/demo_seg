package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;
import org.nlpcn.commons.lang.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 成对出现的，如书名号、单双引号、大小括号等等
 * Created by XYUU <xyuu@xyuu.net> on 2018/7/14.
 */
public class PairRecognition implements Recognition {

    private static final long serialVersionUID = 1L;

    private Nature nature;

    private Map<String, String> ruleMap;

    public PairRecognition(String natureStr, Map<String, String> ruleMap) {
        this.nature = new Nature(natureStr);
        this.ruleMap = ruleMap;
    }

    @Override
    public void recognition(Result result) {
        List<Term> terms = result.getTerms();
        String end = null;
        String name;

        LinkedList<Term> mergeList = null;
        Integer wordCnt = 0;//配对符号中的字数，三个字以内认为是专属词
        List<Term> list = new LinkedList<Term>();
        List<String> biaodianList = new ArrayList<String>(){{add("。");add("，");add("；");add("!");}};
        for (Term term : terms) {
            name = term.getName();
            if (end == null) {
                if ((end = ruleMap.get(name)) != null) {
                    mergeList = new LinkedList<Term>();
                    wordCnt = 0;
//                    mergeList.add(term);
//                    list.add(term);
                } else {
//                    list.add(term);
                }
                list.add(term);
            } else {
                mergeList.add(term);
                wordCnt+=term.getName().length();
                if (end.equals(name)) {
                    Term rightTag = mergeList.pollLast();
                    if (mergeList.size()>0) {
                        if ((wordCnt - name.length()) <= 4) {//!"w".equals(term.to().getNatureStr())){//wordCnt长度要减去右边的配对符合
                            Term ft = mergeList.pollFirst();
                            for (Term sub : mergeList) {
                                ft.merage(sub);
                            }
                            ft.setNature(nature);
                            list.add(ft);

                        } else {
                            if ((wordCnt - name.length()) <= 20) {//!"w".equals(term.to().getNatureStr())){
                                mergeList = merge(mergeList, biaodianList, wordCnt - name.length());
                            }
                            for (Term sub : mergeList) {
                                list.add(sub);
                            }
                        }
                    }
                    list.add(rightTag);
//                    System.out.println(mergeList);
                    mergeList = null;
                    end = null;
                }
            }
        }

        if (mergeList != null) {
            list.addAll(mergeList);
        }
        result.setTerms(list);
    }

    private LinkedList<Term> merge(LinkedList<Term> mergeList,List<String> biaodianList,int nameSize){
        LinkedList<Term> result = new LinkedList<Term>();
        Term ft = mergeList.pollFirst();
        String ft_name_old = ft.getName();
        List<String> natureSentence = new ArrayList<>();
        if (StringUtil.isNotBlank(ft.getNatureStr())) {
            natureSentence.add(ft.getNatureStr().substring(0, 1));
            natureSentence.add("#");
        }
        boolean isOne = false;
        int index = mergeList.size();
        for (Term sub : mergeList) {
            index--;
            if (!isOne && natureSentence.size()>0 &&
                    ("w".equals(sub.getNatureStr())
                            && !biaodianList.contains(sub.getName())
                            && (index>0))){//保证标点不是最后一个，"我很好啊！"
                isOne = true;//作为整句
//                if (nameSize>20){todo
//                    ft.setNature(nature);
//                    result.add(ft);
//                    result.add(sub);
//                    ft = null;
//                    continue;
//                }

            }else if (biaodianList.contains(sub.getName())){
                ft.setName(ft_name_old);
                ft.setRealName(ft_name_old);
                if (natureSentence.size()>0) ft.setNature(new Nature(natureSentence.get(0)));
                mergeList.add(0,ft);
                return mergeList;
            }
            if (ft ==null) {
                ft =sub;
            }else{
                ft.merage(sub);
            }
            if (StringUtil.isNotBlank(sub.getNatureStr())) {
                natureSentence.add(sub.getNatureStr().substring(0, 1));
                natureSentence.add("#");
            }
        }
        ft.setNature(nature);
        if (isOne){
//            return new LinkedList<Term>(){{add(ft);}};
            result.add(ft);
            return result;
        }else{
//            Pattern p = Pattern.compile("r#.*v#.*r#|r#.*v#.*n|n#.*v#.*n|n#.*v#.*r|n#(?!.*v#)");//?!不匹配
            Pattern p = Pattern.compile("r#.*v#.*r#|r#.*v#.*n|n#.*v#.*n|n#.*v#.*r");
//            System.out.println(String.join("", natureSentence));
            Matcher m = p.matcher(String.join("", natureSentence));
            if (m.find()){
                ft.setName(ft_name_old);
                ft.setRealName(ft_name_old);
                if (natureSentence.size()>0) ft.setNature(new Nature(natureSentence.get(0)));
                mergeList.add(0,ft);
                return mergeList;
            }else{
//                return new LinkedList<Term>(){{add(ft);}};
                result.add(ft);
                return result;
            }
        }
    }
}
