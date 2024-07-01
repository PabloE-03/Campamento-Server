package es.andujar.campamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.andujar.campamento.models.jpa.Children;

public interface IChildrenRepository extends JpaRepository<Children, String>
{

}
