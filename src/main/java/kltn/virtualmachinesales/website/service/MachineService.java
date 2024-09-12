package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;


public interface MachineService {

    Machine getById(Integer id) ;
    MachineDTO createMachine(MachineDTO machineDTO);
    void deleteById(Integer id);
    Machine buyMachine(Integer id);
    MachineDTO testMachine(MachineDTO machineDTO);
}
