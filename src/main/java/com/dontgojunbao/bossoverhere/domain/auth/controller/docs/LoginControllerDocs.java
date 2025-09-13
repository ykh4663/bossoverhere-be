package com.dontgojunbao.bossoverhere.domain.auth.controller.docs;


import com.dontgojunbao.bossoverhere.domain.auth.dto.request.OauthLoginRequest;
import com.dontgojunbao.bossoverhere.domain.auth.dto.request.RefreshTokenRequest;
import com.dontgojunbao.bossoverhere.domain.auth.dto.request.TokenHealthCheckRequest;
import com.dontgojunbao.bossoverhere.domain.auth.dto.response.OauthLoginResponse;
import com.dontgojunbao.bossoverhere.domain.auth.dto.response.RefreshedTokensResponse;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import com.dontgojunbao.bossoverhere.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

public interface LoginControllerDocs {

    @Operation(
            summary = "테스트 로그인",
            description = "개발 편의를 위해, 주어진 userId로 임시 액세스/리프레시 토큰을 발급합니다.",
            tags = "로그인"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "accessToken": "eyJhbGciOi...",
                                        "refreshToken": "eyJhbGciOi..."
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 파라미터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INVALID_PARAMETER",
                                    value = """
                                    { "code": "INVALID_PARAMETER", "message": "유효하지 않은 요청 파라미터가 포함되어 있습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "NOTFOUND_USER",
                                    value = """
                                    { "code": "NOTFOUND_USER", "message": "해당 사용자를 찾을 수 없습니다." }
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
    ResponseEntity<CommonResponse<OauthLoginResponse>> getToken(
            @Parameter(description = "임시 토큰을 발급할 사용자 ID", example = "1")
            Long userId
    );

    @Operation(
            summary = "OAuth 로그인",
            description = "OAuth 자격증명으로 로그인하고 액세스/리프레시 토큰을 발급합니다.",
            tags = "로그인"
    )
    @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = OauthLoginRequest.class))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "accessToken": "eyJhbGciOi...",
                                        "refreshToken": "eyJhbGciOi..."
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 파라미터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INVALID_PARAMETER",
                                    value = """
                                    { "code": "INVALID_PARAMETER", "message": "유효하지 않은 요청 파라미터가 포함되어 있습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "ID 토큰 없음/유효하지 않음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "ID_TOKEN_NOT_FOUND",
                                    value = """
                                    { "code": "ID_TOKEN_NOT_FOUND", "message": "ID 토큰을 찾을 수 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "OAuth 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "OAUTH_ERROR",
                                    value = """
                                    { "code": "OAUTH_ERROR", "message": "OAuth 오류" }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "OAuth ID 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "OAUTH_ID_NOT_FOUND",
                                    value = """
                                    { "code": "OAUTH_ID_NOT_FOUND", "message": "OAuth ID를 찾을 수 없습니다." }
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
    ResponseEntity<CommonResponse<OauthLoginResponse>> oauthLogin(OauthLoginRequest request);

    @Operation(
            summary = "토큰 리프레시",
            description = "리프레시 토큰을 검증하고 새로운 액세스/리프레시 토큰을 발급합니다.",
            tags = "로그인"
    )
    @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "accessToken": "eyJhbGciOi...",
                                        "refreshToken": "eyJhbGciOi..."
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "리프레시 토큰 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "MISSING_REFRESH_TOKEN",
                                    value = """
                                    { "code": "MISSING_REFRESH_TOKEN", "message": "리프레시 토큰이 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "리프레시 토큰 만료",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "EXPIRED_REFRESH_TOKEN",
                                    value = """
                                    { "code": "EXPIRED_REFRESH_TOKEN", "message": "리프레시 토큰이 만료되었습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "리프레시 토큰 불일치/유효하지 않음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INVALID_REFRESH_TOKEN",
                                    value = """
                                    { "code": "INVALID_REFRESH_TOKEN", "message": "유효하지 않은 리프레시 토큰입니다." }
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
    ResponseEntity<CommonResponse<RefreshedTokensResponse>> refreshToken(RefreshTokenRequest request);

    @Operation(
            summary = "토큰 헬스체크",
            description = "토큰 만료 여부를 확인합니다. 만료 시 에러로 응답합니다.",
            tags = "로그인"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "정상(만료 아님)",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success-no-content",
                                    value = """
                                    { "success": true }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 만료",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "EXPIRED_TOKEN",
                                    value = """
                                    { "code": "EXPIRED_TOKEN", "message": "토큰이 만료되었습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 파라미터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INVALID_PARAMETER",
                                    value = """
                                    { "code": "INVALID_PARAMETER", "message": "유효하지 않은 요청 파라미터가 포함되어 있습니다." }
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
    ResponseEntity<CommonResponse<Void>> tokenHealthCheck(
            @ParameterObject TokenHealthCheckRequest request
    );
}