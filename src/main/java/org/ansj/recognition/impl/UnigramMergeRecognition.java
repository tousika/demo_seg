package org.ansj.recognition.impl;

import org.ansj.domain.*;
import org.ansj.recognition.Recognition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dongsijia on 2019/5/6.
 */
public class UnigramMergeRecognition implements Recognition {

    private static final long serialVersionUID = 6804487354300627672L;
    private static final Map<String,String> nsGroup = new HashMap<>();
    private static final Map<String,String> mqGroup = new HashMap<>();
    static {
        nsGroup.put("v|n","vn1");
        nsGroup.put("vn|n","vn1");
        nsGroup.put("n|n","nw1");
        nsGroup.put("ns|ng","nw1");
        nsGroup.put("b|n","nw1");
        nsGroup.put("m|v","v1");
        mqGroup.put("en|m","mq");//g20,iphone5
        mqGroup.put("m|en","mq");//4G,320Li
        //        nsGroup.put("nz|n","nz");
    }
    @Override
    public void recognition(Result result) {
        wordMerge(result);


    }
    private static boolean merge(Term temp) {
        Term to = temp.to();
        Term toMax = null;
        //        String ns = "";
        if(temp==null || to==null) {return false;}
        String ns = String.join("|",temp.getNatureStr(), to.getNatureStr());
        if(nsGroup.containsKey(ns)){
            toMax = to.to();
            if (temp.getName().length()>1
                    || to.getName().length()>1
                    || (toMax != null && toMax.getName().length()==1 && "n".equals(toMax.getNatureStr()))) {return false;}
            temp.merageWithBlank(to);
            temp.setNature(new Nature(nsGroup.get(ns)));
            return true;
        }else if(mqGroup.containsKey(ns)
                && to.getOffe()==temp.getOffe()+temp.getRealName().length()){//原文里不含空格
            if ("m".equals(temp.getNatureStr())){//m|en
                temp.merageWithBlank(to);
                temp.setNature(new Nature(mqGroup.get(ns)));
            }else if(temp.getRealName().length()<3){//en|m
                temp.merageWithBlank(to);
                temp.setNature(new Nature(mqGroup.get(ns)));
            }else{//en|m
                temp.merageWithBlank(to);
                temp.setNature(new Nature(mqGroup.get(ns)));
            }
        }
        return false;
    }
    private static void wordMerge(Result result){
        Term to,toMax;
        Term tmp = null;
        List<Term> terms = result.getTerms();
        List<Term> list = new LinkedList<Term>();
        boolean isTrue;
        for (int i = 0; i < terms.size(); i++) {
            isTrue = false;
            if(terms.get(i)!=null && terms.get(i).to()!=null) {
                to = terms.get(i).to();
                toMax = to.to();
                String ns = String.join("|",terms.get(i).getNatureStr(), to.getNatureStr());
                if(nsGroup.containsKey(ns)){
                    if (terms.get(i).getName().length()<=2
                            && to.getName().length()<=2
                            && (terms.get(i).getName().length()+to.getName().length())<=3
                            && (toMax == null || toMax.getName().length()>1 || !"n".equals(toMax.getNatureStr()))) {
                        terms.get(i).merageWithBlank(to);
                        terms.get(i).setNature(new Nature(nsGroup.get(ns)));
                        isTrue = true;
                    }
                }else if(mqGroup.containsKey(ns)
                        && to.getOffe()==terms.get(i).getOffe()+terms.get(i).getRealName().length()){//原文里不含空格
                    //SNEC第十二届,考虑‘第’这样的词性也是mq，要进一步严格
                    boolean isNum = ns.indexOf("m")==0? isAllNum(terms.get(i).getName()):isAllNum(to.getName());
                    if (isNum){
                        if ("m".equals(terms.get(i).getNatureStr())
                                || ("en".equals(terms.get(i).getNatureStr()) && terms.get(i).getRealName().length()<3)){
                            terms.get(i).merageWithBlank(to);
                            terms.get(i).setNature(new Nature(mqGroup.get(ns)));
                            isTrue = true;
                        }else if("en".equals(terms.get(i).getNatureStr()) && terms.get(i).getRealName().length()>=3){//en|m
                            tmp = new Term(terms.get(i).getName(),terms.get(i).getOffe(), TermNatures.EN);//iphone,5->iphone5,iphone
                            terms.get(i).merageWithBlank(to);
                            terms.get(i).setNature(new Nature(mqGroup.get(ns)));
                            isTrue = true;
                        }
                    }
                }
            }
            list.add(terms.get(i));
            if(isTrue){
                i++;
                if (tmp !=null){
                    list.add(tmp);
                    tmp = null;
                }
            }
        }
        result.setTerms(list);
    }
    /**
     * 是否全是数字
     * @param str
     * @return
     */
    public static boolean isAllNum(String str) {
        if (str == null)
            return false;

        int i = 0;
        /** 判断开头是否是+-之类的符号 */
        if ("±+-＋－—".indexOf(str.charAt(0)) != -1)
            i++;
        /** 如果是全角的０１２３４５６７８９ 字符* */
        while (i < str.length() && "０１２３４５６７８９".indexOf(str.charAt(i)) != -1)
            i++;
        // Get middle delimiter such as .
        if (i > 0 && i < str.length())
        {
            char ch = str.charAt(i);
            if ("·∶:，,．.／/".indexOf(ch) != -1)
            {// 98．1％
                i++;
                while (i < str.length() && "０１２３４５６７８９".indexOf(str.charAt(i)) != -1)
                    i++;
            }
        }
        if (i >= str.length())
            return true;

        /** 如果是半角的0123456789字符* */
        while (i < str.length() && "0123456789".indexOf(str.charAt(i)) != -1)
            i++;
        // Get middle delimiter such as .
        if (i > 0 && i < str.length())
        {
            char ch = str.charAt(i);
            if (',' == ch || '.' == ch || '/' == ch  || ':' == ch || "∶·，．／".indexOf(ch) != -1)
            {// 98．1％
                i++;
                while (i < str.length() && "0123456789".indexOf(str.charAt(i)) != -1)
                    i++;
            }
        }

        if (i < str.length())
        {
            if ("百千万亿佰仟%％‰".indexOf(str.charAt(i)) != -1)
                i++;
        }
        if (i >= str.length())
            return true;

        return false;
    }
}
