package pl.wroc.pwr.student.acteditor.parsing.xsd;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.student.acteditor.parsing.Parser;
import pl.wroc.pwr.student.acteditor.util.Utils;

public class XSDParser implements Parser {
	private String xsdContent;
	private String[] xsdLines;

	public XSDParser(String xsdContent) {
		this.xsdContent = xsdContent;
	}

	@Override
	public String[] getElementStructure(String elementName) {
		List<String> result;

		if (xsdLines == null) {
			xsdLines = xsdContent.split("\n");
		}

		result = getLinesWithElement(elementName, xsdLines);

		String[] tmp = Utils.convertStringListToArray(result);

		return tmp;
	}

	private List<String> getLinesWithElement(String elementName, String[] lines) {
		boolean read = false;
		String tag = "";
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < xsdLines.length; i++) {
			String currentLine = getWithoutInitialSpaces(xsdLines[i]);

			if (hasElementInLine(elementName, currentLine)) {
				read = true;
				tag = getTagName(currentLine);

				System.out.println(currentLine);
				result.add(currentLine);

				continue;
			}

			if (read == true) {
				String endingTag = getEndingTag(tag);

				if (currentLine.equals(endingTag)) {
					read = false;
				}

				System.out.println(currentLine);
				result.add(currentLine);
			}
		}
		return result;
	}

	private String getWithoutInitialSpaces(String text) {
		return text.substring(text.indexOf("<"));
	}

	public boolean hasElementInLine(String elementName, String line) {
		return line.contains("element name=\"" + elementName);
	}

	private String getTagName(String line) {
		return line.split(" ")[0];
	}

	private String getEndingTag(String tag) {
		return tag.replaceAll("<", "</") + ">";
	}

	@Override
	public String getDescription(String elementName) {
		String[] lines = getElementStructure(elementName);

		for (String eachLine : lines) {
			String description = getLineWithDescription(eachLine);
			if (description == null) {
				continue;
			} else {
				return description;
			}
		}

		return null;
	}

	private String getLineWithDescription(String eachLine) {
		if (eachLine.contains("xsd:documentation>")) {
			return eachLine.substring(eachLine.indexOf("<xsd:documentation>"), eachLine.indexOf("</xsd:documentation>"));
		}
		return null;
	}
}