package se.opazoweb.DCGallery.repositories;

import org.springframework.data.repository.CrudRepository;
import se.opazoweb.DCGallery.model.DcChannel;

public interface DcChannelRepo extends CrudRepository<DcChannel, String> {
}
