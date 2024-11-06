package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;

import java.util.List;


public interface MachineService {

    Machine getById(Integer id) ;
    MachineDTO createMachine(MachineDTO machineDTO);
    List<Machine> getAll(String username) ;
    void deleteById(Integer id);
//    Machine buyMachine(Integer id);
    MachineDTO testMachine(MachineDTO machineDTO);
}
