package org.ansj.splitWord.analysis;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.arrimpl.PersonRecognition;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.ObjConver;

import java.io.Reader;
import java.util.*;

/**
 * 用于检索的分词方式
 * 
 * @author ansj
 * 
 */
public class IndexAnalysis extends Analysis {

	@Override
	protected List<Term> getResult(final Graph graph) {
		Merger merger = new Merger() {

			@Override
			public List<Term> merger() {
				graph.walkPath();

				// 数字发现
				if (isNumRecognition) {
					new NumRecognition(isQuantifierRecognition).recognition(graph);
				}

				// 姓名识别
				if (graph.hasPerson && isNameRecognition) {
					// 人名识别
					new PersonRecognition().recognition(graph);
				}

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				return result();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(InsertTermType.SKIP, forests).recognition(graph);
				graph.rmLittlePath();
				graph.walkPathByScore();
			}

			/**
			 * 一层分词后避免词条碰撞而产生跨越的单词（消减歧义）
			 * @return
			 */
			private Set<String> filterSpanTerm(){
				int length = graph.terms.length - 1;
				Set<String> r = new HashSet<>();
				if (length > 1) {
					String[][] array= new String[length][length];
					int row = 0;
					for (int i = 0; i < length; i++) {
						if (graph.terms[i] != null) {
							char[] c = graph.terms[i].getName().toCharArray();
							array[row][0]= String.valueOf(c[0])+(graph.terms[i].getOffe()-1);
							array[row][1]=String.valueOf(c[c.length-1]);
							row++;
						}
					}
					for(int i=0;i<row-1;i++){
						r.add(array[i][1]+array[i+1][0]);
					}
				}
				return r;
			}

			/**
			 * 检索的分词
			 * 
			 * @return
			 */
			private List<Term> result() {

				String temp = null;

				Set<String> set = new HashSet<>();

				List<Term> result = new LinkedList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						setIsNewWord(graph.terms[i]) ;
						result.add(graph.terms[i]);
						set.add(graph.terms[i].getName() + graph.terms[i].getOffe());
					}
				}
				Set<String> filterSet = filterSpanTerm(); //对应v5.1.5.3
//				filterSet = new HashSet<>(); //重置filterSpanTerm的结果,对应v5.1.5.2
				LinkedList<Term> last = new LinkedList<Term>();

				char[] chars = graph.chars;

				if (forests != null) {
					for (Forest forest : forests) {
						if (forest == null) {
							continue;
						}
						GetWord word = forest.getWord(chars);
						while ((temp = word.getAllWords()) != null) {
							if (!set.contains(temp + word.offe) && !filterSet.contains(temp + word.offe)) {
								set.add(temp + word.offe);
								last.add(new Term(temp, word.offe, word.getParam(0), ObjConver.getIntValue(word.getParam(1))));
							}
						}
					}
				}

				result.addAll(last);

				Collections.sort(result, new Comparator<Term>() {

					@Override
					public int compare(Term o1, Term o2) {
						if (o1.getOffe() == o2.getOffe()) {
							return o2.getName().length() - o1.getName().length();
						} else {
							return o1.getOffe() - o2.getOffe();
						}
					}
				});

				setRealName(graph, result);
				return result;
			}
		};

		return merger.merger();
	}

	public IndexAnalysis() {
		super();
	}

	public IndexAnalysis(Reader reader) {
		super.resetContent(new AnsjReader(reader));
	}

	public static Result parse(String str) {
		return new IndexAnalysis().parseStr(str);
	}

	public static Result parse(String str, Forest... forests) {
		return new IndexAnalysis().setForests(forests).parseStr(str);
	}

}
