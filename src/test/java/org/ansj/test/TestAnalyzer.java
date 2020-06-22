package org.ansj.test;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.library.StopLibrary;
import org.ansj.recognition.impl.*;
import org.ansj.splitWord.analysis.*;
import org.ansj.util.TermUtil;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.ObjConver;

import java.util.*;

/**
 * Created by dongsijia on 2018/9/7.
 */
public class TestAnalyzer {

    public static void main(String[] args) {
        StopRecognition re = StopLibrary.get();
        UnigramMergeRecognition unir = new UnigramMergeRecognition();
//        System.out.println(ToAnalysis.parse(segment).recognition(re));
        String segment="中国证券";
//        segment="缅甸赛范林丹出席开幕式";
        Result result =ToAnalysis.parse("dic_editorial",segment);
        System.out.println(result);
        System.out.println(IndexAnalysis.parse("dic_editorial",segment));
        System.out.println(result.recognition(unir));
        System.out.println(result.recognition(re));
        System.exit(0);
        System.out.println(result.recognition(new NameAmbiguityRecognition()).toStringWithOutNature());
        result =IndexAnalysis.parse("dic_editorial",segment);
        System.out.println(result.recognition(re).toStringWithOutNature());

        System.out.println("------------");
//        segment="游云谷";
//        result =IndexAnalysis.parse("dic_editoral",segment);
        System.out.println(IndexAnalysis.parse("dic_editoral","游云谷和我是朋友").recognition(re).toStringWithOutNature());
        System.out.println(IndexAnalysis.parse("dic_editoral","去游云谷家玩").recognition(re).toStringWithOutNature());
        System.out.println("---------");

        Forest forest1 = DicLibrary.get("dic_editorial");
        GetWord word = forest1.getWord(segment);
        String temp=null;
        int min=0,max=-1;
        Map<Integer, Term> map = new HashMap<>();
        while ((temp = word.getAllWords()) != null) {
            if (word.offe==min || word.offe>max){
                map.put(word.offe, new Term(temp, word.offe, word.getParam(0), ObjConver.getIntValue(word.getParam(1))));
                min = word.offe;
                max = word.offe+temp.length()-1;
            }

        }
        Collection<Term> valueCollection = map.values();
        List<Term> valueList = new ArrayList<Term>(valueCollection);
        Collections.sort(valueList, new Comparator<Term>() {

            @Override
            public int compare(Term o1, Term o2) {
                if (o1.getOffe() == o2.getOffe()) {
                    return o2.getName().length() - o1.getName().length();
                } else {
                    return o1.getOffe() - o2.getOffe();
                }
            }
        });
        System.out.println(valueList.toString());

//        ToAnalysis d1=new ToAnalysis(true);
//        DicAnalysis d3=new DicAnalysis(true);
//        DicAnalysis d4=new DicAnalysis();
//        System.out.println(ToAnalysis.parse("dic_e","C罗射门").recognition(re));
//        System.out.println(ToAnalysis.parse("dic_c","C罗射门").recognition(re));
//        System.out.println(ToAnalysis.parse("dic","C罗射门").recognition(re));

//        DicLibrary.insert(DicLibrary.DEFAULT,"c罗","nr",1000);
//        System.out.println(ToAnalysis.parse("dic","C罗射门."));
//        DicLibrary.insert(DicLibrary.DEFAULT,"c罗","3",1000);
//        System.out.println(ToAnalysis.parse("dic","C罗射门."));
//        DicLibrary.delete(DicLibrary.DEFAULT,"c罗");
//        System.out.println(ToAnalysis.parse("dic","C罗射门."));

        GetWord getWord=DicLibrary.get(DicLibrary.DEFAULT).getWord("C罗");

        Forest forest = DicLibrary.get(DicLibrary.DEFAULT);
        SmartForest<String[]> branch = forest;
        char[] chars = "c罗".toCharArray();
        for (int i = 0; i < chars.length; i++) {
            branch = branch.getBranch(chars[i]);
            if(branch==null)break;
        }
        DicLibrary.insert(DicLibrary.DEFAULT,"c罗","3",1000);
        forest = DicLibrary.get(DicLibrary.DEFAULT);
         branch = forest;
         chars = "c罗".toCharArray();
        for (int i = 0; i < chars.length; i++) {
            branch = branch.getBranch(chars[i]);
            if(branch==null)break;
        }



        //        String str = "不三不四，您好！欢迎使用ansj_seg,深圳有没有城中村这里有宽带吗?(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
//        // 增加新词,中间按照'\t'隔开
////        DicLibrary.insert(DicLibrary.DEFAULT_NATURE,"城中村", "userDefine", 1000);
//        DicLibrary.insert(DicLibrary.DEFAULT,"城中村", "userDefine", 1000);
//        //自定义词汇、自定义词性
//        DicLibrary.insert(DicLibrary.DEFAULT_NATURE,"ansj中文分词", "userDefine", 1001);
//        System.out.println("增加自定义词库:" + terms.toString());
//        // 删除词语,只能删除.用户自定义的词典.
//        DicLibrary.remove("ansj中文分词");
//        DicLibrary.remove("城中村");
//        terms = ToAnalysis.parse(str);
//        System.out.println("删除自定义词库:" + terms.toString());
//        // 歧义词
//        Value value = new Value("济南下车", "济南", "n", "下车", "v");
//        System.out.println(ToAnalysis.parse("我经济南下车到广州.中国经济南下势头迅猛!"));
//        Library.insertWord(AmbiguityLibrary.get(), value);
//        System.out.println(ToAnalysis.parse("我经济南下车到广州.中国经济南下势头迅猛!"));
//        value = new Value("南京东路", "南京", "ns", "东路", "n");
//        Library.insertWord(AmbiguityLibrary.get(), value);
//        System.out.println(ToAnalysis.parse("我经济南下车到广州.中国经济南下势头迅猛!"));
//        System.out.println(ToAnalysis.parse("上海南京东路战略招标").recognition(re));
//        System.out.println(IndexAnalysis.parse("上海南京东路战略招标").recognition(re));
//        System.out.println("------------------------------");
//        // 多用户词典
//        String str1 = "神探夏洛克这部电影作者.是一个dota迷";
//        System.out.println(ToAnalysis.parse(str1));
//        // 两个词汇 神探夏洛克 douta迷
//        Forest dic1 = new Forest();
//        Library.insertWord(dic1, new Value("神探夏洛克", "define", "1000"));
//        Forest dic2 = new Forest();
//        Library.insertWord(dic2, new Value("dota迷", "define", "1000"));
//        System.out.println(ToAnalysis.parse(str1, dic1, dic2));




    }
}
