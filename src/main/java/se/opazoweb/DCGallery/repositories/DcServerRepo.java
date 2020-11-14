package se.opazoweb.DCGallery.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.opazoweb.DCGallery.model.DcServer;

@Repository
public interface DcServerRepo extends CrudRepository<DcServer, String> {
}
