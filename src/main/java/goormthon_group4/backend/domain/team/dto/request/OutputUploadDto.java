package goormthon_group4.backend.domain.team.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OutputUploadDto {
    private MultipartFile file;
}
