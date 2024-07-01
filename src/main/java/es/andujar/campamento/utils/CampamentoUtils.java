package es.andujar.campamento.utils;

import java.util.List;

import es.andujar.campamento.exception.UserException;
import es.andujar.campamento.models.jpa.Children;
import es.andujar.campamento.models.jpa.Monitor;
import es.andujar.campamento.models.jpa.MonitorChildren;
import es.andujar.campamento.models.jpa.id.MonitorChildrenId;
import es.andujar.campamento.repository.IMonitorChildrenRepository;

public class CampamentoUtils 
{
	public static void asignar(String valueMonitor,String valueChildrenName,String valueChildrenSurname,List<Monitor> monitores,List<Children> childrens,IMonitorChildrenRepository asignacionRepo) throws UserException
	{
		int index = 0;
		boolean find = false;
		Monitor monitor = null;
		Children children = null;
		
		while(index<monitores.size() && !find)
		{
			Monitor item = monitores.get(index); 
			if(item.getNombre().equals(valueMonitor))
			{
				monitor = item;
				find = true;
			}
			index++;
		}
		
		index = 0;
		find = false;
		
		while(index<childrens.size() && !find)
		{
			Children item = childrens.get(index); 
			if(item.getNombre().equals(valueChildrenName) && item.getApellido().equals(valueChildrenSurname))
			{
				children = item;
				find = true;
			}
			index++;
		}
		
		if(monitor==null || children==null)
		{
			throw new UserException("Se ha introducido un monitor o un niño que no existe",404);
		}
		
		MonitorChildren asignacion = new MonitorChildren(new MonitorChildrenId(monitor.getEmail(),children.getNumeroSS()), monitor, children);
		asignacionRepo.save(asignacion);
	}
	
	public static void desAsignar(String valueMonitor,String valueChildrenName,String valueChildrenSurname,List<Monitor> monitores,List<Children> childrens,IMonitorChildrenRepository asignacionRepo) throws UserException
	{
		int index = 0;
		boolean find = false;
		Monitor monitor = null;
		MonitorChildren childrenAsignado = null;
		List<MonitorChildren> asignaciones = asignacionRepo.findAll();
		
		while(index<monitores.size() && !find)
		{
			Monitor item = monitores.get(index); 
			if(item.getNombre().equals(valueMonitor))
			{
				monitor = item;
				find = true;
			}
			index++;
		}
		
		index = 0;
		find = false;
		
		while(index<childrens.size() && !find)
		{
			Children item = childrens.get(index);
			
			for(MonitorChildren asignacion:asignaciones)
			{
				if(item.getNumeroSS().equals(asignacion.getMonitorChildrenId().getNumeroSS()))
				{
					childrenAsignado = asignacion;
					find = true;
				}
			}
			
			index++;
		}
		
		
		if(monitor==null || childrenAsignado==null)
		{
			throw new UserException("Se ha introducido un monitor o un niño que no existe",404);
		}
		
		asignacionRepo.delete(childrenAsignado);
	}
}
