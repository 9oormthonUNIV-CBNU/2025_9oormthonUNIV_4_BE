package goormthon_group4.backend.domain.univcert.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/univcert")
@Tag(name = "대학 인증 api", description = "대학 인증 api")

public class UnivController {
}
