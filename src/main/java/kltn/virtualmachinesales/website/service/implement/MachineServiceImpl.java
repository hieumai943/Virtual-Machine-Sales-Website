package kltn.virtualmachinesales.website.service.implement;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.repository.MachineRepository;
import kltn.virtualmachinesales.website.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineRepository machineRepository;

    public Machine getById(long id){
        return  null;
    }
    public MachineDTO createMachine(MachineDTO machineDTO){
        Machine machine = new Machine();
        machine.setName(machineDTO.getName());
        machine.setDescription(machineDTO.getDescription());
        machineRepository.save(machine);
        return machineDTO;
    }
    public MachineDTO testMachine(MachineDTO machineDTO){
        Machine machine = new Machine();
        machine= machineRepository.getByName(machineDTO.getName());
        MachineDTO machineDTO1 = new MachineDTO();
        machineDTO1.setName(machineDTO.getName());
        machineDTO1.setDescription(machineDTO.getDescription());
        machineDTO1.setId(machineDTO.getId());
        return machineDTO1;
    }

}
