package com.mangocity.hotel.order.service.assistant;
/**
 * 渠道信息封
 * @author chenjuesu
 *
 */
public class ChannelInfo {

	 // ID
    private Long ID;

    // 名称
    private String channelName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}
}
