package org.nfctools.ndef.mime;

import org.nfctools.ndef.wkt.records.Record;

public class TextMimeRecord extends Record {

	private String contentType;
	private String content;

	public TextMimeRecord(String contentType, String content) {
		this.contentType = contentType;
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Content-Type: " + contentType + "; Content: [" + content + "]";
	}

}
