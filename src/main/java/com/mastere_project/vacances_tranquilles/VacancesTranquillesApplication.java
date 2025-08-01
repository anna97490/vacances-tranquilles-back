package com.mastere_project.vacances_tranquilles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'application Vacances Tranquilles.
 * Lance l'application Spring Boot.
 */
@SpringBootApplication
public class VacancesTranquillesApplication {

	/**
	 * Méthode principale qui démarre l'application Spring Boot.
	 * 
	 * @param args arguments de la ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(VacancesTranquillesApplication.class, args);
	}
}