1.清洗词典
2.解决交集型歧义
链长 	1	    2   	3	    4       5       6	    7	    8   	总计
词次数	47402	28790	1217    608	    29	    19	    2	    1	    78248
比例  	50.58	47.02	1.56	0.78	0.04	0	    0	    0	    100
字段数	12686	10131	743	    324	    22	    5	    2	    1	    23914
比例	    53.05	42.36	3.11	1.35    0.09	0.02	0.01	0.01	100
3.自定义词一般是专属名词，不想拆分的特定词，
如果分词器自动拆出来的所有term都是n开始的词性，就没有必要加入自定义词典
但是"张馨予结婚"不符合词规律，但是本身是主谓结构构成句子就不应该加入自定义词典
