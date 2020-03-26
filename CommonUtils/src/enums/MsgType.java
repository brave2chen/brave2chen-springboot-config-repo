package enums;

/**
 * kafka队列消息，消息类型ID定义，MSG_HEAD.MSG_TYPE
 */
public enum MsgType implements Valuable {
	/** 业务路由方案上报消息 */
	CIRCUIT_ROUTE( 9 ),
	/** 备选通道路由方案上报消息 */
	ALTERNATIVE_CHANNEL_ROUTE( 10 ),
	/** 原子路由路由方案上报消息 */
	ODR_AR_ROUTE( 11 );

	private int value;

	private MsgType( int value ) {
		this.value = value;
	}

	/**
	 * 获取msgType
	 * 
	 * @return the value
	 */
	@Override
	public int getValue() {
		return this.value;
	}
}