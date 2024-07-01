package es.andujar.campamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.andujar.campamento.models.jpa.MonitorChildren;
import es.andujar.campamento.models.jpa.id.MonitorChildrenId;

public interface IMonitorChildrenRepository extends JpaRepository<MonitorChildren, MonitorChildrenId>
{

}
