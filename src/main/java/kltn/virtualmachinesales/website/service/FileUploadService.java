package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.response.CloudinaryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    CloudinaryResponseDTO uploadFile(MultipartFile multipartFile, final String fileName);
}