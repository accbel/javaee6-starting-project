package br.accbel.javaee6sp.service;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jboss.seam.solder.core.ExtensionManaged;

public class EntityManagerProducer {
	
	@ExtensionManaged
	@Produces
	@PersistenceUnit(unitName="javaee6sp")
	@ConversationScoped
	EntityManagerFactory producerField;
	
}
