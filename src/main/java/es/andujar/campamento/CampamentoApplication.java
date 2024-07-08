package es.andujar.campamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import es.andujar.campamento.utils.CargaMonitores;

@SpringBootApplication
@ComponentScan( basePackages = "es.andujar.campamento")
public class CampamentoApplication implements CommandLineRunner
{
	@Autowired
	private CargaMonitores cargaMonitores;
	
	public static void main(String[] args) 
	{
		SpringApplication.run(CampamentoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception 
	{
		cargaMonitores.cargarMonitores();
		//cargaMonitores.cargarChildrens();
	}
	

}
