package com.proyecto.util;

import com.hp.hpl.jena.reasoner.rulesys.Rule;

/**
 * La clase que nos permite validar distintos campos dentro de la aplicaci�n.
 * 
 * @author Guillermo Mazzali
 * @version 1.0
 */
public class Validator {

	/**
	 * La funci�n encargada de validar una descripci�n de un elemento dentro del sistema.
	 * 
	 * @param description
	 *            La descripci�n que queremos validar.
	 * @return TRUE en caso de que la descripci�n este acorde a los par�metros establecidos, en caso contrario retornar FALSE.
	 */
	public static Boolean descriptionValidator(String description) {
		String pattern = "[\\sa-z�������0-9]+";
		return description == null ? false : description.toLowerCase().trim().matches(pattern);
	}

	/**
	 * La funci�n encargada de validar una regla dentro del sistema.
	 * 
	 * @param rule
	 *            La regla que queremos validar.
	 * @return TRUE en caso de que la regla este acorde a los par�metros establecidos, en caso contrario retornar FALSE.
	 */
	public static Boolean ruleValidator(String rule) {
		Boolean valid = null;
		try {
			Rule.parseRule(rule);
			valid = true;
		} catch (Exception e) {
			e.printStackTrace();
			valid = false;
		}
		return valid;
	}
}
