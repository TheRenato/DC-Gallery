package se.opazoweb.DCGallery.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.opazoweb.DCGallery.model.DcChannel;

@Repository
public interface DcChannelRepo extends CrudRepository<DcChannel, String> {
}
