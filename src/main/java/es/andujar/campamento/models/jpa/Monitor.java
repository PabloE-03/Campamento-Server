package es.andujar.campamento.models.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monitores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Monitor implements Serializable
{
	/**Email monitor */
	@Id
	@Column
	private String email;
	
	/**Token o password del monitor */
	@Column(unique = true)
	private String token;
	
	/**Nombre del monitor */
	@Column
	private String nombre;
	
	/**Rol o roles del monitor */
	@Column
	private String rol;
}
