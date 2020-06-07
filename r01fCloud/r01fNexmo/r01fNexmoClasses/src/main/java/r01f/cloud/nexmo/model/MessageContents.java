package r01f.cloud.nexmo.model;

import lombok.Getter;
import r01f.objectstreamer.annotations.MarshallField;

public abstract class MessageContents {

	public static class TextMessageContent 
			extends MessageContentBase {

		@MarshallField(as="text")
		@Getter  private  final String  _text;
		
		public TextMessageContent(final String text) {
			super(MessageContentType.text);	
			_text = text;
		}
	}
}
