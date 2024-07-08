package es.andujar.campamento.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.andujar.campamento.exception.UserException;
import es.andujar.campamento.models.jpa.Children;
import es.andujar.campamento.models.jpa.Monitor;
import es.andujar.campamento.repository.IChildrenRepository;
import es.andujar.campamento.repository.IMonitorRepository;

@Service
public class CargaMonitores 
{
	@Autowired
	private IMonitorRepository monitorRepo;
	
	@Autowired
	private IChildrenRepository childrenRepo;
	
	private static Logger log = LogManager.getLogger();
	
	public void cargarMonitores()
	{
		List<Monitor> monitores = new LinkedList<Monitor>();
		monitores.add(new Monitor("peibol.max@gmail.com","Pablo_64020","Pablo","Administrador"));
		monitores.add(new Monitor("noelia.momu@gmail.com","Noe_14030","Noelia","Moderador"));
		monitores.add(new Monitor("alvarez.botella@gmail.com","Lola_12978","Lola","Monitor"));
		monitores.add(new Monitor("larubita15vs@gmail.com","Silvia_36820","Silvia","Moderador"));
		monitores.add(new Monitor("lorluqcab@gmail.com","Lorena_42120","Lorena","Moderador"));
		monitorRepo.saveAll(monitores);
		
	}
	
	public void cargarChildrens()throws UserException
	{
		File file = new File("./src/main/resources/childrens.csv");
		FileReader fr = null;
		BufferedReader br = null;
		
		try
		{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String linea = br.readLine();
			if(!linea.equals("numeross;nombre;apellido;enfermedades"))
			{
				if(br!=null)
				{
					br.close();
				}
				if(fr!=null)
				{
					fr.close();
				}
				log.error("El fichero no es correcto");
				throw new UserException("El fichero entrgado con los niños no es correcto", 406);
			}
			
			linea = br.readLine();
			while(linea!=null)
			{
				String [] spliter = linea.split(";");
				String [] rawEnfermedades = spliter[3].split(",");
				
				List<String> enfermedades = new LinkedList<String>();
				for(String enfermedad:rawEnfermedades)
				{
					enfermedades.add(enfermedad);
				}
				
				Children children = new Children(spliter[0],spliter[1],spliter[2],enfermedades,new LinkedList<>());
				
				childrenRepo.save(children);
				linea = br.readLine();
			}
			
		}
		catch(IOException ex)
		{
			log.error("Error al cargar los niños",ex);
			throw new UserException(400,"No se pudo cargar los niños correctamente",ex);
		}
		finally 
		{
			try
			{
				if(br!=null)
				{
					br.close();
				}
			}
			catch(IOException ex)
			{
				
			}
			
			try {
				if(fr!=null)
				{
					fr.close();
				}
			} 
			catch (IOException e) 
			{	
				
			}
		}
	}
}
