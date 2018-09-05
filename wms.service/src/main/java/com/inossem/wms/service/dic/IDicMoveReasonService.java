package com.inossem.wms.service.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicMoveReason;

public interface IDicMoveReasonService {
	List<DicMoveReason> getMoveTypeByMoveTypeId(int moveTypeId);
}
