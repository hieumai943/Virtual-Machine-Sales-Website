package kltn.virtualmachinesales.website.service.implement;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.repository.MachineRepository;
import kltn.virtualmachinesales.website.repository.UserRepository;
import kltn.virtualmachinesales.website.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private UserRepository userRepository;

    public Machine getById(Integer id){
        Machine machine = machineRepository.findById(id).orElse(null);
        return machine;
    }
    public MachineDTO createMachine(MachineDTO machineDTO){
        Machine machine = new Machine();
        machine.setName(machineDTO.getName());
        machine.setDescription(machineDTO.getDescription());
        machine.setRam(machineDTO.getRam());
        machine.setCoreCpu(machineDTO.getCoreCpu());
        machine.setMemory(machineDTO.getMemory());
        machine.setImgSrc(machineDTO.getImgSrc());
        machine.setIsSample(Boolean.FALSE);
        machine.setOldPrice(machineDTO.getOldPrice());
        machine.setUserId(machineDTO.getUserId());
        machine.setStatus(Boolean.FALSE);
        Double newPrice = Integer.valueOf(machine.getCoreCpu()) *5 + Integer.valueOf(machine.getRam()) + Integer.valueOf(machine.getMemory())*0.1;
        machine.setNewPrice(newPrice.toString() + '$');
        machine.setImgSrc("https://res.cloudinary.com/dlggnttqv/image/upload/v1726463019/machine_1_fgtptn.png");
        machineRepository.save(machine);
        return machineDTO;
    }
    public List<Machine> getAll(String username){
        User user = userRepository.findByUsername(username);
        List<Machine> machines = machineRepository.findAllByUserId(user.getId());
        List<Machine> sampleMachines = machineRepository.findAlllSample();
        machines.addAll(sampleMachines);
        return machines;
    }
    public void deleteById(Integer id){
        machineRepository.deleteById(id);
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
