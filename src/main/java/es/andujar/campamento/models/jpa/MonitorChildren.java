package es.andujar.campamento.models.jpa;

import es.andujar.campamento.models.jpa.id.MonitorChildrenId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "monitor_children")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorChildren 
{
	@EmbeddedId
	/**Id emmebido de la tabla */
	private MonitorChildrenId monitorChildrenId;
	
	@ManyToOne
	@MapsId("email")
	private Monitor email;
	
	@ManyToOne
	@MapsId("numeroSS")
	private Children numeroSS;
}
