package es.andujar.campamento.rest;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import es.andujar.campamento.exception.UserException;
import es.andujar.campamento.models.jpa.Children;
import es.andujar.campamento.models.jpa.Monitor;
import es.andujar.campamento.models.jpa.MonitorChildren;
import es.andujar.campamento.repository.IChildrenRepository;
import es.andujar.campamento.repository.IMonitorChildrenRepository;
import es.andujar.campamento.repository.IMonitorRepository;
import es.andujar.campamento.utils.CampamentoUtils;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(value="/campamento",produces="application/json")
public class CampamentoRest 
{
	@Autowired
	private IMonitorRepository monitorRepo;
	
	@Autowired
	private IChildrenRepository childrenRepo;
	
	@Autowired
	private IMonitorChildrenRepository childrenAssignedRepo;
	
	private static Logger log = LogManager.getLogger();
	
	@RequestMapping(method = RequestMethod.POST, value="/login",consumes = "application/json")
	public ResponseEntity<?> login(@RequestHeader(name="email",required = false) String email,
								@RequestHeader(name="passwd",required = false) String password)
	{
		try
		{
			Optional<Monitor> rawMonitor = this.monitorRepo.findById(email);
			Monitor monitor = rawMonitor.get();
			if(monitor==null || !monitor.getToken().equals(password))
			{
				throw new UserException("El usuario o la contraseña son incorrectos",401);
			}
			
			return ResponseEntity.ok().body(monitor);
			//169 173
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/get-all/childrens")
	public ResponseEntity<?> getAllChildrens()
	{
		try
		{
			List<Children> childrens = new LinkedList<Children>();
			childrens = this.childrenRepo.findAll();
			return ResponseEntity.ok().body(childrens);
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/get-all/monitores")
	public ResponseEntity<?> getAllMonitores()
	{
		try
		{
			List<Monitor> monitores = new LinkedList<Monitor>();
			monitores = this.monitorRepo.findAll();
			return ResponseEntity.ok().body(monitores);
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/get/not-assigned")
	public ResponseEntity<?> getNotAssignedChilds()
	{
		try
		{
			List<Children> childrens = this.childrenRepo.findAll();
			List<MonitorChildren> childAssigned = this.childrenAssignedRepo.findAll();
			List<Children> response = new LinkedList<Children>();
			
			if(childAssigned.isEmpty())
			{
				response = childrens;
			}
			else
			{
				for(Children child:childrens)
				{
					boolean add = true;
					int index = 0;
					while(index<childAssigned.size() && add)
					{
						MonitorChildren asign = childAssigned.get(index);
						
						if(asign.getMonitorChildrenId().getNumeroSS().equals(child.getNumeroSS()))
						{
							add = false;
						}
						index++;
					}
					if(add)
					{
						response.add(child);
					}
				}
			}
			
			return ResponseEntity.ok().body(response);
			
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}

	@RequestMapping(method = RequestMethod.GET,value = "/get/assigned")
	public ResponseEntity<?> getAssignedChilds(@RequestHeader(name = "monitor",required=true)String monitorValue)
	{
		try
		{
			List<Children> childrens = this.childrenRepo.findAll();
			List<MonitorChildren> assigned = this.childrenAssignedRepo.findAll();
			List<Monitor> monitores = this.monitorRepo.findAll();
			Monitor monitor = null;
			List<Children> response = new LinkedList<Children>();
			
			int index = 0;
			boolean find = false;
			while(index<monitores.size() && !find)
			{
				Monitor item = monitores.get(index);
				
				if(item.getNombre().equals(monitorValue))
				{
					monitor = item;
					find = true;
				}
				
				index++;
			}
			
			if(monitor==null)
			{
				throw new UserException("El monitor seleccionado no existe",404);
			}
			
			if(assigned.size()>0)
			{
				for(Children child:childrens)
				{
					index = 0;
					boolean add = false;
					while(index<assigned.size() && !add)
					{
						MonitorChildren item = assigned.get(index);
						if(item.getMonitorChildrenId().getNumeroSS().equals(child.getNumeroSS())
								&& item.getMonitorChildrenId().getEmail().equals(monitor.getEmail()))
						{
							response.add(child);
							add=true;
						}
						index++;
					}
				}
			}
			
			return ResponseEntity.ok().body(response);
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,value="/asignar/children",consumes = "application/json")
	public ResponseEntity<?> asignarChildren (@RequestHeader(name="monitor",required=true) String monitor,
											  @RequestHeader(name="childrenName",required=true) String childrenName,
											  @RequestHeader(name="childrenSurname",required=true)String childrenSurname)
	{
		try
		{
			List<Monitor> monitores = this.monitorRepo.findAll();
			List<Children> childrens = this.childrenRepo.findAll();
			CampamentoUtils.asignar(monitor, childrenName,childrenSurname, monitores, childrens, childrenAssignedRepo);
			return ResponseEntity.ok().build();
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE,value="/quitar-asignacion/children",consumes = "application/json")
	public ResponseEntity<?> quitarAsignacionChildren (@RequestHeader(name="monitor",required=true) String monitor,
											  @RequestHeader(name="childrenName",required=true) String childrenName,
											  @RequestHeader(name="childrenSurname",required=true)String childrenSurname)
	{
		try
		{
			List<Monitor> monitores = this.monitorRepo.findAll();
			List<Children> childrens = this.childrenRepo.findAll();
			CampamentoUtils.desAsignar(monitor, childrenName,childrenSurname, monitores, childrens, childrenAssignedRepo);
			return ResponseEntity.ok().build();
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
			
	@RequestMapping(method = RequestMethod.POST,value = "/insert/child",consumes = "application/json")
	public ResponseEntity<?> insertarChildren(@RequestBody(required=true) Children child)
	{
		try
		{
			List<Children> childrens = this.childrenRepo.findAll();
			
			for(Children item:childrens)
			{
				if(child.getNumeroSS().equals(item.getNumeroSS()))
				{
					throw new UserException("Objeto con id duplicado",406);
				}
			}
			
			this.childrenRepo.save(child);
			return ResponseEntity.ok().build();
			
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.PATCH,value = "/change/password",consumes = "application/json")
	public ResponseEntity<?> changePassword(@RequestHeader(name = "email",required = true) String email,
											@RequestHeader(name = "password",required = true) String passwd,
											@RequestHeader(name = "newPassword",required = true) String newPasswd)
	{
		try
		{
			Optional<Monitor> rawMonitor = this.monitorRepo.findById(email);
			if(rawMonitor.isEmpty())
			{
				throw new UserException("El monitor o la contraseña son erroneos",404);
			}
			
			Monitor monitor = rawMonitor.get();
			
			if(!monitor.getToken().equals(passwd))
			{
				throw new UserException("El monitor o la contraseña son erroneos",404);
			}
			
			monitor.setToken(newPasswd);
			this.monitorRepo.save(monitor);
			return ResponseEntity.ok().build();
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.PATCH,value = "/change/rol",consumes = "application/json")
	public ResponseEntity<?> changePassword(@RequestHeader(name = "name",required = true) String name,
											@RequestHeader(name = "newRol",required = true) String newRol)
	{
		try
		{
			List<Monitor> monitores = this.monitorRepo.findAll();
			Monitor monitor = null;
			for(Monitor item:monitores)
			{
				if(item.getNombre().equals(name))
				{
					monitor = item;
					break;
				}
			}
			
			if(monitor==null)
			{
				throw new UserException("No existe un monitor con nombre "+name,404);
			}
			
			monitor.setRol(newRol);
			this.monitorRepo.save(monitor);
			return ResponseEntity.ok().build();
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
	@RequestMapping(method = RequestMethod.PATCH,value = "/marcar/children",consumes="application/json")
	public ResponseEntity<?> markChildren(@RequestHeader(name="children",required=true)String children)
	{
		try
		{
			List<Children> childrens = this.childrenRepo.findAll();
			Children toPatch = null;
			boolean found = false;
			int index = 0;
			while(index<childrens.size() && !found)
			{
				Children item = childrens.get(index);
				String valueItem = item.getNombre()+" "+item.getApellido();
				if(valueItem.equals(children))
				{
					toPatch = item;
					found = true;
				}
				index++;
			}
			
			if(toPatch==null)
			{
				throw new UserException("El niño "+children+" no existe",404);
			}
			
			List<String> dias = toPatch.getDias();
			if(dias==null)
			{
				dias = new LinkedList<String>();
			}
			LocalDateTime now = LocalDateTime.now();
			String dayValue = now.getDayOfMonth()+"/"+now.getMonthValue()+"/"+now.getYear();
			dias.add(dayValue);
			toPatch.setDias(dias);
			this.childrenRepo.save(toPatch);
			return ResponseEntity.ok().build();
		}
		catch(UserException exception)
		{
			log.error(exception.getMessage(),exception);
			return ResponseEntity.status(exception.getCode()).body(exception.toMap());
		}
		catch(Exception exception)
		{
			String message = "Error de servidor";
			log.error(message,exception);
			UserException response = new UserException(500,message,exception);
			return ResponseEntity.status(500).body(response.toMap());
		}
	}
	
}
