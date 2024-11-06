package kltn.virtualmachinesales.website.service.implement;

import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.repository.PortContainerMappingRepository;
import kltn.virtualmachinesales.website.service.PortContainerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Port;
import java.util.List;

@Service
public class PortContainerMappingServiceImpl implements PortContainerMappingService {
    @Autowired
    private PortContainerMappingRepository portContainerMappingRepository;

    @Override
    public PortContainerMapping create(PortContainerMapping portContainerMapping){
         PortContainerMapping portContainerMapping1 =  portContainerMappingRepository.save(portContainerMapping);
//         portContainerMapping1.setPort(portContainerMapping1.getId() + 80);
         portContainerMapping1.setContainerName("nginx"+ portContainerMapping1.getPort());
         return portContainerMappingRepository.save(portContainerMapping1);

    }

    @Override
    public List<PortContainerMapping> listContainer() {
        List<PortContainerMapping> portContainerMappings = portContainerMappingRepository.findAll();
        return portContainerMappings;
    }
}
