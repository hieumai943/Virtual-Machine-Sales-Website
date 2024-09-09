package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;


public interface MachineService {

    Machine getById(long id) ;
    MachineDTO createMachine(MachineDTO machineDTO);
    MachineDTO testMachine(MachineDTO machineDTO);
}
