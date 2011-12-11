/**
 * Copyright 2011 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nfctools.ndef.wkt.records;

import java.nio.charset.Charset;

public class UriRecord extends Record {

	public static final byte[] TYPE = { 'U' };
	public static final Charset DEFAULT_URI_CHARSET = Charset.forName("UTF-8");

	public static final String[] abbreviableUris = { "", "http://www.", "https://www.", "http://", "https://", "tel:",
			"mailto:", "ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://", "smb://", "nfs://", "ftp://",
			"dav://", "news:", "telnet://", "imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:", "btspp://",
			"btl2cap://", "btgoep://", "tcpobex://", "irdaobex://", "file://", "urn:epc:id:", "urn:epc:tag:",
			"urn:epc:pat:", "urn:epc:raw:", "urn:epc:", "urn:nfc:" };

	private String uri;

	public UriRecord(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	@Override
	public String toString() {
		return "Uri: [" + uri + "]";
	}
}
