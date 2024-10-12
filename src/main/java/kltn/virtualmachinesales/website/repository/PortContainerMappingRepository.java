package kltn.virtualmachinesales.website.repository;

import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PortContainerMappingRepository extends JpaRepository<PortContainerMapping, Integer> {

    @Query("select p.containerName from PortContainerMapping p where p.port = :port")
    String findContainerNameByPort(int port);
}
