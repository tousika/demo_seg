package org.ansj.splitWord.analysis;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.recognition.arrimpl.PersonRecognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil;
import org.ansj.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.ObjConver;

import java.io.Reader;
import java.util.*;

/**
 * 标准分词
 *
 * @author ansj
 */
public class ToAnalysis extends Analysis {
	private static final List<String> myDicNatures = Arrays.asList("3", "4", "8");
	@Override
	protected List<Term> getResult(final Graph graph) {

		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {

				forwardMax(graph, forests);

				graph.walkPath();

//				userDefineRecognition(graph, forests);

				// 数字发现
				if (isNumRecognition) {
					new NumRecognition(isQuantifierRecognition && graph.hasNumQua).recognition(graph);
				}
				// 姓名识别
				if (graph.hasPerson && isNameRecognition) {
					// 人名识别
					new PersonRecognition().recognition(graph);
				}
				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);
				return getResult();
			}
			//正向最大匹配
			private void forwardMax(final Graph graph, Forest... forests){
				String temp = null;
				int min=0,max=-1;
				Map<Integer, Term> map = new HashMap<>();
				if (forests != null) {
					for (Forest forest : forests) {
						if (forest == null) {
							continue;
						}
						GetWord word = forest.getWord(graph.chars);
						while ((temp = word.getAllWords()) != null) {
							if ((word.offe==min || word.offe>max) && myDicNatures.contains(word.getParam(0)) ){
//								terms[word.offe] = new Term(temp, word.offe, word.getParam(0), ObjConver.getIntValue(word.getParam(1)));
								map.put(word.offe, new Term(temp, word.offe, word.getParam(0), ObjConver.getIntValue(word.getParam(1))));
								min = word.offe;
								max = word.offe+temp.length()-1;
							}
						}
					}
				}
				for (Map.Entry<Integer, Term> e : map.entrySet()){
					TermUtil.insertTerm(graph.terms, e.getValue(),InsertTermType.SKIP);
				}
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(InsertTermType.SKIP, forests).recognition(graph);
				graph.rmLittlePath();
				graph.walkPathByScore();
			}

			private List<Term> getResult() {
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						setIsNewWord(graph.terms[i]) ;
						result.add(graph.terms[i]);
					}
				}
				setRealName(graph, result);
				return result;
			}
		};
		return merger.merger();
	}

	public ToAnalysis() {
		super();
	}

	public ToAnalysis(String userDefineKey) {
		super(userDefineKey);
	}

	public ToAnalysis(Reader reader) {
		super.resetContent(new AnsjReader(reader));
	}

	public static Result parse(String str) {
		return new ToAnalysis().parseStr(str);
	}

	public static Result parse(String str, Forest... forests) {
		return new ToAnalysis().setForests(forests).parseStr(str);
	}

	public static Result parse(String userDefineKey, String str) {
		return new ToAnalysis(userDefineKey).parseStr(str);
	}

	public static Result parse(String userDefineKey, String str, Forest... forests) {
		return new ToAnalysis(userDefineKey).setForests(forests).parseStr(str);
	}

}
