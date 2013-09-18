package com.mangocity.hotel.base.manage;

import java.util.List;
import java.util.Map;

/**
 * 批量开关房控制
 * @author xuyiwen
 *
 */
public interface BatchRoomControlManage {
	List<Map> getHotelRoomType(Long id);
}
