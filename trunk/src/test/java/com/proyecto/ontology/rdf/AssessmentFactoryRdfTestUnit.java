package com.proyecto.ontology.rdf;

import java.io.FileOutputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.common.util.holder.HolderApplicationContext;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.proyecto.CreateExampleMaterial;
import com.proyecto.model.material.activity.Activity;
import com.proyecto.model.material.assessment.Assessment;
import com.proyecto.model.material.reactive.Reactive;
import com.proyecto.ontology.rdf.material.assessment.factory.AssessmentFactoryRdf;

/**
 * La clase de prueba para la factor�a de las evaluaciones en la ontolog�a.
 * 
 * @author Guillermo Mazzali
 * @version 1.0
 */
public class AssessmentFactoryRdfTestUnit {

	/**
	 * Antes de que arranque la ejecuci�n de la clase, cargamos el dao.
	 */
	@BeforeClass
	public static void beforeClass() {
		String[] files = { "/com/proyecto/spring/general-application-context.xml" };
		HolderApplicationContext.initApplicationContext(files);
	}

	/**
	 * Al finalizar dejamos un espacio en blanco en la consola.
	 */
	@AfterClass
	public static void afterClass() {
		System.out.println();
	}

	/**
	 * Las pruebas sobre la factor�a de las actividades.
	 */
	@Test
	public void pruebaDeLaFactoriaDeLasActividades() {

		System.out.println("######################################################################");
		System.out.println("############### FACTOR�A DE EVALUACIONES EN ONTOLOG�AS ###############");
		System.out.println("######################################################################");

		Reactive reactive1 = CreateExampleMaterial.createReactive(10, CreateExampleMaterial.createInstrumentRestrictedEssayActivity(10));
		Reactive reactive2 = CreateExampleMaterial.createReactive(20, CreateExampleMaterial.createInstrumentUnrestrictedEssayActivity(20));
		Activity activity1 = CreateExampleMaterial.createActivity(10, reactive1, reactive2);
		activity1.setDescription("essay activity");

		Reactive reactive3 = CreateExampleMaterial.createReactive(30, CreateExampleMaterial.createInstrumentSingleChoice(30));
		Reactive reactive4 = CreateExampleMaterial.createReactive(40, CreateExampleMaterial.createInstrumentMultipleChoice(40));
		Reactive reactive5 = CreateExampleMaterial.createReactive(50, CreateExampleMaterial.createInstrumentCompletion(50));
		Reactive reactive6 = CreateExampleMaterial.createReactive(60, CreateExampleMaterial.createInstrumentCorrespondence(60));
		Activity activity2 = CreateExampleMaterial.createActivity(20, reactive3, reactive4, reactive5, reactive6);
		activity2.setDescription("objective activity");

		Assessment assessment = CreateExampleMaterial.createAssessment(10, activity1, activity2);

		OntModel ontology = ModelFactory.createOntologyModel();

		HolderApplicationContext.getBean(AssessmentFactoryRdf.class).loadEntityToOntology(ontology, assessment);

		ontology.write(System.out);

		try {
			String archivo = System.getProperty("proyecto.configuration.dir") + "/ontology.rdf";
			FileOutputStream salida = new FileOutputStream(archivo);
			ontology.write(salida);
			salida.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
