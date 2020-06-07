package r01f.cloud.nexmo.model.inbound;

// https://developer.nexmo.com/api/messages-olympus#inbound-message
/**
 * {
  "message_uuid": "aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
  "to": {
    "type": "messenger",
    "id": "01234567",
    "number": "447700900000"
  },
  "from": {
    "type": "messenger",
    "id": "0123456789012345",
    "number": "447700900000"
  },
  "timestamp": "2020-01-01T14:00:00.000Z",
  "message": {
    "content": {
      "type": "text",
      "text": "Hello World!",
      "image": {
        "url": "https://example.com/image.jpg",
        "caption": "Additional text to accompany the image."
      },
      "audio": {
        "url": "https://example.com/audio.mp3"
      },
      "video": {
        "url": "https://example.com/video.mp4",
        "caption": "Additional text to accompany the image."
      },
      "file": {
        "url": "https://example.com/file.zip",
        "caption": "Additional text to accompany the image."
      },
      "location": {
        "lat": "51.5228349",
        "long": "-0.0854414",
        "url": "abc123",
        "address": "15 Bonhill St London EC2A 4DN",
        "name": "Nexmo London"
      }
    }
  }
}
 * @author IOLABARO
 *
 */


public class NexmoInboundMessage {
	//
}
