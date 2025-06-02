package goormthon_group4.backend.domain.team.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class OutputUploadDto {
    private List<MultipartFile> file;
}
