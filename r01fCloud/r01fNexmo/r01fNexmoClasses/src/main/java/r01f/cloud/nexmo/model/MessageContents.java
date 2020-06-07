package r01f.cloud.nexmo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.url.Url;

public abstract class MessageContents {

/////////////////////////////////////////////////////
// Message Content Text Type
/////////////////////////////////////////////////////	
	@MarshallType(as="content",typeId="text")
	@Accessors(prefix="_")
	public static class TextMessageContent 
			extends MessageContentBase {		
		@MarshallField(as="text")
	    @Setter @Getter  private  String  _text;
		
		public TextMessageContent() {
			super();			
		}		
		public TextMessageContent(@MarshallFrom("text") final String text) {
			super(MessageContentType.text);	
			_text = text;
		}	
	}
/////////////////////////////////////////////////////
//Message Content Text Image
/////////////////////////////////////////////////////	
	@MarshallType(as="content",typeId="image")
	@Accessors(prefix="_")
	public static class ImageMessageContent 
		extends MessageContentBase {
		
		@MarshallField(as="url")
		@Setter @Getter private  Url  _url;
		
		public ImageMessageContent() {
			super();			
		}
		public ImageMessageContent(@MarshallFrom("url") final Url url) {
			super(MessageContentType.image);	
			_url = url;
		}
	}	
}
