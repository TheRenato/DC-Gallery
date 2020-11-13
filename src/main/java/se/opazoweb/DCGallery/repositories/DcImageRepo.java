package se.opazoweb.DCGallery.repositories;

import org.springframework.data.repository.CrudRepository;
import se.opazoweb.DCGallery.model.DcImage;

public interface DcImageRepo extends CrudRepository<DcImage, String> {
}
