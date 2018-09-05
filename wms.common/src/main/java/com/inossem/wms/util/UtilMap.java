package com.inossem.wms.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilMap {

	private static final Logger logger = LoggerFactory.getLogger(UtilMap.class);

	/**
	 * map.values转换为list
	 */
	public static List mapToList(Map map) {
		List list = new ArrayList();
		// 获得map的Iterator
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			list.add(entry.getValue());
		}
		return list;
	}
}
