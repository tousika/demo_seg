package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by dongsijia on 2018/11/8.
 */
public class CrossAmbiguityRecognition implements Recognition {

    private static final long serialVersionUID = -4788147739577569345L;

    private static final Set<String> suffixSet = new HashSet<>();

    static {
    }
    @Override
    public void recognition(Result result) {
        List<Term> list = result.getTerms();
        Iterator<Term> iterator = list.iterator();
        while (iterator.hasNext()) {
            Term term = iterator.next();
            if (filter(term)) {
                iterator.next();
                iterator.remove();
            }
        }
    }
    private boolean filter(Term temp){
        Term to = temp.to();
        Term toMax = null;
        String ns = "";
        if(temp==null) {return false;}
        if ("ns".equals(temp.getNatureStr()) && to!=null && suffixSet.contains(to.getName())){
            toMax = to.to();
            temp.setName(temp.getName()+to.getName());
            temp.setRealName(temp.getName());
            temp.setNature(TermNatures.NS.nature);
            temp.setTo(toMax);
            return true;
        }
        return false;
    }

}
