package es.andujar.campamento.models.jpa.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MonitorChildrenId 
{
	/**Id que referencia al monitor */
	private String email;
	
	/**Id que referencia al niño */
	private String numeroSS;
}
