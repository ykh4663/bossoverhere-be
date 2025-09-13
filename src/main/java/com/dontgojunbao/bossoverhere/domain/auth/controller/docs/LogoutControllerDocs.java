package com.dontgojunbao.bossoverhere.domain.auth.controller.docs;

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

public interface LogoutControllerDocs {

    @Operation(
            summary = "로그아웃",
            description = "서버에 저장된 리프레시 토큰을 삭제합니다.",
            tags = "로그인"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success-no-content",
                                    value = """
                                    { "success": true }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Authorization 헤더 문제",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INVALID_AUTHORIZATION_HEADER",
                                    value = """
                                    { "code": "INVALID_AUTHORIZATION_HEADER", "message": "잘못된 Authorization 헤더입니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "엑세스 토큰 문제(만료/서명/형식/유효성)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                                            """),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                            { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
                                            """),
                                    @ExampleObject(name = "INVALID_JWT_SIGNATURE",
                                            value = """
                                            { "code": "INVALID_JWT_SIGNATURE", "message": "잘못된 JWT 서명입니다." }
                                            """),
                                    @ExampleObject(name = "UNSUPPORTED_TOKEN",
                                            value = """
                                            { "code": "UNSUPPORTED_TOKEN", "message": "지원되지 않는 토큰 형식입니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "ACCESS_DENIED",
                                    value = """
                                    { "code": "ACCESS_DENIED", "message": "권한이 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                    value = """
                                    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                                    """)
                    )
            )
    })
    ResponseEntity<CommonResponse<Void>> logout(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId
    );
}
