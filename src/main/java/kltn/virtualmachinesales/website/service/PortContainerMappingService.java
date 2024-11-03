package kltn.virtualmachinesales.website.service;


import kltn.virtualmachinesales.website.entity.PortContainerMapping;

import java.util.List;

public interface PortContainerMappingService {
     PortContainerMapping create(PortContainerMapping portContainerMapping);
     List<PortContainerMapping> listContainer();
}
