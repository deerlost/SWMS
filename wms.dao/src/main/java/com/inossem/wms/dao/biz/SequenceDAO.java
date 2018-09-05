package com.inossem.wms.dao.biz;


public interface SequenceDAO {
	long selectNextVal(String seq_name);
}
