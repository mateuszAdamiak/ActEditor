package pl.wroc.pwr.student.acteditor.model;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.student.acteditor.model.tags.Attribute;

/**
 * Zapewnia dostep do rejestru atrybutow.
 * 
 * Rejestr zawiera atrybuty Schematu XML, ktore sa identyfikowane po nazwie
 * atrybutu.
 * 
 * @author Mateusz
 * 
 */
public class AttributeRegistry {
	private static AttributeRegistry registry;
	private List attributes;

	private AttributeRegistry() {
		attributes = new ArrayList();
	}

	/**
	 * Zwraca referencje do rejestru atrybut�w.
	 * 
	 * @return Referencja do rejestru atrybut�w.
	 */
	public static AttributeRegistry getRegistry() {
		if (registry == null) {
			registry = new AttributeRegistry();
		}
		return registry;
	}

	/**
	 * Dodaje atrybut do rejestru, jesli atrybut o takiej nazwie nie istnieje
	 * jeszcze w rejestrze.
	 * 
	 * @param attribute
	 *          Atrybut, kt�ry ma zostac dodany do rejestru.
	 */
	public void add(Attribute attribute) {
		for (Object each : attributes) {
			if (((Attribute) each).getName().equals(attribute.getName())) {
				return;
			}
		}
		attributes.add(attribute);
	}

	/**
	 * Zwraca atrybut z rejestru o podanym indeksie.
	 * 
	 * @param index
	 *          Indeks atrybutu w rejestrze.
	 * @return Atrybut o podanym indeksie.
	 */
	public Attribute get(int index) {
		return (Attribute) attributes.get(index);
	}

	/**
	 * Zwraca atrybut z rejestru o podanej nazwie.
	 * 
	 * @param name
	 *          Nazwa atrybutu w rejestrze.
	 * @return Atrybut o podanej nazwie.
	 */
	public Attribute get(String name) {
		for (Object each : attributes) {
			if (((Attribute) each).getName().equals(name)) {
				return (Attribute) each;
			}
		}
		return null;
	}

	/**
	 * Zwraca liste atrybutow istniejacych w rejestrze.
	 * 
	 * @return Lista atrybut�w.
	 */
	public List getAttributes() {
		return attributes;
	}
}
