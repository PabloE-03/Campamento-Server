package es.andujar.campamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.andujar.campamento.models.jpa.Monitor;

public interface IMonitorRepository extends JpaRepository<Monitor, String>
{
	
}
