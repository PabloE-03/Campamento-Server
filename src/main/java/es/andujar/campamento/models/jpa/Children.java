package es.andujar.campamento.models.jpa;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "childrens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Children implements Serializable
{
	/**Numero de la seguridad social del niño */
	@Id
	@Column(name = "numero_ss")
	private String numeroSS;
	
	/**Nombre del niño */
	@Column
	private String nombre;
	
	/**Apellido del niño */
	@Column
	private String apellido;
	
	/**Lista de enfermedades del niño*/
	@Column
	private List<String> enfermedades;
}
