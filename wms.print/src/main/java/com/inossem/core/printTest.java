/*package com.inossem.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class printTest {

	@Test
	public void test() {
		System.out.println(1);
	}

	@Test
	public void testListInteger() {
		
		 * List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
		 * List<List<Integer>> subSets = Lists.partition(intList, 3);
		 * 
		 * List<Integer> lastPartition = subSets.get(2); List<Integer>
		 * expectedLastPartition = Lists.<Integer>newArrayList(7, 8);
		 * System.out.println(lastPartition.toString());
		 * System.out.println(expectedLastPartition.toString());
		 
		System.out.println(1);
	}

	@Test
	public void givenCollection() {
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> intCollection = Lists.newArrayList(map);

		Iterable<List<Map<String, Object>>> subSets = Iterables.partition(intCollection, 3);

		List<Map<String, Object>> ite = subSets.iterator().next();

	}

	@Test
	public void givenCollection_whenParitioningIntoNSublists_thenCorrect() {
		Collection<Integer> intCollection = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);

		Iterable<List<Integer>> subSets = Iterables.partition(intCollection, 3);

		List<Integer> firstPartition = subSets.iterator().next();
		Iterator tera = subSets.iterator();
		while(tera.hasNext()){
			List<Integer> list = (List<Integer>) tera.next();
			System.out.println(list.toString());
		}
		List<Integer> expectedLastPartition = Lists.<Integer>newArrayList(1, 2, 3);
		System.out.println(this. equalTo(firstPartition,expectedLastPartition));
		System.out.println(firstPartition.toString());
		System.out.println(expectedLastPartition.toString());
	}

	@SuppressWarnings("unused")
	private int equalTo(List<Integer>o1,List<Integer>o2){
		Integer s1= 0;
		Integer s2= 0;
		for(int i=0;i<o1.size();i++){
			s1+=o1.get(i);
		}
		for(int i=0;i<o2.size();i++){
			s2+=o2.get(i);
		}
		if(s1 != null){
			return s1.compareTo(s2);
		}
		return 0;
	}
	
}
*/