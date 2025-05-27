package goormthon_group4.backend.domain.univcert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnivCodeRequestDto {
    private String univName;
    private String univEmail;
    private int code;
}
