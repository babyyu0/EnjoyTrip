package com.ssafy.trip.member.model.dto.command;

import com.ssafy.trip.global.data.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public record ValidIdCommandDto(
        @NotBlank(message = "아이디가 존재하지 않습니다.")
        @Pattern(regexp = RegexPattern.EMAIL, message = "아이디가 올바르지 않습니다.")
        String memberId
) {
        @Builder
        public ValidIdCommandDto(String memberId) {
                this.memberId = memberId;
        }
}
