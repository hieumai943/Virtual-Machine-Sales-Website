package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.dto.MachinePortDTO;
import kltn.virtualmachinesales.website.dto.response.MachineDto;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.request.AuthRequest;
import kltn.virtualmachinesales.website.request.ChangeBgrRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface MachineService {

    Machine getById(Integer id) ;
    MachineDTO createMachine(MachineDTO machineDTO);
    String auth(AuthRequest authRequest);
    List<MachineDto> getAll(Boolean isSampleStore, String username) ;
    void deleteById(Integer id);
//    Machine buyMachine(Integer id);
    MachineDTO testMachine(MachineDTO machineDTO);
    MachinePortDTO getMachineByPort(Integer port);
    Machine renderImage(ChangeBgrRequest request);
}
