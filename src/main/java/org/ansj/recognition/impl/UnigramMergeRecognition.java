package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.ansj.util.TermUtil;

import java.util.*;

/**
 * Created by dongsijia on 2018/11/8.
 */
public class UnigramMergeRecognition implements Recognition {

    private static final long serialVersionUID = 6804487354300627672L;
    private static final Nature PHONE_NATURE = new Nature("phone");
    private static final Map<String,String> nsGroup = new HashMap<>();
    static {
        nsGroup.put("v|n","vn");
        nsGroup.put("n|n","nw");
        nsGroup.put("m|v","v");
//        nsGroup.put("nz|n","nz");
    }
    @Override
    public void recognition(Result result) {
        List<Term> list = result.getTerms();
        Iterator<Term> iterator = list.iterator();

        while (iterator.hasNext()) {
            Term term = iterator.next();
            if (merge(term)) {
                iterator.next();
                iterator.remove();
            }
        }

    }
    private boolean merge(Term temp) {
        Term to = temp.to();
        Term toMax = null;
        String ns = "";
        if(temp==null) {return false;}
        if (temp.getName().length()>1 || to==null || to.getName().length()>1){
            return false;
        }
        ns = String.join("|",temp.getNatureStr(), to.getNatureStr());
        if(nsGroup.containsKey(ns)){
            toMax = to.to();
            if (toMax != null && toMax.getName().length()==1 && "n".equals(toMax.getNatureStr())) {return false;}
            temp.setName(temp.getName()+to.getName());
            temp.setRealName(temp.getName()+to.getName());
            temp.setNature(new Nature(nsGroup.get(ns)));
            temp.setTo(toMax);
//            TermUtil.termLink(temp, toMax);
            return true;
        }
        return false;
    }
    public void recognition2(Result result) {
        List<Term> terms = new ArrayList<>();
//        Term term = null;
        Term to = null;
        Term temp = null;
        String ns = "";
        int size = result.getTerms().size();
        for (int i = 0; i < size; i++) {
            temp = result.getTerms().get(i);
            if(temp==null){
//                terms.add(temp);
                continue;
            }
            if (temp.getName().length()>1 || result.getTerms().get(i+1).getName().length()>1){
                continue;
            }
            ns = temp.getNatureStr()+result.getTerms().get(i+1).getNatureStr();
            if(nsGroup.containsKey(ns)){
                //                    if (i+2<size) {
                //                        //合并
                //                        if (terms.get(i+2).getName().length()==1 && "n".equals(terms.get(i+2).getNatureStr())) continue;
                //                        wordMerge();
                //                    }else {
                //                        wordMerge();
                //                    }
                if(i+2>=size //没有后缀单字
                        || result.getTerms().get(i+2).getName().length()>1 //后面可成词
                        || !"n".equals(result.getTerms().get(i+2).getNatureStr())) {
                    to = temp.to() ;
                    temp.setName(temp.getName()+to.getName());
                    terms.remove(to.getOffe());
                    //            terms.get(to.getOffe()) = null ;
                    TermUtil.termLink(temp, to.to());
                    to = to.to() ;
                    if(temp.getName().length()>1){
                        i-- ;
                        terms.add(temp);
                        continue;
                    }
                }
            }
//            if (!"v".equals(term.getNatureStr()) || term.getName().length()>1 || term.next()!=null){
//                continue;
//            }
//            if (!"v".equals(terms.get(i+1).getNatureStr()) || terms.get(i+1).getName().length()>1 || terms.get(i+1).next()!=null){
//                continue;
//            }

        }
    }
    private void wordMerge(){

    }
//    private Term getMaxTerm(Term maxTerm) {
////        Term maxTerm = terms.get(0);
//        if (maxTerm == null) {
//            return null;
//        }
//        Term term = maxTerm;
//        while ((term = term.next()) != null) {
//            maxTerm = term;
//        }
//        return maxTerm;
//    }

}
