package kltn.virtualmachinesales.website.controller;


import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MachineController {
    @Autowired
    private MachineService machineService;

    @PostMapping("/machine/create")
    public ResponseEntity<DefaultResponse<MachineDTO>> create(@RequestBody MachineDTO machineDTO ) {
        MachineDTO machineDTO1 =  machineService.createMachine(machineDTO);
        return DefaultResponse.success("Group đã được tạo thành công", machineDTO1);

    }
    @GetMapping("/machine/{id}")
    public ResponseEntity<DefaultResponse<Machine>> getById(@PathVariable Integer id) {
        Machine machine = machineService.getById(id);
        return DefaultResponse.success(machine);
    }
    @DeleteMapping("machine/{id}")
    public ResponseEntity<DefaultResponse<Object>> delete(@PathVariable Integer id) {
        machineService.deleteById(id);
        return DefaultResponse.success("đã xóa thành công ", null);
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
