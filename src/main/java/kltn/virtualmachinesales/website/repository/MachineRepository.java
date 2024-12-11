package kltn.virtualmachinesales.website.repository;

import kltn.virtualmachinesales.website.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
        public interface MachineRepository extends JpaRepository<Machine, Integer> {
        @Query("SELECT m from Machine m WHERE m.name = :name")
        Machine getByName(String name);

        @Query("SELECT m from Machine m where m.userId = :userId order by m.id desc")
        List<Machine> findAllByUserId(Integer userId);

        @Query("SELECT m from Machine m WHERE m.isSample = true")
        List<Machine> findAlllSample();

        @Query("SELECT m from Machine m WHERE m.userAuth = :auth and m.id = :machineId")
        Machine findByUserAuth(String auth, Integer machineId );


}
