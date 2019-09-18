package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.ansj.util.TermUtil;

import java.util.*;

/**
 * Created by dongsijia on 2019/04/22.
 * 基于充分相信自定义词典（nr:识别出的人名, 3:自定义人名），过滤掉重复字产生的歧义人名
 * 需要考虑term的偏移量，避免消歧过度
 * ex:张成龙参加(消除歧义名成龙)，张成龙成龙参加(应切成张成龙和成龙)
 */
public class NameAmbiguityRecognition implements Recognition {

    private static final long serialVersionUID = 4922144046022997776L;

    @Override
    public void recognition(Result result) {
        Iterator<Term> iterator = result.getTerms().iterator();
        int offe = -1;
        int end = 0;
        while(iterator.hasNext()){
            Term t = iterator.next();
            if("null".equals(t.getNatureStr())){//文中\t\n等造成的影响
                iterator.remove();
            }
            if(t.getOffe()==offe ||
                    (t.getOffe()+t.getRealName().length()<=end)){
                iterator.remove();
                continue;
            }
            if("3".equals(t.getNatureStr())||("nr".equals(t.getNatureStr())&&t.getRealName().length()<=3)){
                //记录term偏移量避免过度消歧
                offe = t.getOffe();
                end = t.getOffe()+t.getRealName().length();
            }

        }
    }
}
