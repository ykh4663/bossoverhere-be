package com.dontgojunbao.bossoverhere.domain.user.controller.docs;

import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import com.dontgojunbao.bossoverhere.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface UserControllerDocs {

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인한 사용자의 계정을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "OK", value = """
                {
                  "success": true,
                  "data": null
                }
                """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "MISSING_ACCESS_TOKEN", value = """
                    { "code": "MISSING_ACCESS_TOKEN", "message": "액세스 토큰이 없습니다." }
                    """),
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN", value = """
                    { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                    """)
                            }
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용자 없음 (선택)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "NOTFOUND_USER", value = """
                { "code": "NOTFOUND_USER", "message": "해당 사용자를 찾을 수 없습니다." }
                """)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR", value = """
                { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                """)
                    )
            )
    })
    ResponseEntity<CommonResponse<Void>> deleteUser(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId);
}
