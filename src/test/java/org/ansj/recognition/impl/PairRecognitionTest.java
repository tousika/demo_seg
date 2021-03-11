package org.ansj.recognition.impl;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by Ansj on 01/12/2017.
 */
public class PairRecognitionTest {

	@Test
	public void test() {


		String[] tests = new String[]{
//				"2020年10月12日讯（具体拍摄时间不详），第21届英国伦敦，一只巨型“红毛猩猩”占领一个废弃教堂，将其作为自己的新家，路人看到这一场景都被震惊了。这只巨型电子猩猩在这个位于市中心的废弃教堂中“玩耍”。天然果酱品牌Meridian设计了这一噱头，试图强调如果婆罗洲猩猩的栖息地继续被破坏，为棕榈油种植园让路，这些猩猩将会灭绝。目前数据显示38%的英国人依然不可避免使用棕榈油，世界自然基金会表示，50%的包装产品中都含有这种物质。",
//				"“只有詹姆斯能让我心甘情愿做替补！”联盟曾经第一中锋何出此言",
//				"当地时间2020年10月10日，在接受新冠治疗出院后，美国总统特朗普在美国华盛顿特区白宫首次出席公共活动。站在白宫二层阳台上，特朗普摘掉口罩发表讲话。他说，自己“感觉很好”。特朗普10月1日晚新冠病毒检测呈阳性。2日晚，他入住沃尔特·里德国家军事医疗中心接受治疗，并于5日返回白宫继续疗程。白宫医生肖恩·康利8日表示，特朗普已于当天完成新冠治疗，预计10日可“安全重返”公共活动。",
//				"2020年10月8日，香港百佳超级市场。其在10月11日宣布，将推出涉及超过3200万港元的抽奖活动，市民12日至18日于网上或电话登记资料便可参加抽奖，不用到百佳消费。百佳将在10月23日由电脑随机抽出32万名得奖者，所有得奖者将可获100元百佳现金券。部分得奖者除了可获现金券外，还分别可以参加“1分钟扫货”以及获赠礼品套装。此前，香港特区政府要求两间连锁超级市场惠康及百佳，申请第二期保就业计划后须回馈社会。 张炜(香港分社)中新社",
//				"2020年10月9日，香港卫生署卫生防护中心公布，香港新增7宗新冠肺炎确诊个案，至今累计5182宗确诊个案。本地确诊个案中，位于葵涌的私营残疾人士院舍“国宝之家”再多两名人士确诊。图为香港葵涌私营残疾人士院舍“国宝之家”所在的大厦。 李志华(香港分社)/中新社",
//				"2020年10月10日，由郑州市卫健委主办的“疫路同行·科学防控·护佑健康”院士论坛，在河南省人民会堂举办。中国工程院院士、著名感染病学专家、国家卫健委高级别专家组成员李兰娟院士为大家讲解《科学防控疫情，护卫人民健康》，随后率院士专家团队到郑州市第三人民医院进行义诊、查房。",
//				"2020年10月11日，“百年传承 上海匠心——上海市档案馆藏老字号历史文献选萃”展览作为重要组成部分，亮相“2020第十四届中华老字号博览会”，档案修复师现场演示修复老资料技艺。",
//				"市民在北京首都博物馆参观《文物的时空漫游 “互联网+中华文明”》数字体验展",
//                "无风不起浪。一个时期以来，在国际大气候和台湾岛内小气候影响下，蔡英文当局愈加挟洋自重，加紧与外部反华势力勾连聚合，频频制造事端，严重危害两岸和平和台海局势稳定。根据国际危机组织观察，台湾海峡已被列入“重大形势恶化地区”。将心比心，两岸人民都不愿兵戎相见。但如不幸战事爆发，罪魁祸首必是“台独”。更须值得警惕的是，战事未发，情报先行。台湾情治部门不仅长期把祖国大陆当成攻击目标开展情报活动，近年来更沦为蔡英文当局党同伐异、铲除异己、推动“台独”路线的工具。对内，借助修订“国安五法”、出台“反渗透法”，大搞“绿色恐怖”，严密监控岛内民众，限缩两岸交流交往。对外，奉行“金钱外交”“情报外交”，甘当棋子，不惜出卖国家主权和台湾人民切身利益，换取美西方的庇护。",
				"东北师范大学;斯坦福大学工学院“特曼奖”大陆教师首次获得者“Intel国际大赛指导教师表彰证书”;《生物学探究性学习教学示例》《当代中学生新知识百科生物卷》等"

		};

		for (String str : tests) {

			for (Term term : ToAnalysis.parse(str).recognition(new PairRecognition("rule_s",new HashMap<String,String>(){{put("‘","’");put("“","”");}})).getTerms()) {
				System.out.print(term + " ");
			}
			System.out.println("--------------------------------------------");
		}

	}
}