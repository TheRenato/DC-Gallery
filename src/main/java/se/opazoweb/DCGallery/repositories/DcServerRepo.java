package se.opazoweb.DCGallery.repositories;

import org.springframework.data.repository.CrudRepository;
import se.opazoweb.DCGallery.model.DcServer;

public interface DcServerRepo extends CrudRepository<DcServer, String> {
}
