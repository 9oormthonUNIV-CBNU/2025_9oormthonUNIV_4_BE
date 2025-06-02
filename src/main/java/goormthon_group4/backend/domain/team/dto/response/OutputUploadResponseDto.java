package goormthon_group4.backend.domain.team.dto.response;

import goormthon_group4.backend.domain.team.entity.Output;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OutputUploadResponseDto {
    private Long id;
    private String fileUrl;

    public static OutputUploadResponseDto from(Output output) {
        return OutputUploadResponseDto.builder()
                .id(output.getId())
                .fileUrl(output.getFileUrl())
                .build();
    }
}
