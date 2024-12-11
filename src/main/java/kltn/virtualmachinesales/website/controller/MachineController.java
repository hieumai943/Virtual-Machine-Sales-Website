package kltn.virtualmachinesales.website.controller;


import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.dto.MachinePortDTO;
import kltn.virtualmachinesales.website.dto.response.MachineDto;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.http.DefaultListResponse;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.request.AuthRequest;
import kltn.virtualmachinesales.website.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import java.util.List;

@Controller
@CrossOrigin()
public class MachineController {
    @Autowired
    private MachineService machineService;

    @PostMapping("/shop/machine/create")
    public ResponseEntity<DefaultResponse<MachineDTO>> create(@RequestBody MachineDTO machineDTO ) {
        MachineDTO machineDTO1 =  machineService.createMachine(machineDTO);
        return DefaultResponse.success("Group đã được tạo thành công", machineDTO1);

    }

    @PostMapping("/shop/machine/auth")
    public ResponseEntity<DefaultResponse<String>> MachineAuth(@RequestBody AuthRequest authRequest) {
        return DefaultResponse.success("Tai khoan đã được tạo thành công",  machineService.auth(authRequest));

    }

    @GetMapping("/shop/machine/{id}")
    public ResponseEntity<DefaultResponse<Machine>> getById(@PathVariable Integer id) {
        Machine machine = machineService.getById(id);
        return DefaultResponse.success(machine);
    }
    @DeleteMapping("/shop/machine/{id}")
    public ResponseEntity<DefaultResponse<Object>> delete(@PathVariable Integer id) {
        machineService.deleteById(id);
        return DefaultResponse.success("đã xóa thành công ", null);
    }
    @GetMapping("/shop/machine/list")
    public ResponseEntity<DefaultListResponse<MachineDto>> getAllMachine(@RequestParam Boolean isSampleStore,@RequestParam String username) {
        List<MachineDto> machines = machineService.getAll( isSampleStore,username);
        return DefaultListResponse.success(machines, machines.stream().count());
    }
    @GetMapping("/shop/machine/port/{port}")
    public ResponseEntity<DefaultResponse<MachinePortDTO>> getMachineByPort(@PathVariable Integer port) {
        MachinePortDTO machine = machineService.getMachineByPort( port);
        return DefaultResponse.success(machine);
    }
    // API mua don hang do
//    @PostMapping("machine/{id}")
//    public ResponseEntity<DefaultResponse<Machine>> buyMachine(@PathVariable Integer id) {
//        Machine machine = machineService.buyMachine(id);
//        return DefaultResponse.success("Đã bán thành công", machine);
//    }

    @PostMapping("/test-machine")
    public ResponseEntity<DefaultResponse<MachineDTO>> test(@RequestBody MachineDTO machineDTO ) {
        MachineDTO machineDTO1 =  machineService.testMachine(machineDTO);
        return DefaultResponse.success("Group đã được tạo thành công", machineDTO1);

    }

}
