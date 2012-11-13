package com.nikmesoft.android.nearfood.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LoginHandler extends DefaultHandler {
	private final static String RESULT_XML = "result";
	private final static String ERRORCODE_XML = "ErrorCode";
	private final static String ERRORCODE_ID_XML = "errorID";
	private final static String ERRORCODE_MSG_XML = "errorMsg";
	private StringBuilder builder;
	private String result;
	private ErrorCode errorCode;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (this.errorCode != null) {
			if (localName.equalsIgnoreCase(ERRORCODE_ID_XML)) {
				errorCode.setErrorID(builder.toString().trim());
			} else if (localName.equalsIgnoreCase(ERRORCODE_MSG_XML)) {
				errorCode.setErrorMsg(builder.toString().trim());
			}
		} else if (localName.equalsIgnoreCase(RESULT_XML)) {
			result = builder.toString().trim();
		}
		builder.setLength(0);

	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equalsIgnoreCase(ERRORCODE_XML)) {
			this.errorCode = new ErrorCode();
		}
	}

	public String getResult() {
		if (errorCode == null) {
			return result;
		} else {
			return errorCode.getErrorMsg();
		}
	}

}
