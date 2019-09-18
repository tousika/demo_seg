package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by dongsijia on 2018/11/8.
 */
public class LocationRecognition implements Recognition {

    private static final long serialVersionUID = 2362166800760569419L;
    private static final Set<String> suffixSet = new HashSet<>();
    private static final Set<String> quantitySet = new HashSet<>();

    static {
        suffixSet.add("省");
        suffixSet.add("市");
        suffixSet.add("县");
        suffixSet.add("区");
        suffixSet.add("镇");
        suffixSet.add("街");
        suffixSet.add("街道");
        suffixSet.add("路");
        suffixSet.add("路口");
        suffixSet.add("站");
        suffixSet.add("郡");
        suffixSet.add("府");
        suffixSet.add("港");
//        quantitySet.add("点");
    }
    @Override
    public void recognition(Result result) {
        List<Term> terms = result.getTerms();
        List<Term> list = new LinkedList<Term>();
        int end=0;
        for (int i = 0; i < terms.size(); i++) {
            Term termBase = terms.get(i);
            if(merge(termBase) && termBase.getOffe()>=end){
                list.add(termBase);
                end = termBase.getOffe() + termBase.getRealName().length();
            }else if(termBase.getOffe()>=end){
                //如果清洗词典删掉北京东城区暂时不需要补全：北京东城区，北京，东城区
                list.add(termBase);
            }
        }
        result.setTerms(list);
    }
    private boolean merge(Term temp){
        if(temp==null) {return false;}
        Term to = temp.to();
        String ns = "";
        if(to!=null && suffixSet.contains(to.getName()) && !"w".equals(temp.getNatureStr())){//正常情况：黄海+路
//            if(to.to()==null || to.to().getRealName().length()>1){//黄海,路,这,条街
                temp.merageWithBlank(to);
                temp.setNature(TermNatures.NS.nature);
                return true;
//            }
        }else if("ns".equals(temp.getNatureStr()) || "4".equals(temp.getNatureStr())){//特殊情况:北京东城区,北京东城(去掉)
            //suffixSet.contains(temp.getName().substring(temp.getName().length()-1))
            return true;
        }
        return false;
    }

}
