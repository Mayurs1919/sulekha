package Sulekhaai.WHBRD.services;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class OtaService {

    private static final String UPLOAD_DIR = "/home/sandeep-terwad/sulekha/uploads/firmwares";

    public String uploadFirmware(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Uploaded file is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID() + "_" + originalFilename;
        Path targetPath = Paths.get(UPLOAD_DIR + File.separator + uniqueName);

        // Ensure directory exists
        Files.createDirectories(targetPath.getParent());

        Files.copy(file.getInputStream(), targetPath);

        return "Firmware uploaded successfully as: " + uniqueName;
    }

    public Resource getLatestFirmware() throws IOException {
        File folder = new File(UPLOAD_DIR);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            throw new IOException("No firmware files found");
        }

        File latest = files[0];
        for (File file : files) {
            if (file.lastModified() > latest.lastModified()) {
                latest = file;
            }
        }
        return new FileSystemResource(latest);
    }
}