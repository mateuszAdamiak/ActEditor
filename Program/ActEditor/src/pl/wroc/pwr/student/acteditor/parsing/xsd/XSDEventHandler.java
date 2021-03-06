package pl.wroc.pwr.student.acteditor.parsing.xsd;

import java.util.Stack;

import pl.wroc.pwr.student.acteditor.model.AttributeRegistry;
import pl.wroc.pwr.student.acteditor.model.ElementRegistry;
import pl.wroc.pwr.student.acteditor.model.TypeRegistry;
import pl.wroc.pwr.student.acteditor.model.tags.Attribute;
import pl.wroc.pwr.student.acteditor.model.tags.AttributeGroup;
import pl.wroc.pwr.student.acteditor.model.tags.Composition;
import pl.wroc.pwr.student.acteditor.model.tags.Element;
import pl.wroc.pwr.student.acteditor.model.tags.SimpleElement;
import pl.wroc.pwr.student.acteditor.model.tags.SimpleType;

public class XSDEventHandler {
	private Stack stack;
	private XSDHelper helper;
	private boolean complexTypeDefinition = false;
	private ElementRegistry elements = ElementRegistry.getRegistry();
	private AttributeRegistry attributes = AttributeRegistry.getRegistry();
	private TypeRegistry types = TypeRegistry.getRegistry();
	
	public XSDEventHandler() {
		stack = new Stack();
		helper = new XSDHelper();
	}
	
	public void handleEvent(String token, String line) {
		switch(token) {
		case "documentation":
			documentationEventHandle(line);
			break;
		case "element-def":
			elementDefinitionEventHandle(line);
			break;
		case "one-line-element-def":
			oneLineElementDefinitionEventHandle(line);
			break;
		case "close-element-def":
			closingElementEventHandle(line);
			break;
		case "element-ref-without-closing":
			elementReferenceEventHandle(line);
			break;
		case "one-line-element-ref":
			oneLineElementReferenceEventHandle(line);
			break;
		case "sequence":
			sequenceEventHandle(line);
			break;
		case "closing-sequence":
			closingSequenceEventHandle(line);
			break;
		case "choice":
			choiceEventHandle(line);
			break;
		case "closing-choice":
			closingChoiceEventHandle(line);
			break;
		case "element-group-def":
			elementGroupDefinitionEventHandle(line);
			break;
		case "element-group-ref":
			elementGroupReferenceEventHandle(line);
			break;
		case "closing-element-group-def":
			closingElementGroupDefinitionEventHandle(line);
			break;
		case "complex-type-def":
			complexTypeDefinition = true;
			complexTypeDefinitionEventHandle(line);
			break;
		case "closing-complex-type-def":
			if(complexTypeDefinition) {
				closingComplexTypeDefinitionEventHandler(line);
				complexTypeDefinition = false;
			}
			break;
		case "attribute-def-without-closing":
			attributeDefinitionEventHandle(line);
			break;
		case "one-line-attribute-def":
			oneLineAttributeDefinitionEventHandle(line);
			break;
		case "closing-attribute-def":
			closingAttributeDefinitionEventHandle(line);
			break;
		case "attribute-group-def":
			attributeGroupDefinitionEventHandle(line);
			break;
		case "attribute-group-ref":
			attributeGroupReferenceEventHandle(line);
			break;
		case "closing-attribute-group":
			closingAttributeGroupEventHandle(line);
			break;
		case "simple-type-def":
			simpleTypeDefinitionEventHandle(line);
			break;
		case "simple-type-beg":
			simpleTypeBeginningEventHandle(line);
			break;
		case "closing-simple-type" :
			closingSimpleTypeEventHandle(line);
			break;
		case "enumeration" :
			enumerationEventHandle(line);
			break;
		case "pattern" :
			patternEventHandle(line);
			break;
		}
	}
	
	private void patternEventHandle(String line) {
		SimpleType simpleType = (SimpleType) stack.pop();
		String pattern = helper.getAttribute("value", line);
		simpleType.setPattern(pattern);
		stack.push(simpleType);
	}

	private void enumerationEventHandle(String line) {
		SimpleType simpleType = (SimpleType) stack.pop();
		String enumeration = helper.getAttribute("value", line);
		simpleType.add(enumeration);
		stack.push(simpleType);
	}

	private void simpleTypeBeginningEventHandle(String line) {
		SimpleType simpleType = new SimpleType();
		stack.push(simpleType);
	}

	private void closingSimpleTypeEventHandle(String line) {
		SimpleType simpleType = (SimpleType) stack.pop();
		if(!stack.isEmpty()) {
			Object parent = stack.pop();
			if(parent instanceof Attribute) {
				((Attribute)parent).setSimpleType(simpleType);
				stack.push(parent);
			}
		} else {
			types.add(simpleType);
		}
	}

	private void simpleTypeDefinitionEventHandle(String line) {
		SimpleType simpleType = helper.createSimpleTypeDefinition(line);
		stack.push(simpleType);
	}
	
	private void closingAttributeGroupEventHandle(String line) {
		Attribute attribute = (Attribute) stack.pop();
		attributes.add(attribute);
	}

	private void attributeGroupReferenceEventHandle(String line) {
		Attribute attribute = helper.createGroupReference(line);
		Object parent = stack.pop();
		if(parent instanceof Element) {
			((Element)parent).addAttribute(attribute);
		} else if(parent instanceof Attribute) {
			((Attribute)parent).add(attribute);
		}
		stack.push(parent);
	}
	
	private void attributeGroupDefinitionEventHandle(String line) {
		Attribute attribute = helper.createGroupDefinition(line);
		stack.push(attribute);
	}
	
	private void closingAttributeDefinitionEventHandle(String line) {
		Attribute attribute = (Attribute) stack.pop();
		Object parent = stack.pop();
		if(parent instanceof Element) {
			((Element)parent).addAttribute(attribute);
		} else if(parent instanceof Attribute) {
			((Attribute)parent).add(attribute);
		}
		stack.push(parent);
	}

	private void oneLineAttributeDefinitionEventHandle(String line) {
		Attribute attribute = helper.createAttribute(line);
		Object parent = stack.pop();
		if(parent instanceof Element) {
			((Element)parent).addAttribute(attribute);
		} else if(parent instanceof Attribute) {
			((Attribute)parent).add(attribute);
		}
		stack.push(parent);
	}

	private void attributeDefinitionEventHandle(String line) {
		Attribute attribute = helper.createAttribute(line);
		stack.push(attribute);
	}
	
	private void closingComplexTypeDefinitionEventHandler(String line) {
		Element element = (Element) stack.pop();
		elements.add(element);
	}

	private void complexTypeDefinitionEventHandle(String line) {
		String name = helper.getAttribute("name", line);
		Element element = new Composition(name, "complex");
		stack.push(element);
	}

	private void closingElementGroupDefinitionEventHandle(String line) {
		Element element = (Element) stack.pop();
		elements.add(element);
	}

	private void elementGroupReferenceEventHandle(String line) {
		Element element = helper.createElementGroupReference(line);
		String minOccurs = helper.getAttribute("minOccurs", line);
		String maxOccurs = helper.getAttribute("maxOccurs", line);
		if(!minOccurs.equals("")) {
			element.setMinOccurs(minOccurs);
		}
		if(!maxOccurs.equals("")) {
			element.setMaxOccurs(maxOccurs);
		}
		Element parent = (Element) stack.pop();
		parent.add(element);
		stack.push(parent);
	}
	
	private void elementGroupDefinitionEventHandle(String line) {
		Element element = helper.createElementGroupDefinition(line);
		stack.push(element);
	}
	
	private void closingChoiceEventHandle(String line) {
		Element element = (Element) stack.pop();
		if(!stack.isEmpty()) {
			Element parent = (Element) stack.pop();
			parent.add(element);
			stack.push(parent);
		}
	}
	
	private void closingSequenceEventHandle(String line) {
		Element element = (Element) stack.pop();
		Element parent = (Element) stack.pop();
		parent.add(element);
		stack.push(parent);
	}

	private void sequenceEventHandle(String line) {
		Element element = new Composition("rozwi�", "seq");
		String minOccurs = helper.getAttribute("minOccurs", line);
		String maxOccurs = helper.getAttribute("maxOccurs", line);
		if(!minOccurs.equals("")) {
			element.setMinOccurs(minOccurs);
		}
		if(!maxOccurs.equals("")) {
			element.setMaxOccurs(maxOccurs);
		}
		stack.push(element);
	}

	private void oneLineElementDefinitionEventHandle(String line) {
		Element element = helper.createElementDefinition(line);
		elements.add(element);
	}

	private void elementReferenceEventHandle(String line) {
		Element element = helper.createElementReference(line);
		stack.push(element);
	}

	private void oneLineElementReferenceEventHandle(String line) {
		Element element = helper.createElementReference(line);
		Element parent = null;
		if(!stack.isEmpty()) {
			parent = (Element) stack.pop();
			parent.add(element);
			stack.push(parent);
		}
	}
	
	private void closingElementEventHandle(String line) {
		Element element = (Element) stack.pop();
		if(element.getType().equals("ref")) {
			Element parent = (Element) stack.pop();
			parent.add(element);
			stack.push(parent);
		} else {
			elements.add(element);
		}
	}

	private void documentationEventHandle(String line) {
		Object parent = null;
		if(!stack.isEmpty()) {
			parent = stack.pop();
		}
		if(parent instanceof Composition || parent instanceof SimpleElement) {
			Element element = (Element) parent;
			element = (Element) helper.setDescription(element, line);
			stack.push(element);
		}	else if(parent instanceof Attribute || parent instanceof AttributeGroup) {
			Attribute attribute = (Attribute) parent;
			attribute = (Attribute) helper.setDescription(attribute, line);
			stack.push(attribute);
		} else if(parent instanceof SimpleType) {
			SimpleType simpleType = (SimpleType) parent;
			simpleType = (SimpleType) helper.setDescription(simpleType, line);
			stack.push(simpleType);
 		}
	}

	private void elementDefinitionEventHandle(String line) {
		Element element = helper.createElementDefinition(line);
		stack.push(element);
	}

	public void choiceEventHandle(String line) {
		Element element = new Composition("rozwi�", "choice");
		String minOccurs = helper.getAttribute("minOccurs", line);
		String maxOccurs = helper.getAttribute("maxOccurs", line);
		if(!minOccurs.equals("")) {
			element.setMinOccurs(minOccurs);
		}
		if(!maxOccurs.equals("")) {
			element.setMaxOccurs(maxOccurs);
		}
		stack.push(element);		
	}
}
