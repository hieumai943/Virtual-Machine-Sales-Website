package kltn.virtualmachinesales.website.controller;

import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.http.DefaultListResponse;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.PortContainerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class PortContainerMappingController {

    @Autowired
    private PortContainerMappingService portContainerMappingService;

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

}
