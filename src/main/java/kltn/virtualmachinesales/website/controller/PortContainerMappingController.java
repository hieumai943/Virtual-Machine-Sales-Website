package kltn.virtualmachinesales.website.controller;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.http.DefaultListResponse;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.request.ChangeBgrRequest;
import kltn.virtualmachinesales.website.service.PortContainerMappingService;
import kltn.virtualmachinesales.website.service.implement.MachineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin()
public class PortContainerMappingController {

    @Autowired
    private PortContainerMappingService portContainerMappingService;
    @Autowired
    private MachineServiceImpl machineServiceImpl;

    @PostMapping("/port-container/create")
    public ResponseEntity<DefaultResponse<PortContainerMapping>> create(@RequestBody PortContainerMapping portContainerMapping ) {
        PortContainerMapping portContainerMappingOutput =  portContainerMappingService.create(portContainerMapping);
        return DefaultResponse.success("Port - Container đã được tạo thành công", portContainerMappingOutput);

    }

    @GetMapping("/admin/container/list")
    public ResponseEntity<DefaultListResponse<PortContainerMapping>> listContainer( ) {
        List<PortContainerMapping> portContainerMappingOutputs =  portContainerMappingService.listContainer();
        Long portCounts = (long) portContainerMappingOutputs.size();
        return DefaultListResponse.success(portContainerMappingOutputs, portCounts);

    }
    @GetMapping("/admin/container/get/{id}")
    public ResponseEntity<DefaultResponse<PortContainerMapping>> findContainerById(@PathVariable Integer id) {
        PortContainerMapping portContainerMappingOutputs =  portContainerMappingService.getById(id);
        return DefaultResponse.success(portContainerMappingOutputs);
    }

    @PostMapping("/admin/render-image/{port}")
    public ResponseEntity<DefaultResponse<Machine>> renderImage(@RequestBody MultipartFile file, @PathVariable Integer port) {
        ChangeBgrRequest changeBgrRequest = new ChangeBgrRequest();
        changeBgrRequest.setPort(port);
        changeBgrRequest.setFile(file);
        Machine machine = machineServiceImpl.renderImage(changeBgrRequest);
        return DefaultResponse.success(machine);
    }

}
