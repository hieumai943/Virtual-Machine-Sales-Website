package kltn.virtualmachinesales.website.repository;

import kltn.virtualmachinesales.website.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
        public interface MachineRepository extends JpaRepository<Machine, Integer> {
        @Query("SELECT m from Machine m WHERE m.name = :name")
        Machine getByName(String name);
}
