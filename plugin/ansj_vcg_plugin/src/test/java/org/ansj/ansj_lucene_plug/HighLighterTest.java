package org.ansj.ansj_lucene_plug;

import org.ansj.vcg.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;

/**
 * Created by dongsijia on 2019/10/25.
 */
public class HighLighterTest {
    public static void main(String[] args) {
        try {
            String keyword = "中华人民共和国梅西 意大利 日本 fifa 小日本";
            String text = "中华人民共和国北京时间9月24日凌晨，FIFA国际足联日本颁奖典礼在意大利召开，似乎是因为率先得知了梅西将得到世界足球先生这一奖项，C罗在离得如此近的情况下，也没有给国际足联面子，继去年缺席后今年再次缺席FIFA国际足联颁奖典礼，最终梅西率先";
            Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.query_ansj);
//            Analyzer analyzer = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser( "content", analyzer);
            QueryScorer scorer = new QueryScorer(queryParser.parse(keyword), "content");
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");

            Highlighter highlighter = new Highlighter(formatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));

            System.out.println(highlighter.getBestFragment(analyzer, "content", text));

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
