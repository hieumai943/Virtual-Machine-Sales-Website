package kltn.virtualmachinesales.website.controller;

import kltn.virtualmachinesales.website.entity.Payment;
import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.PaymentService;
import kltn.virtualmachinesales.website.service.PortContainerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
