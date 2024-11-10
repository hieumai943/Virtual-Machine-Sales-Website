package kltn.virtualmachinesales.website.repository;

import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortContainerMappingRepository extends JpaRepository<PortContainerMapping, Integer> {

    @Query("select p.containerName from PortContainerMapping p where p.port = :port")
    String findContainerNameByPort(int port);

    @Query("select p.port from PortContainerMapping p")
    List<Integer> findAllPorts();

    @Query("select p.port from PortContainerMapping p where p.machineId = :machineId")
    List<Integer> findPortByMachineId(Integer machineId);

    @Query("select p from PortContainerMapping p where p.machineId = :machineId")
    List<PortContainerMapping> findAllByMachineId(Integer machineId);
}
