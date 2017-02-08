package sneps.snip;

import sneps.snip.channels.Channel;

public class Request {
	
	private Channel channel;
	
	public Request(Channel channel) {
		this.channel = channel;
	}
	
	public Channel getChannel() {
		return channel;
	}

}