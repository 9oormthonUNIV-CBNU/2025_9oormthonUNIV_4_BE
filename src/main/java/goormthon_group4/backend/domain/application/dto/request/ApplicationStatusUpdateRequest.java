package goormthon_group4.backend.domain.application.dto.request;

import goormthon_group4.backend.domain.application.entity.ApplicationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationStatusUpdateRequest {
    private ApplicationStatus status;
}
