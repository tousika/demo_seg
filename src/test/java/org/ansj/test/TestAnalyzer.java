package org.ansj.test;

import org.ansj.library.StopLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * Created by dongsijia on 2018/9/7.
 */
public class TestAnalyzer {

    public static void main(String[] args) {
        StopRecognition re = StopLibrary.get();
        System.out.println(ToAnalysis.parse("长春电影节").recognition(re));
        System.out.println(IndexAnalysis.parse("长春电影节").recognition(re));
        System.out.println(ToAnalysis.parse("中美智库对话").recognition(re));
        System.out.println(IndexAnalysis.parse("中美智库对话").recognition(re));
        System.out.println(IndexAnalysis.parse("上海南京东路战略招标").recognition(re));


    }
}
