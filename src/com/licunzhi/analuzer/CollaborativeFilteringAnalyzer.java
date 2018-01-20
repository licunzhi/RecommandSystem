package com.licunzhi.analuzer;

import com.licunzhi.bean.HabitsBean;
import com.licunzhi.calculate.ReaderFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collaborative Filter
 * @author lcz
 * 2018/01/18
 */
public class CollaborativeFilteringAnalyzer extends Analyzer {
	
	/**
	 * Calculate similarity value of users in lists and save value 
	 * into the recommendTable,value describes how popular the item is.
	 * @param lists the user score from file u1.base
	 * @return recommendTable[][]
	 */
	public float[][] userSimilarityConsine(List<?> lists) throws IOException {
		Set<String> setsA = new HashSet<String>(), setsB = new HashSet<String>();
		float[][] recommendTable = new float[lists.size()][lists.size()];
		HabitsBean a, b;
		for (int i = 0; i < lists.size(); i++) {
			a = (HabitsBean)lists.get(i);
			setsA.addAll(a.getArray());
			recommendTable[i][i] = 0;//the same user is zero
			for (int j = i + 1; j < lists.size(); j++) {
				b = (HabitsBean)lists.get(j);
				float count = 0;
				
				//caculate the similarity value for a comparing with b
				for (int h = 0; h < b.getSize(); h++) {
					if(setsA.contains(b.getString(h)))
						count ++;
				}
				recommendTable[i][j] = (float) (count/(Math.sqrt(a.getSize() * b.getSize())));
				
				//caculate the similarity value for b comparing with a
				setsB.addAll(b.getArray());
				for (int h = 0; h < a.getSize(); h++) {
					if(setsB.contains(a.getString(h)))
						count ++;
				}
				recommendTable[j][i] = (float) (count/(Math.sqrt(a.getSize() * b.getSize())));
				
//				if(recommendTable[j][i] > 1)
//				System.out.println("recommendTable[" + j + "][" + i + "]" + recommendTable[j][i] + " : " 
//									+ "recommendTable[" + i + "][" + j + "]" + recommendTable[i][j]);
				setsB.clear();
			}
			setsA.clear();
		}
		//相似度结果的存储
//		String filePath = "D:/RecommandSystem/src/similar.txt";
//		File file = new File(filePath);
//		//943
//		StringBuilder context = new StringBuilder("");
//		for (int m = 0; m < 943; m++) {
//			context.append("这是用户" + m + "相似度计算结果:\n");
//			for (int n = 0; n < 943; n++) {
//				context.append("user-" + m + " and user-" + n + ": "+ recommendTable[m][n] + "\n");
//			}
//			context.append("\n");
//		}
//		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
//		String contextStr = context.toString();
//		int len = contextStr.getBytes().length;
//		for (int i = 0; i < len; i++) {
//			outputStream.write(contextStr.getBytes(), 0 , len -1);
//		}
//		outputStream.close();
		return recommendTable;
	}

	// main function
	public static void main(String[] args) {
		//CollabrativeFilter class
		CollaborativeFilteringAnalyzer analyzer = new CollaborativeFilteringAnalyzer();
		try {
			analyzer.userSimilarityConsine(new ReaderFormat().formateLogUser("u1.base"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
