package com.proyecto.ontology.rdf.option.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Statement;
import com.proyecto.annotation.RdfService;
import com.proyecto.model.option.Option;
import com.proyecto.ontology.OntologyConstants;
import com.proyecto.ontology.rdf.ProyectoRdfImpl;
import com.proyecto.ontology.rdf.answer.TrueFalseAnswerRdf;
import com.proyecto.ontology.rdf.option.OptionRdf;

/**
 * La clase que implementa la interfaz que define el comportamiento de las opciones dentro de la ontolog�a.
 * 
 * @author Guillermo Mazzali
 * @version 1.0
 * 
 * @param <O>
 *            La clase de opci�n que vamos a manejar dentro de la ontolog�a.
 */
@RdfService("OptionRdf")
public class OptionRdfImpl<O extends Option> extends ProyectoRdfImpl<O> implements OptionRdf<O> {

	private static final long serialVersionUID = -8539107768475990485L;

	/**
	 * El servicio para las respuestas.
	 */
	@Autowired
	private TrueFalseAnswerRdf trueFalseAnswerRdf;

	/**
	 * La clase de opci�n.
	 */
	private OntClass optionClass;
	/**
	 * Las relaciones de la clase de opciones.
	 */
	private DatatypeProperty haveDescription;
	private ObjectProperty haveAnswer;

	@Override
	public OntClass initClass(OntModel ontology) {
		// Creamos la clase si es nula.
		String optionClassName = this.namespace + OntologyConstants.ClassName.OPTION;
		this.optionClass = ontology.getOntClass(optionClassName);
		if (this.optionClass == null) {
			this.optionClass = ontology.createClass(optionClassName);
		}

		// Creamos las relaciones.
		String description = this.namespace + OntologyConstants.PropertyName.OPTION_HAS_DESCRIPTION;
		this.haveDescription = ontology.getDatatypeProperty(description);
		if (this.haveDescription == null) {
			this.haveDescription = ontology.createDatatypeProperty(description);
		}

		String answer = this.namespace + OntologyConstants.PropertyName.OPTION_HAS_ANSWER;
		this.haveAnswer = ontology.getObjectProperty(answer);
		if (this.haveAnswer == null) {
			this.haveAnswer = ontology.createObjectProperty(answer);
			this.haveAnswer.setDomain(this.optionClass);
			this.haveAnswer.setRange(this.trueFalseAnswerRdf.initClass(ontology));
		}

		return this.optionClass;
	}

	@Override
	public Individual loadEntityData(OntModel ontology, Individual individual, O entity) {
		// Creamos los literales.
		Literal description = ontology.createTypedLiteral(entity.getDescription(), XSDDatatype.XSDstring);

		// Creamos las carga de los datos.
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(ontology.createLiteralStatement(individual, this.haveDescription, description));
		statements.add(ontology.createLiteralStatement(individual, this.haveAnswer,
				this.trueFalseAnswerRdf.createIndividual(ontology, entity.getTrueFalseAnswer())));

		ontology.add(statements);

		return individual;
	}
}