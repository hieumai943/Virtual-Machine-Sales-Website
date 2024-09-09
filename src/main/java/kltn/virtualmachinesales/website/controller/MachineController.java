package kltn.virtualmachinesales.website.controller;


import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MachineController {
    @Autowired
    private MachineService machineService;

    @PostMapping("/insert-machine")
    public ResponseEntity<DefaultResponse<MachineDTO>> create(@RequestBody MachineDTO machineDTO ) {
        MachineDTO machineDTO1 =  machineService.createMachine(machineDTO);
        return DefaultResponse.success("Group đã được tạo thành công", machineDTO1);

    }
    @PostMapping("/test-machine")
    public ResponseEntity<DefaultResponse<MachineDTO>> test(@RequestBody MachineDTO machineDTO ) {
        MachineDTO machineDTO1 =  machineService.testMachine(machineDTO);
        return DefaultResponse.success("Group đã được tạo thành công", machineDTO1);

    }

}
