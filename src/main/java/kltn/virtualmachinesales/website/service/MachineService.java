package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.dto.MachinePortDTO;
import kltn.virtualmachinesales.website.dto.response.MachineDto;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.request.AuthRequest;

import java.util.List;


public interface MachineService {

    Machine getById(Integer id) ;
    MachineDTO createMachine(MachineDTO machineDTO);
    String auth(AuthRequest authRequest);
    List<MachineDto> getAll(String username) ;
    void deleteById(Integer id);
//    Machine buyMachine(Integer id);
    MachineDTO testMachine(MachineDTO machineDTO);
    MachinePortDTO getMachineByPort(Integer port);
}
