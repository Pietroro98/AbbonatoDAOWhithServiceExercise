package it.prova.service;

import it.prova.dao.abbonato.AbbonatoDAOImpl;
import it.prova.service.abbonato.AbbonatoService;
import it.prova.service.abbonato.AbbonatoServiceImpl;

public class MyServiceFactory {
	
	public static AbbonatoService getAbbonatoServiceImpl() {
		AbbonatoService abbonatoService = new AbbonatoServiceImpl();
		abbonatoService.setAbbonatoDao(new AbbonatoDAOImpl());
		return abbonatoService;
	}

}
