package kltn.virtualmachinesales.website.service.implement;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.dto.MachinePortDTO;
import kltn.virtualmachinesales.website.dto.response.CloudinaryResponseDTO;
import kltn.virtualmachinesales.website.dto.response.MachineDto;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.repository.MachineRepository;
import kltn.virtualmachinesales.website.repository.PortContainerMappingRepository;
import kltn.virtualmachinesales.website.repository.UserRepository;
import kltn.virtualmachinesales.website.request.AuthRequest;
import kltn.virtualmachinesales.website.request.ChangeBgrRequest;
import kltn.virtualmachinesales.website.service.MachineService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortContainerMappingRepository portContainerMappingRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileUploadServiceImpl fileUploadServiceImpl;

    public Machine getById(Integer id){
        Machine machine = machineRepository.findById(id).orElse(null);
        return machine;
    }
    public MachineDTO createMachine(MachineDTO machineDTO){
        Machine machine = new Machine();
        machine.setName(machineDTO.getName());
        machine.setDescription(machineDTO.getDescription());
        machine.setRam(machineDTO.getRam());
        machine.setCoreCpu(machineDTO.getCoreCpu());
        machine.setMemory(machineDTO.getMemory());
        machine.setImgSrc(machineDTO.getImgSrc());
        machine.setIsSample(Boolean.FALSE);
        machine.setOldPrice(machineDTO.getOldPrice());
        machine.setUserId(machineDTO.getUserId());
        machine.setStatus(Boolean.FALSE);
        Double newPrice = Integer.valueOf(machine.getCoreCpu()) *5 + Integer.valueOf(machine.getRam()) + Integer.valueOf(machine.getMemory())*0.1;
        machine.setNewPrice(newPrice.toString() + '$');
        machine.setImgSrc("https://res.cloudinary.com/dlggnttqv/image/upload/v1726463019/machine_1_fgtptn.png");
        machineRepository.save(machine);
        return machineDTO;
    }

    @Override
    public Machine renderImage(ChangeBgrRequest file){
        if(Objects.isNull(file.getFile())){
            return null;
        }
        String fileName = file.getFile().getOriginalFilename();
        CloudinaryResponseDTO responseDTO = fileUploadServiceImpl.uploadFile( file.getFile(), fileName);
        String postImg = responseDTO.getUrl();
        Integer machineId = portContainerMappingRepository.getMachineByPort(file.getPort());
        Machine machine = machineRepository.findById(machineId).orElse(null);
        machine.setImgSrc(postImg);
        machineRepository.save(machine);
        return machine;
    }

    @Override
    public String auth(AuthRequest authRequest) {
        Machine machine = machineRepository.findByUserAuth(authRequest.getUsername(), authRequest.getMachineId());
        if(Objects.isNull(machine)){
            return null;
        }
        String authPassHash = DigestUtils.sha256Hex(authRequest.getPassword()).toUpperCase();
        if(authPassHash.equals(machine.getPassAuth())){
            return authRequest.getUsername();
        }
        return null;
    }

    public List<MachineDto> getAll(Boolean isSampleStore, String username){
        User user = userRepository.findByUsername(username);
        List<Machine> machines = machineRepository.findAllByUserId(user.getId());
        List<Machine> sampleMachines = machineRepository.findAlllSample();
        if(isSampleStore){
            for(Machine sampleMachine : sampleMachines){
                if(!machines.contains(sampleMachine)){
                    machines.add(sampleMachine);
                }
            }
        }

        List<MachineDto> machineDtos = new ArrayList<>();
        for (Machine machine : machines) {
            List<Integer> portContainerMapping = portContainerMappingRepository.findPortByMachineId(machine.getId());
            MachineDto machineDto = new MachineDto();
            machineDto.setId(machine.getId());
            machineDto.setName(machine.getName());
            machineDto.setDescription(machine.getDescription());
            machineDto.setRam(machine.getRam());
            machineDto.setCoreCpu(machine.getCoreCpu());
            machineDto.setMemory(machine.getMemory());
            machineDto.setImgSrc(machine.getImgSrc());
            machineDto.setIsSample(machine.getIsSample());
            machineDto.setOldPrice(machine.getOldPrice());
            machineDto.setNewPrice(machine.getNewPrice());
            machineDto.setImgSrc(machine.getImgSrc());
            if(isSampleStore){
                machineDto.setImgSrc("https://res.cloudinary.com/dlggnttqv/image/upload/v1733872539/bgr_hdegf1.jpg");
            }
            machineDto.setStatus(machine.getStatus());
            machineDto.setUserId(machine.getUserId());
            if(CollectionUtils.isEmpty(portContainerMapping)){
                machineDto.setPort(80);
            }
            else{
                machineDto.setPort(portContainerMapping.get(0));
            }
            machineDtos.add(machineDto);
        }
        return machineDtos;
    }
    public void deleteById(Integer id){
        machineRepository.deleteById(id);
    }
    public MachineDTO testMachine(MachineDTO machineDTO){
        Machine machine = new Machine();
        machine= machineRepository.getByName(machineDTO.getName());
        MachineDTO machineDTO1 = new MachineDTO();
        machineDTO1.setName(machineDTO.getName());
        machineDTO1.setDescription(machineDTO.getDescription());
        machineDTO1.setId(machineDTO.getId());
        return machineDTO1;
    }
    public MachinePortDTO getMachineByPort(Integer port){
        MachinePortDTO machinePortDTO = new MachinePortDTO();
        machinePortDTO.setMachineId(portContainerMappingRepository.getMachineByPort(port));
        return machinePortDTO;
    }

}
