package org.nfctools.ndef.mime;

import org.nfctools.ndef.NdefConstants;
import org.nfctools.ndef.NdefMessageEncoder;
import org.nfctools.ndef.NdefRecord;
import org.nfctools.ndef.wkt.encoder.RecordEncoder;
import org.nfctools.ndef.wkt.records.Record;

public class TextMimeRecordEncoder implements RecordEncoder {

	@Override
	public boolean canEncode(Record record) {
		return record instanceof TextMimeRecord;
	}

	@Override
	public NdefRecord encodeRecord(Record record, NdefMessageEncoder messageEncoder) {
		TextMimeRecord textMimeRecord = (TextMimeRecord)record;
		return new NdefRecord(NdefConstants.TNF_MIME_MEDIA, textMimeRecord.getContentType().getBytes(), record.getId(),
				textMimeRecord.getContent().getBytes());
	}
}
