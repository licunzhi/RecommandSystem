package com.licunzhi.controller;

import com.licunzhi.analuzer.CollaborativeFilteringAnalyzer;
import com.licunzhi.bean.BasicBean;
import com.licunzhi.bean.HabitsBean;
import com.licunzhi.calculate.ReaderFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the core in the system,it's a recommend system based on user field.
 * It calculate results with the smiliarity for users' habits.
 * @author wqd
 *
 */
public class UserRecommender {
	
	/**
	 * The method recommend a lists of items for users after calculate the similarity and get the 
	 * top range.According to the most similar users, there is a items lists to recommend a user
	 * serval items. 
	 * @param lists
	 * @param recommendTable
	 * @param user
	 * @param range
	 * @return
	 */
	public List<String> recommend(List<HabitsBean> lists, float[][] recommendTable, BasicBean user,int range) {
		int i = 0;
		HabitsBean bean = null;
		for ( ; i < lists.size(); i++) {
			bean = lists.get(i);
			if(bean.getId() == user.getId())
				break;
		}
		// TODO throws over size exception
		
		//排序的方式实现查询最高相似度
		List<Float> row = new ArrayList<Float>(recommendTable[i].length);
		for (int j = 0; j < recommendTable[i].length; j++) {
			row.add(recommendTable[i][j]);
		}
		Collections.sort(row);
		
		//get the common range of these HabitBean
		int start = row.size() - 1;
		Set<String> sets = new HashSet<String>();
		for (int j = start; j > start - range; j--) {
			// TODO change the method for habitBean,it's a condition
			// the lists id  aren't frequently.
			HabitsBean habits = lists.get(j);
			sets.addAll(habits.getArray());
		}
		
		sets.removeAll(bean.getArray());
		
		return new ArrayList<String>(sets);
	}
	
	
	public static void main(String[] args) throws IOException {
		UserRecommender recommender = new UserRecommender();
		List<HabitsBean> lists = new ReaderFormat().formateLogUser("D:/RecommandSystem/src/u1.base");
		float[][] recommendTable = new CollaborativeFilteringAnalyzer().userSimilarityConsine(lists);
		HabitsBean user = (HabitsBean) lists.get(5);
		List<String> result = recommender.recommend(lists , recommendTable, user, 5);
		result.forEach((str) ->System.out.println(str));
	}
}
