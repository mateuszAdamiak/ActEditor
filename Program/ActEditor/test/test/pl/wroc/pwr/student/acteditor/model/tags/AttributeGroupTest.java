package test.pl.wroc.pwr.student.acteditor.model.tags;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.wroc.pwr.student.acteditor.model.tags.Attribute;
import pl.wroc.pwr.student.acteditor.model.tags.AttributeGroup;
import pl.wroc.pwr.student.acteditor.model.tags.SimpleAttribute;
import pl.wroc.pwr.student.acteditor.model.tags.SimpleType;

public class AttributeGroupTest {
	private Attribute attribute;

	@Before
	public void setUp() throws Exception {
		attribute = new AttributeGroup();
	}

	@Test
	public void settingAndGettingTest() {
		attribute.setDefault("default");
		attribute.setDescription("description");
		attribute.setName("name");
		SimpleType t = new SimpleType();
		t.setName("simpleType");
		attribute.setSimpleType(t);
		attribute.setType("type");
		attribute.setUse("use");
		
		assertNotSame("default", attribute.getDefault());
		assertNull(attribute.getDefault());
		assertEquals("description", attribute.getDescription());
		assertEquals("name", attribute.getName());
		assertNotSame(t, attribute.getSimpleType());
		assertNull(attribute.getSimpleType());
		assertEquals("type", attribute.getType());
		assertEquals("use", attribute.getUse());
	}
	
	public void addingAttributeTest() {
		Attribute a = new SimpleAttribute();
		a.setName("name");
		
		attribute.add(a);
		
		assertTrue(attribute.getAttributes().size() == 1);
		assertEquals(a, attribute.getAttributes().get(0));
	}

}
