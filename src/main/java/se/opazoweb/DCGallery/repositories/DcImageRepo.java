package se.opazoweb.DCGallery.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.opazoweb.DCGallery.model.DcImage;

@Repository
public interface DcImageRepo extends CrudRepository<DcImage, String> {
}
