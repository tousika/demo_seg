package org.ansj;

import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.ObjConver;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dongsijia on 2018/10/30.
 */
public class Test1 {

    public static void main(String[] args) {
        Forest forest1 = DicLibrary.get("dic_editorial");
        System.out.println("--");
//        String regex = "[>)》）]+";
//        String[] split = "李尔王>很好)打好》版本）".split(regex);
//        for (String s : split) {
//            System.out.println(s);
//        }
//        System.out.println("---");
//        String[] split1 = "李尔王".split(regex);
//        for (String s : split1) {
//            System.out.println(s);
//        }
//
//        System.exit(0);
//        System.out.println("车"+"车".hashCode());
//        System.out.println("两"+"两".hashCode());
//        System.out.println("成"+"成".hashCode());
//        System.out.println("动"+"动".hashCode());
//        System.out.println("得"+"得".hashCode());
//        System.out.println("名"+"名".hashCode());
}

    private int[] GetMaxAndSecondWord(String subStr, List<Term> terms){
        int[] nm = {0,0};
        int m=0,n=0;
        for(int i=1;i<=terms.size();i++){
            String tmp = terms.get(i).getName();
            if (!subStr.startsWith(terms.get(i).getName())) break;
            m = tmp.length()-1;
            n = tmp.length()-1;
//            j = terms.get(i).getName().length()-1>j?terms.get(i).getName().length()-1:j;
        }
        return new int[]{n,m};
    }
    private void CheckWord(int i, int nEnd, boolean moas){
        int j=0;
        for (int k = i + 1; k <= nEnd; k++) {
//            j = GetMaxWord(k);//提取t位置最长词
            if (j > nEnd) {//存在交集型歧义
                moas = true;
                nEnd = j;
            }
        }
    }
    /**
     * 字串AJB中，若AJ∈D、JB∈D、A∈D、B∈D ，则AJB为交集型歧义字段。
     * 此时，AJB有AJ/B、A/JB两种切分形式。其中J为交集字段
     * @param terms
     * @return 链长为1或2的交集型歧义
     */
    private void isCrossAmbiguity(List<Term> terms){
        //D为词典所以不用考虑单字情况
        if (terms.size()>=3){
            String maxWord = terms.get(0).getName();
            String collection_D = terms.get(0).getNatureStr().substring(0,1); //从属性层级调整精度
            if (maxWord.length()>=3 || maxWord.length()<=4){ //链长：长词-两端(2)，只考虑1或2的情况

            }
        }

    }
    private LinkedList<Term> moasDetection(List<Term> terms){
        LinkedList<Term> removeLast = new LinkedList<Term>();
        int nEnd=0, n=0, m=0;
        for(int i=0;i<terms.size();i++){
            String word = terms.get(i).getName();
            if (i==0){//初始化最长词，次长词
                //						n = .get(0).getName().length()-1;
                //						m = list.get(1).getName().length()-1;
                if (m>0) nEnd = m; // 次长词非单字
                else nEnd = n;
            }
        }
        if (terms.size()>=3){//初始化最长词三字以上
            //1.判断歧义类型是否为交集型
            //2.获取链长为1或者2的情况
            String collection_D = terms.get(0).getNatureStr();
            collection_D = collection_D.substring(0,1); //从属性层级调整精度
            for(Term t:terms){
                if(!t.getNatureStr().startsWith(collection_D)) terms.remove(t);
            }
            if(terms.size()>1){//进行链长获取

                getChainLength(terms);
            }
            String maxWord = terms.get(0).getName();
            String word = "";

            Map<Integer, List<String>> initWord = new HashMap<>(); // 初始化词条

            for(int i=1;i<terms.size();i++){
                word = terms.get(i).getName();
                if (word.length()==1) continue;

            }
            for(int i=0;i<maxWord.length();i++){

            }
        }
        //				if (term==null) return false; //暂不考虑单字
        //				String D = String.valueOf(term.getNatureStr().charAt(0));
        //				if (subTerm.getNatureStr())
        //				String word = map.get(wordOffe);
        return removeLast;
    }
    private int getChainLength(List<Term> terms){
        List<Term> words;
        boolean moas = false;
        int nEnd=0, m=0, n=0, j=0;
        String maxWord = terms.get(0).getName();
        n = maxWord.length()-1;
        String firstLetter = String.valueOf(maxWord.charAt(0));
        int k=0;
        for(int i=0;i<maxWord.length();i++){
            moas = false;
            k = i+1;

        }
        //				for(int i=1;i<=terms.size();i++){ //初始化最长词和次长词
        //					if (!terms.get(i).getName().startsWith(firstLetter)) break;
        //					m = terms.get(i).getName().length()-1>m?terms.get(i).getName().length()-1:m;
        ////					String letter = String.valueOf(terms.get(i).getName().charAt(0));
        ////					if (initWord.get(i)==null){
        ////						words = new ArrayList<>();
        ////					}else{
        ////						words = initWord.get(letter);
        ////					}
        ////					words.add(terms.get(i));
        ////					initWord.put(i, words);
        //				}
        //				nEnd = m>0?m:n; //>0是次长词非单字词
        //				k = 1;
        //				for(k=1;k<=maxWord.length();k++){ //查找过程最长词
        //					String subStr = maxWord.substring(k);
        //					j = -1;
        //					for(int i=1;i<=terms.size();i++){
        //						if (!subStr.startsWith(terms.get(i).getName())) break;
        //						j = terms.get(i).getName().length()-1>j?terms.get(i).getName().length()-1:j;
        //					}
        //					j = k+j;
        //					if (j>nEnd) {
        //						nEnd = j;
        //						moas = true;
        //					} else if(k<nEnd){//步骤e
        //						continue;
        //					} else if (nEnd<n){//步骤f
        //						nEnd = n;
        //						moas = false;
        //					} else if (nEnd>=n && moas){
        //						moas = true;
        //					}
        //				}
        //				//步骤e
        //				for(Map.Entry<Integer,List<Term>> e:initWord.entrySet()){
        //					Collections.sort(e.getValue(), new Comparator<Term>() {
        //						@Override public int compare(Term o1, Term o2) {
        //							return o2.getName().length() - o1.getName().length();
        //						}
        //					});
        //					System.out.println(e.getValue());
        //				}
        //				int chain =0;
        //
        //				for(Map.Entry<Integer,List<Term>> e:initWord.entrySet()){
        //					List<Term> list = e.getValue();
        //					if (maxWord.equals(list.get(0).getName())){// 获取初始化最长词和次长词
        //						if (list.size()==1) return 0; //TODO (考虑是否continue) 不存在初始化次长词
        //						n = list.get(0).getName().length()-1;
        //						m = list.get(1).getName().length()-1;
        //						if (m>0) nEnd = m; // 次长词非单字
        //						else nEnd = n;
        //					}else {
        //						j = maxWord.indexOf(list.get(0).getName())+list.get(0).getName().length()-1;
        //						if (j>nEnd){//存在歧义
        //							nEnd = j;
        //							chain+=1;
        //						}else{
        //
        //						}
        //					}
        //				}
        return 1;
    }
}
